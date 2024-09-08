package io.github.fhanko.blockhandler

import com.jeff_media.customblockdata.CustomBlockData
import com.jeff_media.customblockdata.events.CustomBlockDataMoveEvent
import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent
import io.github.fhanko.*
import io.github.fhanko.itemhandler.ClickHandler
import io.github.fhanko.itemhandler.Cooldownable
import io.github.fhanko.itemhandler.DroppedItemTickEvent
import net.kyori.adventure.text.Component
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

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

        if (RayTraceTracker.useCooldown(e.player, RayTraceTracker.hashCode().toString())) {
            val b = e.player.rayTraceBlocks(MAX_TRACE_DISTANCE)?.hitBlock ?: return
            val base = BlockBase.get(b)
            if (base is RayTraceHandler && e.player.location.distance(b.location) <= base.traceDistance()) base.trace(e)
        }
    }

    @EventHandler
    fun onMove(e: CustomBlockDataMoveEvent) {
        val base = BlockBase.get(e.block)
        if (base is MoveHandler) base.move(e)
    }

    @EventHandler
    fun onDroppedOn(e: DroppedItemTickEvent) {
        if (!e.item.isOnGround) return
        val base = BlockBase.get(e.item.location.block.getRelative(BlockFace.DOWN))
        if (base is DroppedOnHandler) base.droppedOn(e)
    }
}

object RayTraceTracker: Cooldownable {
    override fun getCooldown(): Long = 300L
    override fun cooldownMessage(cooldown: Long): Component = Component.text("")
}