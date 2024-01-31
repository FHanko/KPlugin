package io.github.fhanko

import org.reflections.Reflections

interface Initializable

/**
 * Initializes objects (avoids lazy initialization) for [Initializable] subtype objects.
 */
object Init {
    fun initialize(prefix: String) {
        val reflections = Reflections(prefix)
        reflections.getSubTypesOf(Initializable::class.java).forEach {
            it.kotlin.objectInstance
        }
    }
}