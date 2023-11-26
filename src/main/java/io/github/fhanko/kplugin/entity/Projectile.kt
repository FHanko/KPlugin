package io.github.fhanko.kplugin.entity

import io.github.fhanko.kplugin.util.Schedulable
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

class Projectile(val entity: Entity, val velocity: Vector): Schedulable {
    val location = entity.location

    fun shoot() {
        entity.velocity = velocity
    }

    fun tick() {

    }

    fun remove() {
        entity.remove()

    }
}