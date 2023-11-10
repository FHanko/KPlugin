package io.github.fhanko.kplugin.items.handler

import org.bukkit.Material
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack

/**
 * Implementable for subclasses of ItemBase to set crafting behaviour.
 */
interface CraftHandler {
    fun craft(e: PrepareItemCraftEvent) { }
}

/**
 * Implementable for subclasses of ItemBase to override the output of crafting with that item to nothing.
 */
interface DisableCraftHandler: CraftHandler {
    override fun craft(e: PrepareItemCraftEvent) {
        e.inventory.result = ItemStack(Material.AIR)
    }
}