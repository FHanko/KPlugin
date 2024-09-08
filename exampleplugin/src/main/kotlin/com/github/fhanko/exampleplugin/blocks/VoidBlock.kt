package com.github.fhanko.exampleplugin.blocks

import com.github.fhanko.blocks.BlockBase
import com.github.fhanko.blocks.blockhandler.DroppedOnHandler
import com.github.fhanko.items.itemhandler.DroppedItemEvent
import net.kyori.adventure.text.Component
import org.bukkit.Material

object VoidBlock: BlockBase(8, Material.GLASS, Component.text("Void Block")), DroppedOnHandler {
    override fun droppedOn(e: DroppedItemEvent) {
        e.dropper.sendMessage("Voided")
        e.item.remove()
        e.isCancelled = true
    }
}
