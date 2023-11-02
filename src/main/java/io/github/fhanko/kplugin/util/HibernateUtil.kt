package io.github.fhanko.kplugin.util

import io.github.fhanko.kplugin.KPlugin
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.cfg.Configuration
import java.io.Serializable

object HibernateUtil {
    private lateinit var fac: SessionFactory

    fun createSessionFactory() {
        val configuration = Configuration()
        fac = configuration.configure().buildSessionFactory()
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

    enum class Operation { Save, Update }
    fun saveEntity(obj: Any, operation: Operation): Boolean {
        val session: Session = fac.openSession()
        var transaction: Transaction? = null

        try {
            transaction = session.beginTransaction()
            if(operation == Operation.Save) session.save(obj) else session.update(obj)
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
}