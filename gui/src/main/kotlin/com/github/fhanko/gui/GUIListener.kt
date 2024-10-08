package com.github.fhanko.gui

import com.github.fhanko.util.Initializable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent

/**
 * Event listener for GUI handler
 *
 * Addresses event efficiency concerns by handling them once and firing handler functions
 * instead of firing all events on all inventories.
 */
object GUIListener: Listener, Initializable {
    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        val holder = e.inventory.holder
        if (holder is InventoryHandler)
            holder.inventoryClose(e)
    }

    @EventHandler
    fun onInventoryOpen(e: InventoryOpenEvent) {
        val holder = e.inventory.holder
        if (holder is InventoryHandler)
            holder.inventoryOpen(e)
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val holder = e.inventory.holder
        if (holder is InventoryHandler)
            holder.inventoryClick(e)
    }
}