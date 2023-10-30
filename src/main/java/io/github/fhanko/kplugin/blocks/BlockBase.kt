package io.github.fhanko.kplugin.blocks

import com.jeff_media.customblockdata.CustomBlockData
import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.items.ItemBase
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

abstract class BlockBase(private val id: Int,material: Material, name: String, description: List<String> = listOf())
    : ItemBase(id, material, name, description), Listener, BlockComparable {

    open fun place(e: BlockPlaceEvent) { }
    @EventHandler
    fun onPlace(e: BlockPlaceEvent) {
        if (compareId(e.itemInHand)) {
            val blockData = CustomBlockData(e.blockPlaced, KPlugin.instance)
            blockData.set(KEY, PersistentDataType.INTEGER, id)
            place(e)
        }
    }

    open fun destroy(e: BlockBreakEvent) { }
    @EventHandler
    fun onBreak(e: BlockBreakEvent) {
        if (compareBlockId(e.block)) {
            e.isDropItems = false
            e.block.world.dropItemNaturally(e.block.location, item)
            destroy(e)
        }
    }

    override fun compareBlockId(other: Block?): Boolean {
        other ?: return false
        val blockData = CustomBlockData(other, KPlugin.instance)
        return blockData.get(KEY, PersistentDataType.INTEGER) == id
    }
}