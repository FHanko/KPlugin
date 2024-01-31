package io.github.fhanko

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

@Suppress("LeakingThis")
abstract class ItemBase(val id: Int, val material: Material, val name: Component, val description: List<Component> = listOf()): Initializable {
    constructor(id: Int, material: Material, name: String, description: List<String> = listOf()):
            this(id, material, Component.text(name), description.map { Component.text(it) })

    companion object {
        @JvmStatic protected val idKey = NamespacedKey(PluginInstance.instance, "Id")
        val itemList = mutableMapOf<Int, ItemBase>()

        /**
         * Adds [instance] of [ItemBase] with given [id] to [player]s inventory.
         */
        fun give(player: Player, id: Int, amount: Int = 1, vararg args: ItemArgument) {
            if (itemList.contains(id))
                itemList[id]!!.give(player, amount, *args)
        }

        /**
         * Updates the [name] and [lore] of [item].
         */
        fun setText(item: ItemStack, name: Component, lore: List<Component>) {
            item.editMeta {
                it.displayName(name)
                it.lore(lore)
            }
        }

        /**
         * Reads an ID from the [PersistentDataContainer] of [item].
         */
        fun <T, Z> readItem(item: ItemStack?, key: NamespacedKey, type: PersistentDataType<T, Z>): Z? {
            val meta = item?.itemMeta ?: return null
            @Suppress("Unchecked_Cast") return meta.persistentDataContainer.get(key, type) as Z
        }

        /**
         * Returns [ItemBase] that is associated with given [ItemStack] [item].
         */
        fun get(item: ItemStack?): ItemBase? {
            item ?: return null
            val itemId = readItem(item, idKey, PersistentDataType.INTEGER) ?: return null
            return itemList.getOrDefault(itemId, null)
        }
    }

    /**
     * Returns a configured [ItemStack] of this [item] with given [amount].
     */
    open fun instance(amount: Int, vararg args: ItemArgument): ItemStack = ItemStack(item).apply { this.amount = amount }

    /**
     * Adds an [instance] of this item to given [player]s inventory with [args] parsed from strings.
     */
    fun give(player: Player, amount: Int = 1, vararg args: ItemArgument) {
        player.inventory.addItem(instance(amount, *args))
    }

    fun setCustomModelData(modelData: Int) = item.editMeta { it.setCustomModelData(modelData) }

    val item: ItemStack = ItemStack(material)
    protected val key = ItemData(PersistentDataType.INTEGER, "Id")

    init {
        setText(item, name, description)
        key.set(item, id)
        itemList[id] = this

        Bukkit.getLogger().info("Init BaseItem ${mm.serialize(name)}")
    }
}

data class ItemArgument(val string: String, val integer: Int?, val float: Float?) {
    constructor(string: String): this(string, string.toIntOrNull(), string.toFloatOrNull())
    constructor(integer: Int): this(integer.toString(), integer, integer.toFloat())
    constructor(float: Float): this(float.toString(), float.toInt(), float)
}