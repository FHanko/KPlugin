package io.github.fhanko.kplugin.entity

import io.github.fhanko.kplugin.util.Schedulable
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

abstract class Projectile(val display: MaterialDisplay, val velocity: Vector): Schedulable {
    private val entity = display.display
    private val world = entity.location.world
    private var liveTicks = 0

    fun shoot(tickInterval: Long = 1) {
        entity.velocity = velocity
        scheduleRepeat(entity.uniqueId.toString(), tickInterval, ::tick, tickInterval)
    }

    private fun tick(params: List<Any>) {
        val hit = world.getNearbyEntities(entity.boundingBox)
        if (hit.isNotEmpty()) {
            hitEntity(hit.first())
            remove()
            return
        }
        val block = world.getBlockAt(entity.location)
        if (!block.isPassable) {
            hitBlock(block)
            remove()
            return
        }
        liveTicks += params[0] as Int
        fly(liveTicks)
    }

    open fun fly(liveTicks: Int) { }

    open fun hitBlock(block: Block) { }

    open fun hitEntity(entity: Entity) { }

    protected fun remove() {
        scheduleCancel(entity.uniqueId.toString())
        entity.remove()
    }
}