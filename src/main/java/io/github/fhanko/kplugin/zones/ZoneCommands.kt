package io.github.fhanko.kplugin.zones

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.DoubleArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import io.github.fhanko.kplugin.items.ItemBase
import org.bukkit.Location

object ZoneCommands {
    fun register() {
        CommandAPICommand("cubezone")
            .withArguments(listOf(DoubleArgument("x1"), DoubleArgument("y1"), DoubleArgument("z1"),
                DoubleArgument("x2"), DoubleArgument("y2"), DoubleArgument("z2")))
            .executesPlayer(PlayerCommandExecutor { p, a ->
                ZoneMap.addZone(ZoneCube(Location(p.world, a[0] as Double, a[1] as Double, a[2] as Double)
                    , Location(p.world, a[3] as Double, a[4] as Double, a[5] as Double)))
            }).register()

        CommandAPICommand("cubezone")
            .executesPlayer(PlayerCommandExecutor { p, _ ->
                ItemBase.give(p, ZoneItem)
            }).register()
    }
}