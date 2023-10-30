package io.github.fhanko.kplugin.items

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
abstract class ItemBase(private var id: Int,material: Material, name: String, description: List<String> = listOf()): Listener, ItemComparable {
    companion object {
        private val KEY = NamespacedKey.fromString("kplugin_item")!!

        /**
         * Adds given item to given Player.
         */
        fun give(player: Player, item: ItemBase, amount: Int = 1) {
            val i = item.item
            i.amount = amount
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
        Bukkit.getPluginManager().registerEvents(this, getServer().pluginManager.getPlugin("KPlugin")!!)
    }

    override fun compareId(other: ItemStack?) =
        other?.itemMeta?.persistentDataContainer?.get(KEY, PersistentDataType.INTEGER) == id
}