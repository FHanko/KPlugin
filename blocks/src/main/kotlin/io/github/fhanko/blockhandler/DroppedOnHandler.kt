package io.github.fhanko.blockhandler

import io.github.fhanko.itemhandler.DroppedItemEvent

interface DroppedOnHandler {
    /**
     * Called when an item is dropped on this block.
     */
    fun droppedOn(e: DroppedItemEvent) { }
}