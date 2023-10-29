package io.github.fhanko.kplugin

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import io.github.fhanko.kplugin.items.ItemListener
import io.github.fhanko.kplugin.zones.ZoneCommands
import io.github.fhanko.kplugin.zones.ZoneListener
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin


class KPlugin : JavaPlugin(), Listener {
    override fun onEnable() {
        CommandAPI.onEnable()
        Bukkit.getPluginManager().registerEvents(ZoneListener(), this)
        Bukkit.getPluginManager().registerEvents(ItemListener(), this)
    }

    override fun onLoad()
    {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).verboseOutput(true))
        ZoneCommands.register()
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }
}
