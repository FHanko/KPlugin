package io.github.fhanko.entities

import io.github.fhanko.EntityBase
import io.github.fhanko.entityhandler.DamageHandler
import io.github.fhanko.entityhandler.DeathHandler
import io.github.fhanko.entityhandler.InteractHandler
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerInteractEntityEvent

object TestEntity: EntityBase(1, EntityType.ZOMBIE), DamageHandler, DeathHandler, InteractHandler {
    override fun damage(e: EntityDamageEvent) {
        if (e !is EntityDamageByEntityEvent) return
        e.damager.sendMessage("Damaged test.")
    }

    override fun death(e: EntityDeathEvent) {
        val killer = e.entity.killer
        if (killer !is Player) return
        killer.sendMessage("Killed test.")
    }

    override fun rightClick(e: PlayerInteractEntityEvent) {
        e.player.sendMessage("Clicked test.")
    }
}