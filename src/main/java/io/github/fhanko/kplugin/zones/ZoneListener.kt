package io.github.fhanko.kplugin.zones

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class ZoneListener : Listener {
    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        var zoneCollisions = mutableListOf<Zone>();
        ZoneMap.getZones(e.player.chunk)?.forEach { if (it.isIn(e.player.location)) zoneCollisions.add(it) }

        zoneCollisions.forEach { println("Is in"); }
    }
}