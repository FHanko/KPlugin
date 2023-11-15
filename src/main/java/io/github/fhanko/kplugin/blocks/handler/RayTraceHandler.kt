package io.github.fhanko.kplugin.blocks.handler

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