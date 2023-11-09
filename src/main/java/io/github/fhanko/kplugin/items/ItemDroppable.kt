package io.github.fhanko.kplugin.items

import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent

/**
 * Implementable for subclasses of ItemBase to override drop and pickup functions.
 */
interface ItemDroppable: Listener, ItemComparable {
    fun drop(e: PlayerDropItemEvent) { }

    fun pickup(e: EntityPickupItemEvent) { }
}