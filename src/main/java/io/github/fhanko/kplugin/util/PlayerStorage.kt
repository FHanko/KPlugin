@file:Suppress("unused")
package io.github.fhanko.kplugin.util

import io.github.fhanko.kplugin.blocks.objects.ConnectedChest
import jakarta.persistence.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldSaveEvent
import java.math.BigDecimal
import java.util.*

object PlayerStorage: Listener {
    private val playerList = HibernateUtil.loadAll(PlayerCard::class.java)?.toMutableList() ?: mutableListOf<PlayerCard>()

    fun register(p: Player) =
        HibernateUtil.emplaceEntity(PlayerCard(p.identity().uuid(), p.name, BigDecimal(0)), p.identity().uuid())

    fun getCard(p: Player): PlayerCard? = playerList.find { it.uuid == p.identity().uuid() }

    @EventHandler
    fun onWorldSave(e: WorldSaveEvent) = HibernateUtil.saveCollection(playerList, HibernateUtil.Operation.Merge)
}

@Entity @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
class PlayerCard(
    @Id val uuid: UUID,
    @Column val name: String,
    @Column var balance: BigDecimal) {
    fun addBalance(value: BigDecimal) { balance = balance.add(value) }
}