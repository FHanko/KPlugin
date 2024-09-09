package com.github.fhanko.core

import com.github.fhanko.blocks.blockhandler.BlockListener
import com.github.fhanko.entity.entityhandler.EntityListener
import com.github.fhanko.entity.goals.LookAtPlayerGoal
import com.github.fhanko.entity.goals.MoveToTargetGoal
import com.github.fhanko.items.itemhandler.ItemListener
import com.github.fhanko.util.PluginInstance
import com.github.fhanko.gui.GUIListener
import com.jeff_media.customblockdata.CustomBlockData
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin

object KPluginCore {
    fun init(plugin: JavaPlugin) {
        plugin.apply {
            PluginInstance.initialize(this)

            ConfigurationSerialization.registerClass(MoveToTargetGoal::class.java, "MoveToTargetGoal")
            ConfigurationSerialization.registerClass(LookAtPlayerGoal::class.java, "LookAtPlayerGoal")

            server.pluginManager.registerEvents(ItemListener, this)
            server.pluginManager.registerEvents(BlockListener, this)
            server.pluginManager.registerEvents(EntityListener, this)
            server.pluginManager.registerEvents(GUIListener, this)

            CustomBlockData.registerListener(this)
        }
    }
}