package com.github.fhanko.exampleplugin.entities

import com.github.fhanko.entity.EntityBase
import com.github.fhanko.util.Initializable
import com.github.fhanko.entity.entityhandler.DeathHandler
import com.github.fhanko.entity.entityhandler.InteractHandler
import com.github.fhanko.entity.entityhandler.NotDamageableHandler
import com.github.fhanko.entity.goals.LookAtPlayerGoal
import com.github.fhanko.entity.goals.MoveToTargetGoal
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerInteractEntityEvent

object TestEntity: EntityBase(1, EntityType.ZOMBIE), DeathHandler, InteractHandler, NotDamageableHandler,
    Initializable {
    override fun death(e: EntityDeathEvent) {
        val killer = e.entity.killer
        if (killer !is Player) return
        killer.sendMessage("Killed test.")
    }

    override fun rightClick(e: PlayerInteractEntityEvent) {
        //e.rightClicked.location.direction = e.player.location.subtract(e.rightClicked.location).toVector()
        e.player.sendMessage("Clicked test.")
    }

    override fun onSpawn(entity: Entity) {
        if (entity !is Mob) return
        addGoal(entity, MoveToTargetGoal(entity, entity.location, 1.0, 0))
        addGoal(entity, LookAtPlayerGoal(entity, 2.0, 1))
    }
}