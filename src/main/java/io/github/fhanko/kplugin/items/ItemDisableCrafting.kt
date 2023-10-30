package io.github.fhanko.kplugin.items

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack

interface ItemDisableCrafting: Listener, ItemComparable {
    @EventHandler
    fun onCraft(e: PrepareItemCraftEvent) {
        if (e.inventory.any() { compareId(it) }) e.inventory.result = ItemStack(Material.AIR)
    }
}