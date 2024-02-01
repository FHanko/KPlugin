package io.github.fhanko

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.reflections.Reflections

interface Initializable

/**
 * Initializes objects (avoids lazy initialization) for [Initializable] subtype objects.
 *
 * Do not use this in production as the reflection libraries are quite heavy.
 */
object Init {
    fun initialize(prefix: String) {
        val reflections = Reflections(prefix)
        reflections.getSubTypesOf(Initializable::class.java).forEach {
            it.kotlin.objectInstance
        }

        reflections.getSubTypesOf(Listener::class.java).forEach {
            if (it.kotlin.objectInstance != null)
            Bukkit.getPluginManager().registerEvents(it.kotlin.objectInstance!!, PluginInstance.instance)
        }
    }
}