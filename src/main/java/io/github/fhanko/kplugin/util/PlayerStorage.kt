@file:Suppress("unused")
package io.github.fhanko.kplugin.util

import jakarta.persistence.*
import org.bukkit.entity.Player
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*

object PlayerStorage {
    private val playerList = HibernateUtil.loadAll(PlayerCard::class.java)?.toMutableList() ?: mutableListOf()

    fun register(p: Player) {
        if (!playerList.any { it.uuid == p.identity().uuid() }) HibernateUtil.saveEntity(
            PlayerCard(p.identity().uuid(), p.name, BigDecimal(0)), HibernateUtil.Operation.Persist)
    }

    fun getCard(p: Player): PlayerCard? = playerList.find { it.uuid == p.identity().uuid() }
}

@Entity @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
open class PlayerCard(
    @Id open val uuid: UUID,
    @Column open val name: String,
    @Column open var balance: BigDecimal) {
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cooldown", joinColumns = [JoinColumn(name = "player_uuid")])
    open val cooldowns = mutableListOf<Cooldown>()

    fun update() { HibernateUtil.saveEntity(this, HibernateUtil.Operation.Merge) }
    fun addBalance(value: BigDecimal) { balance = balance.add(value); update() }

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