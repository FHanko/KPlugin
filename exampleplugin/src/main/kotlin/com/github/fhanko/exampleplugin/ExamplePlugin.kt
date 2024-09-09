package com.github.fhanko.exampleplugin

import com.github.fhanko.core.KPluginCore
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import com.github.fhanko.exampleplugin.zones.ZoneHeal
import com.github.fhanko.exampleplugin.zones.ZoneWater
import com.github.fhanko.persistence.HibernateUtil
import com.github.fhanko.persistence.PersistenceListener
import com.github.fhanko.util.Init
import com.github.fhanko.zones.ZoneChunkMap
import com.github.fhanko.zones.zonehandler.ZoneListener
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin

class ExamplePlugin : JavaPlugin() {
    override fun onEnable() {
        KPluginCore.init(this)

        ConfigurationSerialization.registerClass(ZoneHeal::class.java, "ZoneHeal")
        ConfigurationSerialization.registerClass(ZoneWater::class.java, "ZoneWater")

        server.pluginManager.registerEvents(ZoneListener, this)
        server.pluginManager.registerEvents(PersistenceListener, this)
        Init.initialize("com.github.fhanko", false)
        HibernateUtil.createSessionFactory("PUnit")
        CommandAPI.onEnable()

        Commands.registerGive()
        Commands.registerBal()

        ZoneChunkMap.load()
    }

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).verboseOutput(true))
    }

    override fun onDisable() {
        CommandAPI.onDisable()
        HibernateUtil.shutdown()
    }
}
