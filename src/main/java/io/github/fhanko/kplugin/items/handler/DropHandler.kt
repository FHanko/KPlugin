package io.github.fhanko.kplugin.items.handler

import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent

/**
 * Implementable for subclasses of ItemBase to override drop and pickup functions.
 * Fired when a subclass item is dropped or picked up.
 */
interface DropHandler {
    fun drop(e: PlayerDropItemEvent) { }

    fun pickup(e: EntityPickupItemEvent) { }
}