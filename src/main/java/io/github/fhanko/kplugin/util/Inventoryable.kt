package io.github.fhanko.kplugin.util

import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.InventoryHolder

interface Inventoryable: InventoryHolder {
    fun inventoryClose(e: InventoryCloseEvent) { }
    fun inventoryOpen(e: InventoryOpenEvent) { }
    fun inventoryInteract(e: InventoryInteractEvent) { }

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        if (e.inventory.holder == this)
            inventoryClose(e)
    }

    @EventHandler
    fun onInventoryOpen(e: InventoryOpenEvent) {
        if (e.inventory.holder == this)
            inventoryOpen(e)
    }

    @EventHandler
    fun onInventoryInteract(e: InventoryInteractEvent) {
        if (e.inventory.holder == this)
            inventoryInteract(e)
    }
}