package io.github.fhanko.kplugin

import io.github.fhanko.kplugin.util.PlayerStorage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class EventListener: Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        PlayerStorage.register(e.player)
    }
}