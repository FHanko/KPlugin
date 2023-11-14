package io.github.fhanko.kplugin

import com.jeff_media.customblockdata.CustomBlockData
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import io.github.fhanko.kplugin.util.HibernateUtil
import io.github.fhanko.kplugin.util.Init
import org.bukkit.plugin.java.JavaPlugin

@Suppress("Unused")
class KPlugin : JavaPlugin() {
    companion object {
        lateinit var instance: JavaPlugin

        fun initialize(plugin: JavaPlugin) {
            instance = plugin
            Init.initialize(plugin, "io.github.fhanko.kplugin")
        }
    }

    override fun onEnable() {
        instance = this
        HibernateUtil.createSessionFactory()
        CommandAPI.onEnable()
        CustomBlockData.registerListener(this)
        initialize(this)

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
