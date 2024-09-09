package com.github.fhanko.entity.entityhandler

import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot

/**
 * Implementable for subclasses of EntityBase to override on interact function.
 */
interface InteractHandler {
    fun rightClick(e: PlayerInteractEntityEvent) { }

    fun onInteract(e: PlayerInteractEntityEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        rightClick(e)
    }
}