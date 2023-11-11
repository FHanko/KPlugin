package io.github.fhanko.kplugin.blocks.objects

import io.github.fhanko.kplugin.blocks.TexturedBlock
import io.github.fhanko.kplugin.items.handler.ClickHandler
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent

private const val TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmMzMTNhOGE1MzE4NjM4OGI5YjVmMDdhOGRhZTg4NThhYTI0YmE4Njk4YzgyZTdlZjdiYTg3NTg4MDlhYWIzNyJ9fX0="
object TestBlock: TexturedBlock(TEXTURE, 4, Material.IRON_BLOCK, Component.text("Test")), ClickHandler {
    override fun broke(e: BlockBreakEvent) {
        e.player.sendMessage("Destroyed test")
    }

    override fun rightClickBlock(e: PlayerInteractEvent) {
        e.player.sendMessage("Clicked test")
    }
}