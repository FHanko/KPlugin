package io.github.fhanko.kplugin.util

import io.github.fhanko.kplugin.KPlugin
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import org.hibernate.service.ServiceRegistry
import java.io.Serializable


object HibernateUtil {
    private lateinit var fac: SessionFactory

    fun createSessionFactory() {
        val configuration = Configuration()
        val serviceRegistry: ServiceRegistry = StandardServiceRegistryBuilder().applySettings(
            configuration.properties).build()
        fac = configuration.configure().buildSessionFactory(serviceRegistry)
    }

    fun shutdown() {
        fac.close()
    }

    fun <T> loadEntity(obj: Class<out T>, id: Serializable): T? {
        val session: Session = fac.openSession()
        var transaction: Transaction? = null

        try {
            transaction = session.beginTransaction()
            val ret = session.get(obj, id)
            transaction.commit()
            @Suppress("UNCHECKED_CAST") return ret as T
        } catch (e: Exception) {
            transaction?.rollback()
            KPlugin.instance.logger.warning(e.message)
            e.printStackTrace()
        } finally {
            session.close()
        }
        return null
    }

    enum class Operation { Save, Update, SaveOrUpdate, Persist }
    fun saveEntity(obj: Any, operation: Operation): Boolean {
        val session: Session = fac.openSession()
        var transaction: Transaction? = null

        try {
            transaction = session.beginTransaction()
            when(operation) {
                Operation.Save -> session.save(obj)
                Operation.Update -> session.update(obj)
                Operation.SaveOrUpdate -> session.saveOrUpdate(obj)
                Operation.Persist -> session.persist(obj)
            }
            transaction.commit()
            return true
        } catch (e: Exception) {
            transaction?.rollback()
            KPlugin.instance.logger.warning(e.message)
            e.printStackTrace()
        } finally {
            session.close()
        }
        return false
    }

    fun execute(unit: () -> Unit) {
        val session: Session = fac.openSession()

        var transaction: Transaction? = null
        try {
            transaction = session.beginTransaction()
            unit()
            transaction.commit()
        } catch (e: Exception) {
            transaction?.rollback()
            KPlugin.instance.logger.warning(e.message)
            e.printStackTrace()
        } finally {
            session.close()
        }
    }
}