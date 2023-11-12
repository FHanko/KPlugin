package io.github.fhanko.kplugin.blocks.objects

import io.github.fhanko.kplugin.blocks.AnimatedBlock
import io.github.fhanko.kplugin.blocks.BlockBase
import io.github.fhanko.kplugin.items.handler.ClickHandler
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

private val ISON_KEY = NamespacedKey("kplugin", "eggspawner")
private val frames = mutableListOf<String>(
    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI4YzkyYmMwYWMxZjdlNTExZjJjNGEzYmZmNDM4YzdlZDVkNTViMDRmZWMzNzVlZjI3YTM4YmU1NWNkODk0ZiJ9fX0=",
    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2ZlMmY1MTE2NzdkMzljNDNlYTM3NjUxMmI2OTIxMmU0NWU4MTBkYzljZjk2NmRiY2M4OTgwZDllMDE2NzY3NCJ9fX0="
)
object EggSpawner: AnimatedBlock(frames, 7, Material.IRON_BLOCK, Component.text("Egg Spawner")), ClickHandler {
    override fun rightClickBlock(e: PlayerInteractEvent) {
        val block = e.clickedBlock!!
        val isOn = BlockBase.readBlock(block, ISON_KEY, PersistentDataType.BOOLEAN) ?: return
        nextFrame(block)
        BlockBase.markBlock(block, ISON_KEY, PersistentDataType.BOOLEAN, !isOn)
    }

    override fun place(e: BlockPlaceEvent) {
        super.place(e)
        BlockBase.markBlock(e.block, ISON_KEY, PersistentDataType.BOOLEAN, false)
    }
}