package io.github.fhanko.kplugin.zones

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import io.github.fhanko.kplugin.items.objects.ZoneItem

object ZoneCommands {
    fun register() {
        CommandAPICommand("zonecube")
            .executesPlayer(PlayerCommandExecutor { p, _ ->
                ZoneItem.give(p)
            }).register()
    }
}