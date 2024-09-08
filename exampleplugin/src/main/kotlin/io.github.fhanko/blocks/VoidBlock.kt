package io.github.fhanko.blocks

import io.github.fhanko.BlockBase
import io.github.fhanko.blockhandler.DroppedOnHandler
import io.github.fhanko.itemhandler.DroppedItemEvent
import net.kyori.adventure.text.Component
import org.bukkit.Material

object VoidBlock: BlockBase(8, Material.GLASS, Component.text("Void Block")), DroppedOnHandler {
    override fun droppedOn(e: DroppedItemEvent) {
        e.dropper.sendMessage("Voided")
        e.item.remove()
        e.isCancelled = true
    }
}
