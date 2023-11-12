package io.github.fhanko.kplugin.blocks

import com.jeff_media.customblockdata.CustomBlockData
import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.blocks.handler.PlaceHandler
import io.github.fhanko.kplugin.items.ItemBase
import net.kyori.adventure.text.Component
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
         * Marks a block with a PDC ID.
         */
        fun <T, Z : Any> markBlock(block: Block, key: NamespacedKey, type: PersistentDataType<T, Z>, value: Z) {
            val blockData = CustomBlockData(block, KPlugin.instance)
            blockData.set(key, type, value)
        }

        /**
         * Reads a PDC ID from a block.
         */
        fun <T, Z> readBlock(block: Block?, key: NamespacedKey, type: PersistentDataType<T, Z>): Z? {
            block ?: return null
            @Suppress("Unchecked_Cast") return getBlockPdc(block).get(key, type) as Z
        }

        fun get(block: Block?): BlockBase? {
            block ?: return null
            val blockId = readBlock(block, KEY, PersistentDataType.INTEGER) ?: return null
            return if (itemList.contains(blockId)) itemList[blockId] as BlockBase else null
        }
    }

    private fun compareBlockId(other: Block?): Boolean {
        other ?: return false
        val blockData = CustomBlockData(other, KPlugin.instance)
        return blockData.get(KEY, PersistentDataType.INTEGER) == id
    }
}