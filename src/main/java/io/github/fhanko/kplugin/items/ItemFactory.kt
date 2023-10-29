package io.github.fhanko.kplugin.items

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

object ItemFactory {
    fun create(material: Material, name: String, description: List<String> = listOf(), amount: Int = 1, actionID: Int): ItemStack {
        val item = ItemStack(material, amount)
        ItemListener.mapItem(item, actionID);
        val meta = item.itemMeta;
        meta.displayName(Component.text(name))
        val lore: List<Component> = description.map { Component.text(it) }
        meta.lore(lore)
        item.setItemMeta(meta)
        return item
    }
}