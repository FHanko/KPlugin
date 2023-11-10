package io.github.fhanko.kplugin.util

import io.github.fhanko.kplugin.KPlugin
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.reflections.Reflections

interface Initializable

/**
 * Initializes objects (avoids lazy initialization) for ItemBase and Persistable subtypes
 */
object Init {
    init {
        val reflections = Reflections("io.github.fhanko")
        reflections.getSubTypesOf(Initializable::class.java).forEach {
            val inst = it.kotlin.objectInstance

            if (inst is Listener) Bukkit.getPluginManager().registerEvents(inst, KPlugin.instance)
        }
    }
}