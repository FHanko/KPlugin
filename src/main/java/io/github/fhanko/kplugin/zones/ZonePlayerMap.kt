package io.github.fhanko.kplugin.zones

import io.github.fhanko.kplugin.util.Initializable
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class ZonePlayerMap : Listener, Initializable {
    private val map = mutableMapOf<Player, HashSet<Zone>>()

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        if (map[e.player] == null) map[e.player] = mutableSetOf<Zone>().toHashSet()

        val zoneCollisions = ZoneChunkMap.getZones(e.player.chunk)
            ?.filter { z-> z.isIn(e.player.location.add(0.0, 1.0, 0.0)) } ?: mutableSetOf<Zone>().toHashSet()

        // Enter new collisions
        zoneCollisions.forEach { z -> if (!map[e.player]!!.contains(z)) { z.enter(e.player); map[e.player]!!.add(z) } }

        // Leave non collisions
        map[e.player]!!.filter { z -> !zoneCollisions.contains(z) }.forEach { z -> map[e.player]!!.remove(z); z.leave(e.player) }
    }
}