package io.github.fhanko.kplugin.items.handler

import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Implementable for subclasses of ItemBase to override item inventory interact functions.
 * Fired when a subclass item is clicked or dropped in an inventory action.
 */
interface SlotHandler {
    fun slotClicked(e: InventoryClickEvent) { }
    fun slotDropped(e: InventoryClickEvent) { }
}