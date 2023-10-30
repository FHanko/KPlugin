package io.github.fhanko.kplugin

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import io.github.fhanko.kplugin.items.ItemInit
import io.github.fhanko.kplugin.zones.ZoneCommands
import io.github.fhanko.kplugin.zones.ZoneListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

@Suppress("Unused")
class KPlugin : JavaPlugin() {
    companion object {
        lateinit var instance: JavaPlugin;
    }

    override fun onEnable() {
        instance = this;
        CommandAPI.onEnable()
        Commands.register()
        Bukkit.getPluginManager().registerEvents(ZoneListener(), this)
        Bukkit.getPluginManager().registerEvents(EventListener(), this)
        ItemInit
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
