package io.github.fhanko.kplugin.items.objects

import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.items.ItemClickable
import io.github.fhanko.kplugin.items.ItemEquippable
import io.github.fhanko.kplugin.zones.ZoneChunkMap
import io.github.fhanko.kplugin.zones.objects.ZoneHeal
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent

object ZoneItem : ItemBase(1, Material.STICK, "Cube Stick", listOf("Creates cube zones")), ItemClickable, ItemEquippable {
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

    private val visualisationTaskMap = mutableMapOf<Player, Int>()
    override fun equip(p: Player, e: ItemEquippable.EquipType) {
        val tid = KPlugin.instance.server.scheduler.scheduleSyncRepeatingTask(KPlugin.instance, {
            ZoneChunkMap.getZones(p.chunk)?.forEach { z ->
                z.borders.forEach {
                    p.world.spawnParticle(Particle.REDSTONE, it,3, Particle.DustOptions(z.borderColor, 0.5f))
                }
            }
        }, 0, 10)
        visualisationTaskMap[p] = tid;
        println(tid)
    }

    override fun unequip(p: Player, e: ItemEquippable.EquipType) {
        if (!visualisationTaskMap.containsKey(p)) return
        KPlugin.instance.server.scheduler.cancelTask(visualisationTaskMap[p]!!)
    }
}
