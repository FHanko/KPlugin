package io.github.fhanko

import com.jeff_media.customblockdata.CustomBlockData
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import org.bukkit.plugin.java.JavaPlugin

@Suppress("Unused")
class ExamplePlugin : JavaPlugin() {
    override fun onEnable() {
        PluginInstance.initialize(this, "io.github.fhanko")
        CustomBlockData.registerListener(this)
        HibernateUtil.createSessionFactory("PUnit")
        CommandAPI.onEnable()

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
