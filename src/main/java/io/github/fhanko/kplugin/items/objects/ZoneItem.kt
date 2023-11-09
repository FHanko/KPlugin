package io.github.fhanko.kplugin.items.objects

import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.items.ItemClickable
import io.github.fhanko.kplugin.items.ItemEquippable
import io.github.fhanko.kplugin.util.Schedulable
import io.github.fhanko.kplugin.zones.ZoneChunkMap
import io.github.fhanko.kplugin.zones.objects.ZoneHeal
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent

object ZoneItem : ItemBase(1, Material.STICK, "Cube Stick", listOf("Creates cube zones")), ItemClickable, ItemEquippable, Schedulable {
    private val zoneItemMap = mutableMapOf<Player, Location>()

    override fun leftClick(e: PlayerInteractEvent) {
        zoneItemMap.remove(e.player)
        e.player.sendMessage("Location reset.")
    }

    override fun rightClick(e: PlayerInteractEvent) {
        val firstLoc = zoneItemMap[e.player]
        if (firstLoc != null) {
            val secondLoc = e.interactionPoint ?: return
            ZoneChunkMap.addZone(ZoneHeal(firstLoc, secondLoc))
            e.player.sendMessage("Zone added ${firstLoc.x} ${firstLoc.y} ${firstLoc.z} " +
                    "to ${secondLoc.x} ${secondLoc.y} ${secondLoc.z}.")
            zoneItemMap.remove(e.player)
        } else {
            zoneItemMap[e.player] = e.interactionPoint ?: return
            e.player.sendMessage("Location set.")
        }
    }

    override fun equip(p: Player, e: ItemEquippable.EquipType) {
        scheduleRepeat(p.uniqueId.toString(), 200, p)
    }

    override fun runSchedule(vararg params: Any) {
        val p = params[0] as Player
        ZoneChunkMap.getZones(p.chunk)?.forEach { z ->
            z.borders.forEach {
                p.world.spawnParticle(Particle.REDSTONE, it,3, Particle.DustOptions(z.borderColor, 0.5f))
            }
        }
    }

    override fun unequip(p: Player, e: ItemEquippable.EquipType) {
        cancelSchedule(p.uniqueId.toString())
    }
}

