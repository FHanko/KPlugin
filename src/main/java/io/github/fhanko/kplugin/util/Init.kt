package io.github.fhanko.kplugin.util

import io.github.fhanko.kplugin.items.ItemBase
import org.reflections.Reflections

/**
 * Initializes objects (avoids lazy initialization) for ItemBase and Persistable subtypes
 */
object Init {
    init {
        val reflections = Reflections("io.github.fhanko")
        reflections.getSubTypesOf(ItemBase::class.java).forEach {
            it.kotlin.objectInstance
        }
        reflections.getSubTypesOf(Persistable::class.java).forEach {
            it.kotlin.objectInstance
        }
    }
}