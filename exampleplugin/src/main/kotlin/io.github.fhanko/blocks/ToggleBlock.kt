package io.github.fhanko.blocks

import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent
import io.github.fhanko.AnimatedBlock
import io.github.fhanko.BlockData
import io.github.fhanko.FloatArgument
import io.github.fhanko.itemhandler.ClickHandler
import io.github.fhanko.items.CurrencyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

private val frames = mutableListOf<String>(
    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI4YzkyYmMwYWMxZjdlNTExZjJjNGEzYmZmNDM4YzdlZDVkNTViMDRmZWMzNzVlZjI3YTM4YmU1NWNkODk0ZiJ9fX0=",
    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2ZlMmY1MTE2NzdkMzljNDNlYTM3NjUxMmI2OTIxMmU0NWU4MTBkYzljZjk2NmRiY2M4OTgwZDllMDE2NzY3NCJ9fX0="
)
object ToggleBlock: AnimatedBlock(frames, 7, Material.IRON_BLOCK, Component.text("Toggle Block")), ClickHandler {
    private val isOn = BlockData(PersistentDataType.BOOLEAN, "isOn")
    override fun rightClickBlock(e: PlayerInteractEvent) {
        val block = e.clickedBlock!!
        nextFrame(block)
        val on = isOn.getBlock(e.clickedBlock) ?: return
        isOn.setBlock(e.clickedBlock, !on)

        if (!on) {
            val i = CurrencyItem.instance(1, FloatArgument(1f))
            val loc = block.location.add(0.0, 0.5, 0.0)
            scheduleRepeat(block.location.toString(), 20,
                {_ ->
                    block.world.dropItem(loc, i)
                }
            )
        } else {
            scheduleCancel(block.location.toString())
        }
    }

    override fun place(e: BlockPlaceEvent) {
        super.place(e)
        isOn.setBlock(e.blockPlaced, false)
    }

    override fun destroy(e: CustomBlockDataRemoveEvent) {
        super.destroy(e)
        scheduleCancel(e.block.location.toString())
    }
}