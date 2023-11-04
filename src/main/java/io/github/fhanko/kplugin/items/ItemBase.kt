package io.github.fhanko.kplugin.items

import io.github.fhanko.kplugin.KPlugin
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

@Suppress("LeakingThis")
abstract class ItemBase(val id: Int, val material: Material, val name: Component, val description: List<Component> = listOf()):
    Listener, ItemComparable {
    constructor(id: Int, material: Material, name: String, description: List<String> = listOf()):
            this(id, material, Component.text(name), description.map { Component.text(it) })

    companion object {
        val KEY = NamespacedKey("kplugin", "itembase")
        val itemList = mutableListOf<ItemBase>()

        /**
         * Adds item with id to given Players inventory.
         */
        fun give(player: Player, id: Int, amount: Int = 1, vararg args: String) {
            val i = itemList.find { it.id == id } ?: return
            i.give(player, amount, *args)
        }

        /**
         * Marks an Item with a PDC ID.
         */
        fun <T, Z : Any> markItem(item: ItemStack, key: NamespacedKey, type: PersistentDataType<T, Z>, value: Z) {
            val meta = item.itemMeta ?: return
            meta.persistentDataContainer.set(key, type, value)
            item.setItemMeta(meta)
        }

        fun <T, Z> readItem(item: ItemStack, key: NamespacedKey, type: PersistentDataType<T, Z>): Z? {
            val meta = item.itemMeta ?: return null
            @Suppress("Unchecked_Cast") return meta.persistentDataContainer.get(key, type) as Z
        }

        fun isMarked(item: ItemStack?) : Boolean {
            item ?: return false
            item.itemMeta ?: return false
            return readItem(item, KEY, PersistentDataType.INTEGER) != null
        }

        fun setText(item: ItemStack, name: Component, lore: List<Component>) {
            val meta = item.itemMeta
            meta.displayName(name)
            meta.lore(lore)
            item.setItemMeta(meta)
        }
    }

    /**
     * Adds this item to given Players inventory.
     */
    open fun give(player: Player, amount: Int = 1, vararg args: String) {
        val i = item
        i.amount = amount
        player.inventory.addItem(i)
    }

    protected var item: ItemStack

    init {
        item = ItemStack(material)
        setText(item, name, description)
        markItem(item, KEY, PersistentDataType.INTEGER, id)
        Bukkit.getPluginManager().registerEvents(this, KPlugin.instance)
        itemList.add(this)

        KPlugin.instance.logger.info("Init BaseItem $name")
    }

    override fun compareId(other: ItemStack?) =
        other?.itemMeta?.persistentDataContainer?.get(KEY, PersistentDataType.INTEGER) == id
}