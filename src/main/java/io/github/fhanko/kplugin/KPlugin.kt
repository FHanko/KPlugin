package io.github.fhanko.kplugin

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import io.github.fhanko.kplugin.zones.ZoneCommands
import io.github.fhanko.kplugin.zones.ZoneListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


class KPlugin : JavaPlugin() {
    override fun onEnable() {
        CommandAPI.onEnable()
        Bukkit.getPluginManager().registerEvents(ZoneListener(), this)
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
