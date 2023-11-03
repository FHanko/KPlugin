@file:Suppress("unused")
package io.github.fhanko.kplugin.util

import jakarta.persistence.*
import org.bukkit.entity.Player
import java.math.BigDecimal
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
class PlayerCard(
    @Id val uuid: UUID,
    @Column val name: String,
    @Column var balance: BigDecimal) {
    fun update() { HibernateUtil.saveEntity(this, HibernateUtil.Operation.Merge) }
    fun addBalance(value: BigDecimal) { balance = balance.add(value); update() }
}