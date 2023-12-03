package io.github.fhanko.kplugin.entity

import io.github.fhanko.kplugin.util.Schedulable
import org.bukkit.FluidCollisionMode
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

abstract class Projectile(val source: Entity?, val projectile: Entity, var velocity: Vector, val mass: Float = 1f): Schedulable {
    private val world = projectile.location.world
    private var liveTicks = 0L

    fun shoot(tickInterval: Long = 1) {
        scheduleRepeat(projectile.uniqueId.toString(), tickInterval, ::tick, tickInterval)
    }

    private fun tick(params: List<Any>) {
        val delta = params[0] as Long
        move(delta)
        val hit = world.rayTrace(
            projectile.location.subtract(velocity), velocity, velocity.length(), FluidCollisionMode.NEVER, true, 0.01
        ) { e -> e.uniqueId != source?.uniqueId && e.uniqueId != projectile.uniqueId }
        if (hit?.hitEntity != null) {
            hitEntity(hit.hitEntity!!, hit.hitPosition)
            disable()
            return
        }
        if (hit?.hitBlock != null) {
            hitBlock(hit.hitBlock!!, hit.hitPosition)
            disable()
            return
        }
        liveTicks += delta
        tick(liveTicks)
    }

    open fun move(delta: Long) {
        if (mass > 0) {
            velocity.multiply(1f - (delta * 0.008f * mass))
            velocity.add(Vector(0f, -0.02f * delta * mass, 0f))
        }
        projectile.teleport(projectile.location.add(velocity))
    }

    open fun tick(liveTicks: Long) { }

    open fun hitBlock(block: Block, position: Vector) { }

    open fun hitEntity(entity: Entity, position: Vector) { }

    protected fun disable() {
        scheduleCancel(projectile.uniqueId.toString())
    }
}