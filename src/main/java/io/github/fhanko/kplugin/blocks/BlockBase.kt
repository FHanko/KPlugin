package io.github.fhanko.kplugin.blocks

import com.jeff_media.customblockdata.CustomBlockData
import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.blocks.handler.PlaceHandler
import io.github.fhanko.kplugin.items.ItemArgument
import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.util.copyPdc
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

abstract class BlockBase(id: Int, material: Material, name: Component, description: List<Component> = listOf())
    : ItemBase(id, material, name, description), PlaceHandler {
    constructor(id: Int, material: Material, name: String): this(id, material, Component.text(name))

    companion object {
        private fun getBlockPdc(block: Block): PersistentDataContainer = CustomBlockData(block, KPlugin.instance)

        /**
         * Marks [key] of [PersistentDataContainer] from the supplied [block] with [value] of type [type].
         */
        fun <T, Z : Any> markBlock(block: Block, key: NamespacedKey, type: PersistentDataType<T, Z>, value: Z) {
            val blockData = CustomBlockData(block, KPlugin.instance)
            blockData.set(key, type, value)
        }

        /**
         * Removes [key] from [PersistentDataContainer] of supplied [block].
         */
        fun unmarkBlock(block: Block, key: NamespacedKey) {
            val blockData = CustomBlockData(block, KPlugin.instance)
            blockData.remove(key)
        }

        /**
         * Clears [PersistentDataContainer] of supplied [block].
         */
        fun clearBlock(block: Block) {
            val blockData = CustomBlockData(block, KPlugin.instance)
            blockData.clear()
        }

        /**
         * Returns value of type [type] associated with [key] from [PersistentDataContainer] of [block].
         */
        fun <T, Z> readBlock(block: Block?, key: NamespacedKey, type: PersistentDataType<T, Z>): Z? {
            block ?: return null
            @Suppress("Unchecked_Cast") return getBlockPdc(block).get(key, type) as Z
        }

        /**
         * Returns [BlockBase] that is associated with given [Block] [block].
         */
        fun get(block: Block?): BlockBase? {
            block ?: return null
            val blockId = readBlock(block, KEY, PersistentDataType.INTEGER) ?: return null
            return if (itemList.contains(blockId)) itemList[blockId] as BlockBase else null
        }
    }

    enum class DropBehaviour { None, Material, Block }
    /**
     * Determines what is dropped when breaking this block.
     */
    open fun dropBehaviour() = DropBehaviour.Block

    /**
     * Places and returns an instance of this at [location].
     */
    open fun placeInstance(location: Location, vararg args: ItemArgument): Block {
        val block = location.world.getBlockAt(location)
        clearBlock(block)
        block.blockData = Bukkit.createBlockData(material)
        val blockData = CustomBlockData(block, KPlugin.instance)
        copyPdc(instance(1, *args).itemMeta.persistentDataContainer, blockData)
        return block
    }

    private fun compareBlockId(other: Block?): Boolean {
        other ?: return false
        val blockData = CustomBlockData(other, KPlugin.instance)
        return blockData.get(KEY, PersistentDataType.INTEGER) == id
    }
}