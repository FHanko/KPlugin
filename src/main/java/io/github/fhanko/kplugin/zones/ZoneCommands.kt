package io.github.fhanko.kplugin.zones

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.DoubleArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import io.github.fhanko.kplugin.items.ItemFactory
import io.github.fhanko.kplugin.items.ItemListener
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

object ZoneCommands {
    fun register() {
        ItemListener.registerAction(0, ::zoneItem)

        CommandAPICommand("cubezone")
            .withArguments(listOf(DoubleArgument("x1"), DoubleArgument("y1"), DoubleArgument("z1"),
                DoubleArgument("x2"), DoubleArgument("y2"), DoubleArgument("z2")))
            .executesPlayer(PlayerCommandExecutor { p, a ->
                ZoneMap.addZone(ZoneCube(Location(p.world, a[0] as Double, a[1] as Double, a[2] as Double)
                    , Location(p.world, a[3] as Double, a[4] as Double, a[5] as Double)))
            }).register()

        CommandAPICommand("cubezone")
            .executesPlayer(PlayerCommandExecutor { p, a ->
                p.inventory.addItem(ItemFactory.create(Material.STICK, "Cube Stick", listOf("Creates cube zones"), 1, 0))
            }).register()
    }

    val zoneItemMap = mutableMapOf<Player, Location>()
    fun zoneItem(e: PlayerInteractEvent) {
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
            } else {
                zoneItemMap[e.player] = e.clickedBlock?.location ?: return
                e.player.sendMessage("Location set.")
            }
        }
    }
}