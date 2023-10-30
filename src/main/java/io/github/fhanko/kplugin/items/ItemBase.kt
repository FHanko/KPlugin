package io.github.fhanko.kplugin.items

import io.github.fhanko.kplugin.KPlugin
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Bukkit.getServer
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

@Suppress("LeakingThis")
abstract class ItemBase(private val id: Int,material: Material, name: String, description: List<String> = listOf()): Listener, ItemComparable, ItemDisableCrafting {
    companion object {
        val KEY = NamespacedKey.fromString("kplugin_item")!!
        val itemList = mutableListOf<ItemBase>()

        /**
         * Adds given item to given Player.
         */
        fun give(player: Player, item: ItemBase, amount: Int = 1) {
            val i = item.item
            i.amount = amount
            player.inventory.addItem(i)
        }


        fun give(player: Player, id: Int, amount: Int = 1) {
            val i = itemList.find { it.id == id }?.item ?: return
            player.inventory.addItem(i)
        }

        /**
         * Marks an Item with a PDC ID.
         */
        fun markItem(item: ItemStack, id: Int) {
            val meta = item.itemMeta
            meta.persistentDataContainer.set(KEY, PersistentDataType.INTEGER, id)
            item.setItemMeta(meta)
        }
    }

    protected var item: ItemStack

    init {
        item = ItemStack(material)
        val meta = item.itemMeta
        meta.displayName(Component.text(name))
        meta.lore(description.map { Component.text(it) })
        item.setItemMeta(meta)
        markItem(item, id)
        Bukkit.getPluginManager().registerEvents(this, KPlugin.instance)
        itemList.add(this);
    }

    override fun compareId(other: ItemStack?) =
        other?.itemMeta?.persistentDataContainer?.get(KEY, PersistentDataType.INTEGER) == id
}