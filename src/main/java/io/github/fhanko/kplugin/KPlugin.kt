package io.github.fhanko.kplugin

import com.jeff_media.customblockdata.CustomBlockData
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import io.github.fhanko.kplugin.blocks.handler.BlockListener
import io.github.fhanko.kplugin.display.DisplayListener
import io.github.fhanko.kplugin.gui.handler.InventoryListener
import io.github.fhanko.kplugin.items.handler.ItemListener
import io.github.fhanko.kplugin.util.HibernateUtil
import io.github.fhanko.kplugin.util.Init
import io.github.fhanko.kplugin.zones.handler.ZoneListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

@Suppress("Unused")
class KPlugin : JavaPlugin() {
    companion object {
        lateinit var instance: JavaPlugin

        fun initialize(plugin: JavaPlugin) {
            instance = plugin
            Bukkit.getPluginManager().registerEvents(DefaultListener, instance)
            Bukkit.getPluginManager().registerEvents(ZoneListener, instance)
            Bukkit.getPluginManager().registerEvents(BlockListener, instance)
            Bukkit.getPluginManager().registerEvents(ItemListener, instance)
            Bukkit.getPluginManager().registerEvents(DisplayListener, instance)
            Bukkit.getPluginManager().registerEvents(InventoryListener, instance)
            CustomBlockData.registerListener(instance)
        }
    }

    override fun onEnable() {
        initialize(this)
        HibernateUtil.createSessionFactory("PUnit")
        CommandAPI.onEnable()
        Init.initialize("io.github.fhanko")

        Commands.registerGive()
        Commands.registerBal()
    }

    override fun onLoad()
    {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).verboseOutput(true))
    }

    override fun onDisable() {
        CommandAPI.onDisable()
        HibernateUtil.shutdown()
    }
}
