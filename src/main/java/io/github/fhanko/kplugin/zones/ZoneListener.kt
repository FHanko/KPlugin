package io.github.fhanko.kplugin.zones

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class ZoneListener : Listener {
    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val zoneCollisions = mutableListOf<Zone>()
        ZoneMap.getZones(e.player.chunk)?.forEach { z-> if (z.isIn(e.player.location.add(0.0, 1.0, 0.0))) zoneCollisions.add(z) }

        zoneCollisions.forEach { _ -> println("Is in"); }
    }
}