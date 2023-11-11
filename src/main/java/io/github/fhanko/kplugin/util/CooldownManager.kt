package io.github.fhanko.kplugin.util

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.sql.Timestamp

object CooldownManager {
    private val cooldowns = mutableMapOf<String, Cooldown>()

    fun getCooldown(hash: String, fetch: Boolean = false): Long? {
        return if (fetch)
            HibernateUtil.loadEntity(Cooldown::class.java, hash)?.getRemaining()
            else
            cooldowns.getOrDefault(hash, null)?.getRemaining()
    }

    fun setCooldown(hash: String, milliseconds: Long, persist: Boolean = false) {
        val newCooldown = Cooldown(hash, Timestamp(System.currentTimeMillis().plus(milliseconds)))
        if (persist) HibernateUtil.putEntity(hash, newCooldown)
        else cooldowns[hash] = newCooldown
    }
}

@Entity
class Cooldown(
    @Id val hash: String,
    @Column var time: Timestamp
) {
    fun getRemaining() = time.time.minus(System.currentTimeMillis())
}

