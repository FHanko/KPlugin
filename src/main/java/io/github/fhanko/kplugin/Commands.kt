package io.github.fhanko.kplugin

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import io.github.fhanko.kplugin.items.ItemBase

object Commands {
    fun register() {
        CommandAPICommand("givekp")
            .withArguments(listOf(IntegerArgument("id"), IntegerArgument("amount")))
            .withOptionalArguments(StringArgument("options"))
            .executesPlayer(PlayerCommandExecutor { p, a ->
                ItemBase.give(p, a[0] as Int, a[1] as Int, *((a.getOrDefault(2, "") as String).split(" ")).toTypedArray())
            }).register()
    }
}