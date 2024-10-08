package com.github.fhanko.zones.zonehandler

import com.github.fhanko.util.Initializable
import com.github.fhanko.zones.Zone
import com.github.fhanko.zones.ZoneChunkMap
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

object ZoneListener : Listener, Initializable {
    // Maps Players to zones they are inside of
    private val playerZoneMap = mutableMapOf<Player, HashSet<Zone>>()

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val pzmap = playerZoneMap.getOrPut(e.player) { HashSet() }
        val zoneCollisions = (ZoneChunkMap.getZones(e.player.chunk)?.filter { z->
            z is EnterHandler && z.isIn(e.player.location.add(0.0, 1.0, 0.0))
        } ?: HashSet())

        // Enter new collisions
        zoneCollisions.forEach { z -> z as EnterHandler
            if (!pzmap.contains(z)) { z.enter(e.player); pzmap.add(z) }
        }

        // Leave non collisions
        pzmap.filter { z -> !zoneCollisions.contains(z) }.forEach { z -> z as EnterHandler
            pzmap.remove(z); z.leave(e.player)
        }
    }
}