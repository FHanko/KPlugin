package io.github.fhanko.kplugin.handler

import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent

/**
 * Implementable for subclasses of ItemBase to override drop and pickup functions.
 */
interface DropHandler {
    fun drop(e: PlayerDropItemEvent) { }

    fun pickup(e: EntityPickupItemEvent) { }
}