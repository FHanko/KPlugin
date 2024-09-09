package com.github.fhanko.util

import org.bukkit.plugin.java.JavaPlugin

object PluginInstance {
    lateinit var instance: JavaPlugin

    fun initialize(plugin: JavaPlugin) {
        instance = plugin
    }
}