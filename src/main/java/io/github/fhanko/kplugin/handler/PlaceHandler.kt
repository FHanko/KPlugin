package io.github.fhanko.kplugin.handler

import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

interface PlaceHandler {
    fun place(e: BlockPlaceEvent) { }
    fun destroy(e: BlockBreakEvent) { }
}