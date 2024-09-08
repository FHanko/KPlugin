package com.github.fhanko.exampleplugin

import com.jeff_media.customblockdata.CustomBlockData
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import com.github.fhanko.entity.goals.LookAtPlayerGoal
import com.github.fhanko.entity.goals.MoveToTargetGoal
import com.github.fhanko.exampleplugin.zones.ZoneHeal
import com.github.fhanko.exampleplugin.zones.ZoneWater
import com.github.fhanko.persistence.HibernateUtil
import com.github.fhanko.util.Init
import com.github.fhanko.util.PluginInstance
import com.github.fhanko.zones.ZoneChunkMap
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin

class ExamplePlugin : JavaPlugin() {
    override fun onEnable() {
        ConfigurationSerialization.registerClass(MoveToTargetGoal::class.java, "MoveToTargetGoal")
        ConfigurationSerialization.registerClass(LookAtPlayerGoal::class.java, "LookAtPlayerGoal")

        ConfigurationSerialization.registerClass(ZoneHeal::class.java, "ZoneHeal")
        ConfigurationSerialization.registerClass(ZoneWater::class.java, "ZoneWater")


        PluginInstance.initialize(this)
        Init.initialize("com/github/fhanko/zones")
        CustomBlockData.registerListener(this)
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
