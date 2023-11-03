package io.github.fhanko.kplugin.util

import io.github.fhanko.kplugin.KPlugin
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldSaveEvent
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.service.ServiceRegistry
import java.io.Serializable
import java.util.concurrent.LinkedBlockingQueue

object HibernateUtil: Listener {
    private lateinit var fac: SessionFactory

    fun createSessionFactory() {
        val queue = LinkedBlockingQueue<SessionFactory>()
        // https://sjhannah.com/blog/2018/11/21/jaxb-hell-on-jdk-9/
        val t = Thread {
            val serviceRegistry: ServiceRegistry =
                StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build()
            val metadata = MetadataSources(serviceRegistry).metadataBuilder.build()
            fac = metadata.sessionFactoryBuilder.build()
            queue.add(fac)
        }

        t.setContextClassLoader(javaClass.getClassLoader())
        t.start()

        fac = queue.take()
        startTransaction()
    }

    fun shutdown() {
        val session: Session = fac.currentSession
        session.flush()
        transaction.commit()
        KPlugin.instance.logger.info("Database saved.")
        fac.close()
    }

    fun <T> loadEntity(obj: Class<out T>, id: Serializable): T? {
        return execute { session ->
            return@execute session.get(obj, id)
        }
    }

    enum class Operation { Persist, Merge }
    fun saveEntity(obj: Any, operation: Operation): Boolean {
        val ret = execute { session ->
            when(operation) {
                Operation.Persist -> session.persist(obj)
                Operation.Merge -> session.merge(obj)
            }
            return@execute true
        }
        return ret ?: false
    }

    fun <T> loadAll(obj: Class<out T>): List<T>? {
        return execute { session ->
            val builder = session.criteriaBuilder
            val criteria = builder.createQuery(obj)
            criteria.from(obj)
            return@execute session.createQuery(criteria).resultList
        }
    }

    fun saveCollection(obj: Collection<Any>, operation: Operation): Boolean {
        val ret = execute { session ->
            when(operation) {
                Operation.Persist -> obj.forEach { session.persist(it) }
                Operation.Merge -> obj.forEach { session.merge(it) }
            }
            return@execute true
        }
        return ret ?: false
    }

    fun <T> execute(unit: (s: Session) -> T): T? {
        val session: Session = fac.currentSession

        try {
            return unit(session)
        } catch (e: Exception) {
            rollbackTransaction(e)
            startTransaction()
        }
        return null
    }

    private lateinit var transaction: Transaction
    private fun startTransaction() {
        val session: Session = fac.currentSession
        transaction = session.beginTransaction()
    }

    private fun rollbackTransaction(e: Exception) {
        transaction.rollback()
        KPlugin.instance.logger.warning(e.message)
        e.printStackTrace()
    }

    @EventHandler
    fun onWorldSave(e: WorldSaveEvent) {
        if (e.world.environment != World.Environment.NORMAL) return

        val session: Session = fac.currentSession
        try {
            session.flush()
            transaction.commit()
            KPlugin.instance.logger.info("Database saved.")
        } catch (e: Exception) {
            rollbackTransaction(e)
        } finally {
            session.close()
            fac.openSession()
            startTransaction()
        }
    }
}