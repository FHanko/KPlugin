package io.github.fhanko.kplugin

import io.github.fhanko.kplugin.util.HibernateUtil
import io.github.fhanko.kplugin.util.Initializable
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.world.WorldSaveEvent

/**
 * This listener handles events which are not specifically related to any of the other packages
 */
object DefaultListener: Listener, Initializable {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        e.player.sendMessage("Welcome!")
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onWorldSave(e: WorldSaveEvent) {
        if (e.world.environment != World.Environment.NORMAL) return
        HibernateUtil.postWorldSave(e)
    }
}