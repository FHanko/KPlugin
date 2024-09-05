package io.github.fhanko

import com.destroystokyo.paper.profile.ProfileProperty
import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent
import io.github.fhanko.entityhandler.EntityListener
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Skull
import org.bukkit.entity.Display
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector
import java.util.*

/**
 * Represents a [Block] that is covered by a supplied head([Skull]) texture.
 * Get textures from https://minecraft-heads.com
 */
abstract class TexturedBlock(texture: String, id: Int, private val overrideMaterial: Material, name: Component, lore: List<Component> = mutableListOf())
    : BlockBase(id, Material.PLAYER_HEAD, name, lore), Schedulable {
    private val offset = Vector(0.5, 1.01, 0.5)
    private val displayId = BlockData(PersistentDataType.STRING, "displayId")
    protected open val opaque = true
    init {
        val profile = Bukkit.getServer().createProfile(UUID.randomUUID())
        val property = ProfileProperty("textures", texture)
        profile.properties.add(property)
        item.editMeta { it as SkullMeta; it.playerProfile = profile }
    }

    /**
     * Covers the supplied [block] with a [Display] (Skull texture) by [coverItem]. Marks the [block] with the displays unique id for later removal.
     */
    protected fun coverBlock(block: Block, coverItem: ItemStack): ItemDisplay {
        val display = block.world.spawn(block.location.add(offset), ItemDisplay::class.java)
        display.setItemStack(coverItem)
        // Avoid any overlap
        val extraOffset = 0.001 * random.nextFloat()
        display.transformation.apply { scale.set(offset.y * 2 + extraOffset); display.transformation = this }
        if (opaque) display.brightness = Display.Brightness(7, 7)
        block.type = overrideMaterial
        // Mark block with display id for later removal
        displayId.setBlock(block, display.uniqueId.toString())
        return display
    }

    /**
     * Returns the [Display] associated with [block].
     */
    protected fun getDisplay(block: Block): ItemDisplay? {
        return EntityListener.displayIds.getOrDefault(UUID.fromString(displayId.getBlock(block)), null) as ItemDisplay?
    }

    /**
     * Removes the [Display] associated with [block].
     */
    protected fun removeDisplay(block: Block) {
        val id = displayId.getBlock(block) ?: return
        UUID.fromString(id)?.apply { EntityListener.displayIds[this]?.remove() }
        displayId.removeBlock(block)
    }

    /**
     * Covers the [Block] of [e] using [coverBlock] and also turns the cover to face the [Player] of [e].
     */
    override fun place(e: BlockPlaceEvent) {
        DisplayUtil.blockFacePlayer(coverBlock(e.block, item), e.player)
    }

    override fun broke(e: BlockBreakEvent) {
        removeDisplay(e.block)
    }

    override fun destroy(e: CustomBlockDataRemoveEvent) {
        removeDisplay(e.block)
    }
}