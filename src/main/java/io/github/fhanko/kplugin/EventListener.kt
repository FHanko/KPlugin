package io.github.fhanko.kplugin

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class EventListener: Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
    }
}