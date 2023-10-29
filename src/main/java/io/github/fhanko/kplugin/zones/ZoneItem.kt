package io.github.fhanko.kplugin.zones

import io.github.fhanko.kplugin.items.ItemBase
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent

object ZoneItem : ItemBase(Material.STICK, "Cube Stick", listOf("Creates cube zones")) {
    private val zoneItemMap = mutableMapOf<Player, Location>()

    override fun onClick(e: PlayerInteractEvent) {
        if (e.action.isLeftClick) {
            zoneItemMap.remove(e.player)
            e.player.sendMessage("Location reset.")
        } else {
            val firstLoc = zoneItemMap[e.player]
            if (firstLoc != null) {
                val secondLoc = e.clickedBlock?.location ?: return
                ZoneMap.addZone(ZoneCube(firstLoc, secondLoc))
                e.player.sendMessage("Zone added ${firstLoc.x} ${firstLoc.y} ${firstLoc.z} " +
                        "to ${secondLoc.x} ${secondLoc.y} ${secondLoc.z}.")
                zoneItemMap.remove(e.player)
                e.player.inventory.remove(e.item!!);
            } else {
                zoneItemMap[e.player] = e.clickedBlock?.location ?: return
                e.player.sendMessage("Location set.")
            }
        }
        return
    }
}