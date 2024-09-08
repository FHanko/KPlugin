package io.github.fhanko.blockhandler

import io.github.fhanko.itemhandler.DroppedItemTickEvent

interface DroppedOnHandler {
    /**
     * Called when an item is dropped on this block.
     */
    fun droppedOn(e: DroppedItemTickEvent) { }
}