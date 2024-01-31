package io.github.fhanko

import com.jeff_media.customblockdata.CustomBlockData
import org.bukkit.block.Block
import org.bukkit.persistence.PersistentDataType

class BlockData<T : Any>(private val type: PersistentDataType<out Any, T>, private val name: String) : ItemData<T>(type, name) {
    /**
     * [set] with [Block] [instance].
     */
    fun setBlock(instance: Block?, value: T) {
        instance ?: return
        val blockData = CustomBlockData(instance, PluginInstance.instance)
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
        val blockData = CustomBlockData(instance, PluginInstance.instance)
        blockData.remove(key)
    }
}
