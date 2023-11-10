package io.github.fhanko.kplugin.blocks.handler

import com.jeff_media.customblockdata.CustomBlockData
import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.blocks.BlockBase
import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.util.Initializable
import io.github.fhanko.kplugin.util.copyPdc
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack

/**
 * Event listener for block handler
 *
 * Addresses event efficiency concerns by handling them once and firing handler functions
 * instead of firing all events on all blocks.
 */
object BlockListener: Listener, Initializable {
    @EventHandler
    fun onPlace(e: BlockPlaceEvent) {
        val base = ItemBase.get(e.itemInHand)
        if (base is PlaceHandler) base.also {
            it.place(e)
            val blockData = CustomBlockData(e.blockPlaced, KPlugin.instance)
            copyPdc(e.itemInHand.itemMeta.persistentDataContainer, blockData)
        }
    }

    @EventHandler
    fun onBreak(e: BlockBreakEvent) {
        val base = BlockBase.get(e.block)
        if (base is PlaceHandler) base.also {
            it.destroy(e)
            e.isDropItems = false
            val i = ItemStack(it.item)
            i.editMeta { im -> copyPdc(CustomBlockData(e.block, KPlugin.instance), im.persistentDataContainer) }
            i.amount = 1
            e.block.world.dropItemNaturally(e.block.location, i)
        }
    }
}