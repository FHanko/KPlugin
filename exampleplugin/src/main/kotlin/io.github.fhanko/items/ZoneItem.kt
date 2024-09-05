package io.github.fhanko.items

import io.github.fhanko.CubeDisplay
import io.github.fhanko.ItemBase
import io.github.fhanko.Schedulable
import io.github.fhanko.ZoneChunkMap
import io.github.fhanko.itemhandler.ClickHandler
import io.github.fhanko.itemhandler.EquipHandler
import io.github.fhanko.zones.ZoneHeal
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent

object ZoneItem : ItemBase(1, Material.STICK, "Cube Stick", listOf("Creates cube zones")), ClickHandler,
    EquipHandler, Schedulable {
    private val zoneItemMap = mutableMapOf<Player, Location>()

    override fun leftClick(e: PlayerInteractEvent) {
        zoneItemMap.remove(e.player)
        e.player.sendMessage("Location reset.")
    }

    override fun rightClick(e: PlayerInteractEvent) {
        val firstLoc = zoneItemMap[e.player]
        if (firstLoc != null) {
            val secondLoc = e.interactionPoint ?: return
            ZoneChunkMap.addZone(ZoneHeal(ZoneChunkMap.newId(), firstLoc, secondLoc))
            e.player.sendMessage("Zone added ${firstLoc.x} ${firstLoc.y} ${firstLoc.z} " +
                    "to ${secondLoc.x} ${secondLoc.y} ${secondLoc.z}.")
            zoneItemMap.remove(e.player)
        } else {
            zoneItemMap[e.player] = e.interactionPoint ?: return
            e.player.sendMessage("Location set.")
        }
    }

    private val cubeMap = mutableMapOf<Player, MutableList<CubeDisplay>>()
    override fun equip(p: Player, e: EquipHandler.EquipType) {
        cubeMap[p] = mutableListOf()
        ZoneChunkMap.getRadiusZones(p.chunk, 1).forEach { z ->
            cubeMap[p]!!.add(CubeDisplay(z.start, z.end))
        }
    }

    override fun unequip(p: Player, e: EquipHandler.EquipType) {
        cubeMap[p]!!.forEach { it.remove() }
        cubeMap[p]!!.clear()
    }
}

