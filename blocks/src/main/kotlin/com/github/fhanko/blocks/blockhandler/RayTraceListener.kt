package com.github.fhanko.blocks.blockhandler

import com.github.fhanko.blocks.BlockBase
import com.github.fhanko.util.Initializable
import com.github.fhanko.items.itemhandler.Cooldownable
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

/**
 * Returns the maximum distance at which any block can be ray traced.
 */
const val MAX_TRACE_DISTANCE = 10.0
interface RayTraceHandler {
    /**
     * Returns the maximum distance at which this block can be ray traced.
     */
    fun traceDistance() = 3.0

    /**
     * Called when a player ray traces this block.
     */
    fun trace(e: PlayerInteractEvent) { }
}

object RayTraceTracker: Cooldownable {
    override fun getCooldown(): Long = 300L
    override fun cooldownMessage(cooldown: Long): Component = Component.text("")
}

object RayTraceListener: Listener, Initializable {
    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if (RayTraceTracker.useCooldown(e.player)) {
            val b = e.player.rayTraceBlocks(MAX_TRACE_DISTANCE)?.hitBlock ?: return
            val base = BlockBase.get(b)
            if (base is RayTraceHandler && e.player.location.distance(b.location) <= base.traceDistance()) base.trace(e)
        }
    }
}