package io.github.fhanko.kplugin.util

import org.reflections.Reflections

interface Initializable

/**
 * Initializes objects (avoids lazy initialization) for ItemBase and Persistable subtypes
 */
object Init {
    init {
        val reflections = Reflections("io.github.fhanko")
        reflections.getSubTypesOf(Initializable::class.java).forEach {
            it.kotlin.objectInstance
        }
    }
}