package com.github.fhanko.items

import com.github.fhanko.util.Initializable
import com.github.fhanko.util.PluginInstance
import com.github.fhanko.util.dbg
import com.github.fhanko.util.mm
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

abstract class ItemBase(val id: Int, val material: Material, val name: Component, val description: List<Component> = listOf()):
    Initializable {
    constructor(id: Int, material: Material, name: String, description: List<String> = listOf()):
            this(id, material, Component.text(name), description.map { Component.text(it) })

    companion object {
        @JvmStatic protected val idKey = NamespacedKey(PluginInstance.instance, "Id")
        val itemMap = mutableMapOf<Int, ItemBase>()

        /**
         * Adds [instance] of [ItemBase] with given [id] to [player]s inventory.
         */
        fun give(player: Player, id: Int, amount: Int = 1, vararg args: ItemArgument) {
            if (itemMap.contains(id))
                itemMap[id]!!.give(player, amount, *args)
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
            return itemMap.getOrDefault(itemId, null)
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
        itemMap[id] = this

        dbg("Initialize Item ${mm.serialize(name)}")
    }
}

open class ItemArgument(val string: String)
fun String.toItemArg() = ItemArgument(this)

class FloatArgument(val float: Float): ItemArgument(float.toString())
fun ItemArgument?.toFloat(default: Float) = FloatArgument(this?.string?.toFloatOrNull() ?: default)

class IntArgument(val int: Int): ItemArgument(int.toString())
fun ItemArgument?.toInt(default: Int) = IntArgument(this?.string?.toIntOrNull() ?: default)