package io.github.fhanko.kplugin.gui

import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.InventoryHolder

interface Inventoryable: InventoryHolder {
    fun inventoryClose(e: InventoryCloseEvent) { }
    fun inventoryOpen(e: InventoryOpenEvent) { }
    fun inventoryClick(e: InventoryClickEvent) { }
}