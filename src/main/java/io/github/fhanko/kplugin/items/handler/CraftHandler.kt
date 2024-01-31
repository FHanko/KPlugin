package io.github.fhanko.kplugin.items.handler

import org.bukkit.Material
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack

/**
 * Implementable for subclasses of ItemBase to set crafting behaviour.
 */
interface CraftHandler {
    fun craftResult(e: PrepareItemCraftEvent) { }
    fun craftComponent(e: PrepareItemCraftEvent) { }
}

/**
 * Implementable for subclasses of ItemBase to override the output of crafting with that item to nothing.
 */
interface DisableCraftHandler: CraftHandler {
    override fun craftComponent(e: PrepareItemCraftEvent) {
        e.inventory.result = ItemStack(Material.AIR)
    }
}

/**
 * Implementable for subclasses of ItemBase to override the output of crafting with that item to nothing
 * if viewers do not have a certain permission.
 */
interface CraftPermissionHandler: CraftHandler {
    fun craftPermission() = ""
    override fun craftResult(e: PrepareItemCraftEvent) {
        if (e.viewers.any { !it.hasPermission(craftPermission()) }) e.inventory.result = ItemStack(Material.AIR)
    }
}