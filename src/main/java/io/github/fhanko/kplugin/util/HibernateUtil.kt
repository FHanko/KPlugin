package io.github.fhanko.kplugin.util

import io.github.fhanko.kplugin.KPlugin
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.service.ServiceRegistry
import java.io.Serializable
import java.util.concurrent.LinkedBlockingQueue

object HibernateUtil {
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
    }

    fun shutdown() {
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

    fun emplaceEntity(obj: Any, key: Any) {
        execute { session ->
            if (session.find(obj::class.java, key) == null) {
                session.persist(obj)
            }
        }
    }

    fun <T> execute(unit: (s: Session) -> T): T? {
        val session: Session = fac.openSession()

        var transaction: Transaction? = null
        try {
            transaction = session.beginTransaction()
            val ret = unit(session)
            transaction.commit()
            return ret
        } catch (e: Exception) {
            transaction?.rollback()
            KPlugin.instance.logger.warning(e.message)
            e.printStackTrace()
        } finally {
            session.close()
        }
        return null
    }
}