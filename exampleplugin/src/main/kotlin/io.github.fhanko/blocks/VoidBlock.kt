package io.github.fhanko.blocks

import io.github.fhanko.TexturedBlock
import io.github.fhanko.blockhandler.DroppedOnHandler
import io.github.fhanko.itemhandler.DroppedItemTickEvent
import net.kyori.adventure.text.Component
import org.bukkit.Material

private const val TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQxYzUyZDMzNTc4YjBhYWI5NTA3NGE4YjY1MjM4OTFlNjZhNWEyNzI5YjVlOTA0NzE1MmM5MzNhNjJlODFlNyJ9fX0="
object VoidBlock: TexturedBlock(TEXTURE, 8, Material.GLASS, Component.text("Void Block")), DroppedOnHandler {
    override fun droppedOn(e: DroppedItemTickEvent) {
        e.item.remove()
        e.isCancelled = true
    }
}