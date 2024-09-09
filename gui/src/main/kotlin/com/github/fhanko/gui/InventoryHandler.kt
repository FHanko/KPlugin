package com.github.fhanko.gui

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.InventoryHolder

/**
 * Fired when the inventory of the holder is opened, closed or click in.
 */
interface InventoryHandler: InventoryHolder {
    fun inventoryClose(e: InventoryCloseEvent) { }
    fun inventoryOpen(e: InventoryOpenEvent) { }
    fun inventoryClick(e: InventoryClickEvent) { }
}