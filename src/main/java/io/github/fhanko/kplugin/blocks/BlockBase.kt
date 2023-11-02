package io.github.fhanko.kplugin.blocks

import com.jeff_media.customblockdata.CustomBlockData
import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.util.copyPdc
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

abstract class BlockBase(private val id: Int,material: Material, name: String, description: List<String> = listOf())
    : ItemBase(id, material, name, description), Listener, BlockComparable {

    companion object {
        private fun getBlockPdc(block: Block): PersistentDataContainer = CustomBlockData(block, KPlugin.instance)

        fun <T, Z> readBlock(block: Block, key: NamespacedKey, type: PersistentDataType<T, Z>): Z {
            @Suppress("Unchecked_Cast") return getBlockPdc(block).get(key, type) as Z
        }
    }

    open fun place(e: BlockPlaceEvent) { }
    @EventHandler
    fun onPlace(e: BlockPlaceEvent) {
        if (compareId(e.itemInHand)) {
            val blockData = CustomBlockData(e.blockPlaced, KPlugin.instance)
            copyPdc(e.itemInHand.itemMeta.persistentDataContainer, blockData)
            place(e)
        }
    }

    open fun destroy(e: BlockBreakEvent) { }
    @EventHandler
    fun onBreak(e: BlockBreakEvent) {
        if (compareBlockId(e.block)) {
            e.isDropItems = false
            val i = ItemStack(item)
            val im = i.itemMeta
            copyPdc(CustomBlockData(e.block, KPlugin.instance), im.persistentDataContainer)
            i.itemMeta = im
            i.amount = 1
            e.block.world.dropItemNaturally(e.block.location, i)
            destroy(e)
        }
    }

    override fun compareBlockId(other: Block?): Boolean {
        other ?: return false
        val blockData = CustomBlockData(other, KPlugin.instance)
        return blockData.get(KEY, PersistentDataType.INTEGER) == id
    }
}