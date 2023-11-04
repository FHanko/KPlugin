package io.github.fhanko.kplugin.util

import io.github.fhanko.kplugin.KPlugin
import jakarta.persistence.EntityManager
import jakarta.persistence.Persistence
import jakarta.persistence.PersistenceContext
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldSaveEvent
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import java.io.Serializable
import java.util.concurrent.LinkedBlockingQueue


object HibernateUtil: Listener {
    private lateinit var em: EntityManager

    fun createSessionFactory() {
        // https://sjhannah.com/blog/2018/11/21/jaxb-hell-on-jdk-9/
        val queue = LinkedBlockingQueue<EntityManager>()
        val t = Thread {
            val emf = Persistence.createEntityManagerFactory("PUnit")
            queue.add(emf.createEntityManager())
        }
        t.setContextClassLoader(javaClass.getClassLoader())
        t.start()

        em = queue.take()
        em.transaction.begin()
    }

    fun shutdown() {
        em.flush()
        em.transaction.commit()
        KPlugin.instance.logger.info("Database saved.")
        em.close()
    }

    fun <T> loadEntity(obj: Class<out T>, id: Serializable): T? {
        return execute { em ->
            return@execute em.find(obj, id)
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
        return execute { em ->
            val builder = em.criteriaBuilder
            val criteria = builder.createQuery(obj)
            criteria.from(obj)
            return@execute em.createQuery(criteria).resultList
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

    fun <T> execute(unit: (s: EntityManager) -> T): T? {
        try {
            return unit(em)
        } catch (e: Exception) {
            rollbackTransaction(e)
            em.transaction.begin()
        }
        return null
    }

    private fun rollbackTransaction(e: Exception) {
        em.transaction.rollback()
        KPlugin.instance.logger.warning(e.message)
        e.printStackTrace()
    }

    @EventHandler
    fun onWorldSave(e: WorldSaveEvent) {
        if (e.world.environment != World.Environment.NORMAL) return

        try {
            em.flush()
            em.transaction.commit()
            KPlugin.instance.logger.info("Database saved.")
        } catch (e: Exception) {
            rollbackTransaction(e)
        } finally {
            em.transaction.begin()
        }
    }
}