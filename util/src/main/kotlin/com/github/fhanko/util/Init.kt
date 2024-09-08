package com.github.fhanko.util

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
            dbg(it.name)
            it.kotlin.objectInstance
            if (it.kotlin.objectInstance is Listener) {
                dbg("Register $it")
                val listener = it.kotlin.objectInstance as Listener
                Bukkit.getPluginManager().registerEvents(listener, PluginInstance.instance)
            }
        }
    }
}