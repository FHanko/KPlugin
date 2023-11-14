package io.github.fhanko.kplugin.display

import io.github.fhanko.kplugin.util.Initializable
import org.bukkit.entity.Display
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.world.EntitiesLoadEvent
import java.util.*

/**
 * Keeps a consistent list of unique(persistent) entity ids mapped to their [Display]s.
 */
object DisplayListener: Listener {
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
}