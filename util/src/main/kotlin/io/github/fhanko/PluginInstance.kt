package io.github.fhanko

import org.bukkit.plugin.java.JavaPlugin

object PluginInstance {
    lateinit var instance: JavaPlugin

    fun setInstance(plugin: JavaPlugin, groupId: String) {
        instance = plugin
        Init.initialize(groupId)
    }
}