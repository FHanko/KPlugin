package io.github.fhanko.kplugin

import io.github.fhanko.kplugin.zones.ZoneListener
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class KPlugin : JavaPlugin(), Listener {
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(ZoneListener(), this)
    }
}
