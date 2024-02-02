package io.github.fhanko.entities

import io.github.fhanko.EntityBase
import io.github.fhanko.entityhandler.DamageHandler
import io.github.fhanko.entityhandler.DeathHandler
import io.github.fhanko.entityhandler.InteractHandler
import io.github.fhanko.entityhandler.NotDamageableHandler
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerInteractEntityEvent

object TestEntity: EntityBase(1, EntityType.ZOMBIE), DeathHandler, InteractHandler, NotDamageableHandler {
    override fun death(e: EntityDeathEvent) {
        val killer = e.entity.killer
        if (killer !is Player) return
        killer.sendMessage("Killed test.")
    }

    override fun rightClick(e: PlayerInteractEntityEvent) {
        e.player.sendMessage("Clicked test.")
    }
}