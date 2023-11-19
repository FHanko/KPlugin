package io.github.fhanko.kplugin.items

import com.jeff_media.customblockdata.CustomBlockData
import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.blocks.BlockBase
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

data class ItemData<T : Any>(val type: PersistentDataType<out Any, T>, val name: String) {
    private val key = NamespacedKey(KPlugin.instance, name)

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

    /**
     * [set] with [Block] [instance].
     */
    fun setBlock(instance: Block?, value: T) {
        instance ?: return
        val blockData = CustomBlockData(instance, KPlugin.instance)
        blockData.set(key, type, value)
    }

    /**
     * [get] with [Block] [instance].
     */
    fun getBlock(instance: Block?) = BlockBase.readBlock(instance, key, type)

    /**
     * [remove] with [Block] [instance].
     */
    fun removeBlock(instance: Block?) {
        instance ?: return
        val blockData = CustomBlockData(instance, KPlugin.instance)
        blockData.remove(key)
    }
}