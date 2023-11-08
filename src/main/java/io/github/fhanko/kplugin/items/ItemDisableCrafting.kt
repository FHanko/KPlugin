package io.github.fhanko.kplugin.items

import io.github.fhanko.kplugin.KPluginPrepareItemCraftEvent
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

/**
 * Implementable for subclasses of ItemBase to override the output of crafting with that item to nothing.
 */
interface ItemDisableCrafting: Listener, ItemComparable {
    @EventHandler
    fun onCraft(kpe: KPluginPrepareItemCraftEvent) {
        val e = kpe.baseEvent
        if (e.inventory.any { compareId(it) }) e.inventory.result = ItemStack(Material.AIR)
    }
}