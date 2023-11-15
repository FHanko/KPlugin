package io.github.fhanko.kplugin.blocks.handler

import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

interface PlaceHandler {
    /**
     * Called when a player places this block.
     */
    fun place(e: BlockPlaceEvent) { }
    /**
     * Called when a player replaces this block. Only possible for replaceable block types.
     * CustomBlockData is not lost when replacing.
     */
    fun replace(e: BlockPlaceEvent) { }
    /**
     * Called when a player removes this block.
     */
    fun broke(e: BlockBreakEvent) { }
    /**
     * Called when any event removes this block.
     */
    fun destroy(e: CustomBlockDataRemoveEvent) { }
}