package com.github.fhanko.blocks.blockhandler

import com.github.fhanko.items.itemhandler.DroppedItemEvent

interface DroppedOnHandler {
    /**
     * Called when an item is dropped on this block.
     */
    fun droppedOn(e: DroppedItemEvent) { }
}