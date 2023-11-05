@file:Suppress("unused")
package io.github.fhanko.kplugin.util

import jakarta.persistence.*
import org.bukkit.entity.Player
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*

@MappedSuperclass
open class PlayerCard(
    @Id val uuid: UUID,
    @Column val name: String
) {
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = [JoinColumn(name = "player_uuid")])
    open val cooldowns = mutableListOf<Cooldown>()

    protected fun update() { HibernateUtil.saveEntity(this, HibernateUtil.Operation.Merge) }

    fun getCooldown(hash: String) = cooldowns.find{ it ->
        it.hash == hash && it.time > Timestamp(System.currentTimeMillis())}?.time?.time?.minus(System.currentTimeMillis())

    fun setCooldown(hash: String, milliseconds: Long) {
        cooldowns.removeIf { it.hash == hash }
        cooldowns.add(Cooldown(hash, Timestamp(System.currentTimeMillis().plus(milliseconds))))
        if (milliseconds > 60000) update()
    }
}

@Embeddable
class Cooldown(
    @Column val hash: String,
    @Column var time: Timestamp
)

@Entity
class EconomyCard(uuid: UUID, name: String): PlayerCard(uuid, name) {
    @Column
    var balance: BigDecimal = BigDecimal(0)
        private set
    fun addBalance(value: BigDecimal) { balance = balance.add(value); update() }
    companion object {
        fun getCard(player: Player) =
            HibernateUtil.loadOrPersistDefault(EconomyCard(player.uniqueId, player.name), player.uniqueId)
    }
}