package io.github.fhanko.kplugin.items

import org.reflections.Reflections

/**
 * Initializes all ItemBase objects
 */
object ItemInit {
    init {
        val reflections = Reflections("io.github.fhanko")
        val allSubtypes = reflections.getSubTypesOf(ItemBase::class.java)
        allSubtypes.forEach {
            it.kotlin.objectInstance
        }
    }
}