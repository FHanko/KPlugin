package io.github.fhanko.kplugin.items

import org.bukkit.Material
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack

/**
 * Implementable for subclasses of ItemBase to set crafting behaviour.
 */
interface ItemCraftable {
    fun craft(e: PrepareItemCraftEvent) { }
}

/**
 * Implementable for subclasses of ItemBase to override the output of crafting with that item to nothing.
 */
interface ItemDisableCrafting: ItemCraftable {
    override fun craft(e: PrepareItemCraftEvent) {
        e.inventory.result = ItemStack(Material.AIR)
    }
}