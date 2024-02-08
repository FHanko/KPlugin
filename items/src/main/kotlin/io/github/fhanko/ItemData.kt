package io.github.fhanko

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

/**
 * Wrapper for [ItemStack] [PersistentDataContainer].
 */
open class ItemData<T : Any>(private val type: PersistentDataType<out Any, T>, private val name: String) {
    protected val key = NamespacedKey(PluginInstance.instance, name)

    /**
     * Sets instances [PersistentDataContainer] data represented by this [ItemData].
     */
    fun set(instance: ItemStack?, value: T) {
        instance?.editMeta { it.persistentDataContainer.set(key, type, value) }
    }

    /**
     * Gets instances [PersistentDataContainer] data represented by this [ItemData].
     */
    fun get(instance: ItemStack?): T? = ItemBase.readItem(instance, key, type)

    /**
     * Removes data represented by this [ItemData] from instances [PersistentDataContainer].
     */
    fun remove(instance: ItemStack?) {
        instance?.editMeta { it.persistentDataContainer.remove(key) }
    }
}