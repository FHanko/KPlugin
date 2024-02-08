package io.github.fhanko

import com.jeff_media.customblockdata.CustomBlockData
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import io.github.fhanko.goals.LookAtPlayerGoal
import io.github.fhanko.goals.MoveToTargetGoal
import io.github.fhanko.zones.ZoneHeal
import io.github.fhanko.zones.ZoneWater
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin

@Suppress("Unused")
class ExamplePlugin : JavaPlugin() {
    override fun onEnable() {
        ConfigurationSerialization.registerClass(MoveToTargetGoal::class.java, "MoveToTargetGoal")
        ConfigurationSerialization.registerClass(LookAtPlayerGoal::class.java, "LookAtPlayerGoal")

        ConfigurationSerialization.registerClass(ZoneHeal::class.java, "ZoneHeal")
        ConfigurationSerialization.registerClass(ZoneWater::class.java, "ZoneWater")


        PluginInstance.initialize(this)
        Init.initialize("io.github.fhanko")
        CustomBlockData.registerListener(this)
        HibernateUtil.createSessionFactory("PUnit")
        CommandAPI.onEnable()

        Commands.registerGive()
        Commands.registerBal()

        ZoneChunkMap.load()
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
