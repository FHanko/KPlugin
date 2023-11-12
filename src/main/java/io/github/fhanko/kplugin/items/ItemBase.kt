package io.github.fhanko.kplugin.items

import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.util.Initializable
import io.github.fhanko.kplugin.util.mm
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

@Suppress("LeakingThis")
abstract class ItemBase(val id: Int, val material: Material, val name: Component, description: List<Component> = listOf()): Initializable {
    constructor(id: Int, material: Material, name: String, description: List<String> = listOf()):
            this(id, material, Component.text(name), description.map { Component.text(it) })

    companion object {
        val KEY = NamespacedKey("kplugin", "itembase")
        val itemList = mutableMapOf<Int, ItemBase>()

        /**
         * Adds item with id to given Players inventory.
         */
        fun give(player: Player, id: Int, amount: Int = 1, vararg args: String) {
            if (itemList.contains(id))
                itemList[id]!!.give(player, amount, *args)
        }

        /**
         * Marks an item with a PDC ID.
         */
        fun <T, Z : Any> markItem(item: ItemStack, key: NamespacedKey, type: PersistentDataType<T, Z>, value: Z) {
            item.editMeta { it.persistentDataContainer.set(key, type, value) }
        }

        /**
         * Reads a PDC ID from an item.
         */
        fun <T, Z> readItem(item: ItemStack, key: NamespacedKey, type: PersistentDataType<T, Z>): Z? {
            val meta = item.itemMeta ?: return null
            @Suppress("Unchecked_Cast") return meta.persistentDataContainer.get(key, type) as Z
        }

        fun setText(item: ItemStack, name: Component, lore: List<Component>) {
            item.editMeta {
                it.displayName(name)
                it.lore(lore)
            }
        }

        fun get(item: ItemStack?): ItemBase? {
            item ?: return null
            val itemId = readItem(item, KEY, PersistentDataType.INTEGER) ?: return null
            return if (itemList.contains(itemId)) itemList[itemId] else null
        }
    }

    /**
     * Returns an instance of this item's ItemStack.
     */
    open fun instance(amount: Int, vararg args: String): ItemStack = ItemStack(item).apply { this.amount = amount }

    /**
     * Adds this item to given Players inventory.
     */
    fun give(player: Player, amount: Int = 1, vararg args: String) {
        player.inventory.addItem(instance(amount, *args))
    }

    val item: ItemStack = ItemStack(material)

    init {
        setText(item, name, description)
        markItem(item, KEY, PersistentDataType.INTEGER, id)
        itemList[id] = this

        KPlugin.instance.logger.info("Init BaseItem ${mm.serialize(name)}")
    }

    protected fun compareId(other: ItemStack?) =
        other?.itemMeta?.persistentDataContainer?.get(KEY, PersistentDataType.INTEGER) == id
}