package io.github.fhanko.kplugin.util

import io.github.fhanko.kplugin.blocks.BlockBase
import io.github.fhanko.kplugin.blocks.BlockClickable
import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent

object TestBlock: BlockBase(1000, Material.DIAMOND_BLOCK, "Test"), BlockClickable {
    override fun destroy(e: BlockBreakEvent) {
        e.player.sendMessage("Destroyed test")
    }

    override fun rightClick(e: PlayerInteractEvent) {
        e.player.sendMessage("Clicked test")
    }
}