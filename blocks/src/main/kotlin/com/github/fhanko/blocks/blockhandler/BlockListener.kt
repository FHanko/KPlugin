package com.github.fhanko.blocks.blockhandler

import com.jeff_media.customblockdata.CustomBlockData
import com.jeff_media.customblockdata.events.CustomBlockDataMoveEvent
import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent
import com.github.fhanko.blocks.BlockBase
import io.github.fhanko.*
import com.github.fhanko.items.itemhandler.ClickHandler
import com.github.fhanko.items.ItemBase
import com.github.fhanko.items.itemhandler.DroppedItemEvent
import com.github.fhanko.util.Initializable
import com.github.fhanko.util.PluginInstance
import com.github.fhanko.util.copyPdc
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

/**
 * Event listener for block handler.
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
            val blockData = CustomBlockData(e.blockPlaced, PluginInstance.instance)
            copyPdc(e.itemInHand.itemMeta.persistentDataContainer, blockData)
        }
    }

    @EventHandler
    fun onBreak(e: BlockBreakEvent) {
        val base = BlockBase.get(e.block)
        if (base is PlaceHandler) base.also {
            it.broke(e)
            if (base.dropBehaviour() == BlockBase.DropBehaviour.Material) return
            e.isDropItems = false
            if (base.dropBehaviour() == BlockBase.DropBehaviour.None) return
            val i = ItemStack(it.item)
            i.editMeta { im -> copyPdc(CustomBlockData(e.block, PluginInstance.instance), im.persistentDataContainer) }
            i.amount = 1
            e.block.world.dropItemNaturally(e.block.location, i)
        }
    }

    @EventHandler
    fun onDestroy(e: CustomBlockDataRemoveEvent) {
        val base = BlockBase.get(e.block)
        if (base is PlaceHandler) base.destroy(e)
    }

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        BlockBase.get(e.clickedBlock)?.also { if (it is ClickHandler) it.onBlockInteract(e) }
    }

    @EventHandler
    fun onMove(e: CustomBlockDataMoveEvent) {
        val base = BlockBase.get(e.block)
        if (base is MoveHandler) base.move(e)
    }

    @EventHandler
    fun onDroppedOn(e: DroppedItemEvent) {
        val midPoint = e.item.location.block.location.toCenterLocation()
        val vec = e.item.location.subtract(midPoint).multiply(2.0)
        listOf(Vector(vec.x, 0.0, 0.0), Vector(0.0, vec.y, 0.0), Vector(0.0, 0.0, vec.z)).forEach {
            if (e.isCancelled) return@forEach
            val base = BlockBase.get(midPoint.clone().add(it).block)
            if (base is DroppedOnHandler) base.droppedOn(e)
        }
    }
}
