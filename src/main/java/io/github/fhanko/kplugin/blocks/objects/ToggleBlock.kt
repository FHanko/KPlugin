package io.github.fhanko.kplugin.blocks.objects

import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent
import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.blocks.AnimatedBlock
import io.github.fhanko.kplugin.items.ItemArgument
import io.github.fhanko.kplugin.items.handler.ClickHandler
import io.github.fhanko.kplugin.items.objects.CurrencyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

private val ISON_KEY = NamespacedKey(KPlugin.instance, "toggleblock")
private val frames = mutableListOf<String>(
    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI4YzkyYmMwYWMxZjdlNTExZjJjNGEzYmZmNDM4YzdlZDVkNTViMDRmZWMzNzVlZjI3YTM4YmU1NWNkODk0ZiJ9fX0=",
    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2ZlMmY1MTE2NzdkMzljNDNlYTM3NjUxMmI2OTIxMmU0NWU4MTBkYzljZjk2NmRiY2M4OTgwZDllMDE2NzY3NCJ9fX0="
)
object ToggleBlock: AnimatedBlock(frames, 7, Material.IRON_BLOCK, Component.text("Toggle Block")), ClickHandler {
    override fun rightClickBlock(e: PlayerInteractEvent) {
        val block = e.clickedBlock!!
        nextFrame(block)
        val isOn = readBlock(block, ISON_KEY, PersistentDataType.BOOLEAN)!!
        markBlock(block, ISON_KEY, PersistentDataType.BOOLEAN, !isOn)

        if (!isOn) {
            val i = CurrencyItem.instance(1, ItemArgument(1f))
            val loc = block.location.add(0.0, 0.5, 0.0)
            scheduleRepeat(block.location.toString(), 1000,
                {_ ->
                    block.world.dropItem(loc, i)
                }
            )
        } else {
            cancelSchedule(block.location.toString())
        }
    }

    override fun place(e: BlockPlaceEvent) {
        super.place(e)
        markBlock(e.block, ISON_KEY, PersistentDataType.BOOLEAN, false)
    }

    override fun destroy(e: CustomBlockDataRemoveEvent) {
        super.destroy(e)
        cancelSchedule(e.block.location.toString())
    }
}