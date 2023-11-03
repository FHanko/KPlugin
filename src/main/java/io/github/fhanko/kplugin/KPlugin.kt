package io.github.fhanko.kplugin

import com.jeff_media.customblockdata.CustomBlockData
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import io.github.fhanko.kplugin.util.HibernateUtil
import io.github.fhanko.kplugin.util.Init
import io.github.fhanko.kplugin.util.PlayerStorage
import io.github.fhanko.kplugin.zones.ZoneCommands
import io.github.fhanko.kplugin.zones.ZonePlayerMap
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

@Suppress("Unused")
class KPlugin : JavaPlugin() {
    companion object {
        lateinit var instance: JavaPlugin
    }

    override fun onEnable() {
        instance = this
        HibernateUtil.createSessionFactory()
        CommandAPI.onEnable()
        Commands.register()
        Bukkit.getPluginManager().registerEvents(ZonePlayerMap(), this)
        Bukkit.getPluginManager().registerEvents(HibernateUtil, this)
        Bukkit.getPluginManager().registerEvents(VanillaEventListener(), this)
        CustomBlockData.registerListener(this)
        PlayerStorage
        Init
    }

    override fun onLoad()
    {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).verboseOutput(true))
        ZoneCommands.register()
    }

    override fun onDisable() {
        CommandAPI.onDisable()
        HibernateUtil.shutdown()
    }
}
