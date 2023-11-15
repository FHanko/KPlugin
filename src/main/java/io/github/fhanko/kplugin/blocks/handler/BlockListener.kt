package io.github.fhanko.kplugin.blocks.handler

import com.jeff_media.customblockdata.CustomBlockData
import com.jeff_media.customblockdata.events.CustomBlockDataMoveEvent
import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent
import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.blocks.BlockBase
import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.util.Cooldownable
import io.github.fhanko.kplugin.util.copyPdc
import io.papermc.paper.event.player.PlayerArmSwingEvent
import net.kyori.adventure.text.Component
import org.bukkit.Material
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
object BlockListener: Listener {
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
            it.broke(e)
            e.isDropItems = false
            val i = ItemStack(it.item)
            i.editMeta { im -> copyPdc(CustomBlockData(e.block, KPlugin.instance), im.persistentDataContainer) }
            i.amount = 1
            e.block.world.dropItemNaturally(e.block.location, i)
        }
    }

    @EventHandler
    fun onDestroy(e: CustomBlockDataRemoveEvent) {
        e.block.type = Material.AIR
        val base = BlockBase.get(e.block)
        if (base is PlaceHandler) base.destroy(e)
    }

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
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
}

object RayTraceTracker: Cooldownable {
    override fun getCooldown(): Long = 300L
    override fun cooldownMessage(cooldown: Long): Component = Component.text("")
}