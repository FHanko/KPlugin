package com.github.fhanko.blocks.blockhandler

import com.jeff_media.customblockdata.events.CustomBlockDataMoveEvent

interface MoveHandler {
    /**
     * Called when this block is moved by a piston.
     */
    fun move(e: CustomBlockDataMoveEvent) { }
}