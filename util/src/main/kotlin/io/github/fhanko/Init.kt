package io.github.fhanko

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.reflections.Reflections

interface Initializable

/**
 * Initializes objects (avoids lazy initialization) for [Initializable] subtype objects.
 */
object Init {
    internal fun initialize(prefix: String) {
        val reflections = Reflections(prefix)
        reflections.getSubTypesOf(Initializable::class.java).forEach {
            it.kotlin.objectInstance
        }

        val listeners = Reflections("io.github.fhanko")
        listeners.getSubTypesOf(Listener::class.java).forEach {
            Bukkit.getPluginManager().registerEvents(it.kotlin.objectInstance!!, PluginInstance.instance)
        }
    }
}