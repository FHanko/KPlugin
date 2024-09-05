package io.github.fhanko

import jakarta.persistence.EntityManager
import jakarta.persistence.Persistence
import org.bukkit.Bukkit
import org.bukkit.event.world.WorldSaveEvent
import java.io.Serializable
import java.util.concurrent.LinkedBlockingQueue


object HibernateUtil {
    private lateinit var em: EntityManager
    private var initialized = false

    fun createSessionFactory(persistenceUnitName: String) {
        // https://sjhannah.com/blog/2018/11/21/jaxb-hell-on-jdk-9/
        val queue = LinkedBlockingQueue<EntityManager>()
        val t = Thread {
            val emf = Persistence.createEntityManagerFactory(persistenceUnitName)
            queue.add(emf.createEntityManager())
        }
        t.setContextClassLoader(javaClass.getClassLoader())
        t.start()

        em = queue.take()
        em.transaction.begin()

        initialized = true
    }

    fun shutdown() {
        em.flush()
        em.transaction.commit()
        PluginInstance.instance.logger.info("Database saved.")
        em.close()
    }

    fun <T> loadEntity(obj: Class<out T>, id: Serializable): T? {
        return execute { em ->
            return@execute em.find(obj, id)
        }
    }

    fun <T> loadOrPersistDefault(default: T, id: Serializable): T {
        return execute { em ->
            val ret = em.find(default!!::class.java, id)
            if (ret == null) {
                em.persist(default)
                return@execute default
            }
            return@execute ret
        }!!
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

    fun putEntity(id: Serializable, obj: Any): Boolean {
        val ret = execute { session ->
            if (!session.contains(obj) && session.find(obj::class.java, id) == null) session.persist(obj) else session.merge(obj)
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
        PluginInstance.instance.logger.warning(e.message)
        PluginInstance.instance.logger.warning(e.stackTraceToString())
    }

    fun postWorldSave(e: WorldSaveEvent) {
        if (!initialized) return
        try {
            em.flush()
            em.clear()
            em.transaction.commit()
            PluginInstance.instance.logger.info("Database saved.")
        } catch (e: Exception) {
            rollbackTransaction(e)
        } finally {
            em.transaction.begin()
        }
    }
}