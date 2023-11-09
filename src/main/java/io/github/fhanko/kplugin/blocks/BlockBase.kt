package io.github.fhanko.kplugin.blocks

import com.jeff_media.customblockdata.CustomBlockData
import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.util.copyPdc
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

abstract class BlockBase(id: Int, material: Material, name: Component, description: List<Component> = listOf())
    : ItemBase(id, material, name, description), Listener, BlockComparable {

    constructor(id: Int, material: Material, name: String): this(id, material, Component.text(name))

    companion object {
        private fun getBlockPdc(block: Block): PersistentDataContainer = CustomBlockData(block, KPlugin.instance)

        fun <T, Z> readBlock(block: Block, key: NamespacedKey, type: PersistentDataType<T, Z>): Z {
            @Suppress("Unchecked_Cast") return getBlockPdc(block).get(key, type) as Z
        }

        fun get(block: Block?): BlockBase? {
            block ?: return null
            val blockId = readBlock(block, KEY, PersistentDataType.INTEGER) ?: return null
            return if (itemList.contains(blockId)) itemList[blockId] as BlockBase else null
        }
    }

    open fun place(e: BlockPlaceEvent) { }
    @EventHandler
    fun onPlace(e: BlockPlaceEvent) {
        if (compareId(e.itemInHand)) {
            place(e)
            val blockData = CustomBlockData(e.blockPlaced, KPlugin.instance)
            copyPdc(e.itemInHand.itemMeta.persistentDataContainer, blockData)
        }
    }

    open fun destroy(e: BlockBreakEvent) { }
    @EventHandler
    fun onBreak(e: BlockBreakEvent) {
        if (compareBlockId(e.block)) {
            destroy(e)
            e.isDropItems = false
            val i = ItemStack(item)
            i.editMeta { copyPdc(CustomBlockData(e.block, KPlugin.instance), it.persistentDataContainer) }
            i.amount = 1
            e.block.world.dropItemNaturally(e.block.location, i)
        }
    }

    override fun compareBlockId(other: Block?): Boolean {
        other ?: return false
        val blockData = CustomBlockData(other, KPlugin.instance)
        return blockData.get(KEY, PersistentDataType.INTEGER) == id
    }
}