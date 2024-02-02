package io.github.fhanko.entityhandler

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent
import io.github.fhanko.EntityBase
import org.bukkit.entity.Display
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityInteractEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.world.EntitiesLoadEvent
import java.util.*

/**
 * Handles [Entity] events. Keeps a consistent list of unique(persistent) entity ids mapped to their [Display]s.
 */
object EntityListener: Listener {
    val displayIds = mutableMapOf<UUID, Display>()

    @EventHandler
    fun onEntityLoad(e: EntitiesLoadEvent) {
        e.entities.forEach { if (it is Display) { displayIds[it.uniqueId] = it } }
    }

    @EventHandler
    fun onEntitySpawn(e: EntitySpawnEvent) {
        val d = e.entity
        if (d is Display){ displayIds[d.uniqueId] = d }
    }

    @EventHandler
    fun onEntityRemove(e: EntityRemoveFromWorldEvent) {
        val d = e.entity
        if (d is Display){ displayIds.remove(e.entity.uniqueId) }
    }

    @EventHandler
    fun onEntityDamage(e: EntityDamageEvent) {
        EntityBase.get(e.entity)?.also { if (it is DamageHandler) it.damage(e) }
    }

    @EventHandler
    fun onEntityDeath(e: EntityDeathEvent) {
        EntityBase.get(e.entity)?.also { if (it is DeathHandler) it.death(e) }
    }

    @EventHandler
    fun onEntityInteract(e: PlayerInteractEntityEvent) {
        EntityBase.get(e.rightClicked)?.also { if (it is InteractHandler) it.onInteract(e) }
    }
}