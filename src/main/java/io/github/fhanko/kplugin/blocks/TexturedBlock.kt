package io.github.fhanko.kplugin.blocks

import com.destroystokyo.paper.profile.ProfileProperty
import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent
import io.github.fhanko.kplugin.display.DisplayListener
import io.github.fhanko.kplugin.display.DisplayUtil
import io.github.fhanko.kplugin.util.Schedulable
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.block.Skull
import org.bukkit.entity.Display
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector
import java.util.*

private val BLOCK_DISPLAY_ID_KEY = NamespacedKey("kplugin", "texturedblock")
val OFFSET = Vector(0.5, 1.01, 0.5)
/**
 * Represents a [Block] that is covered by a supplied head([Skull]) texture.
 * Get textures from https://minecraft-heads.com
 */
abstract class TexturedBlock(texture: String, id: Int, private val overrideMaterial: Material, name: Component, lore: List<Component> = mutableListOf())
    : BlockBase(id, Material.PLAYER_HEAD, name, lore), Schedulable {

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
        val display = block.world.spawn(block.location.add(OFFSET), ItemDisplay::class.java)
        display.itemStack = coverItem
        display.transformation.apply { scale.set(OFFSET.y * 2); display.transformation = this }
        display.brightness = Display.Brightness(7, 7)
        block.type = overrideMaterial
        // Mark block with display id for later removal
        markBlock(block, BLOCK_DISPLAY_ID_KEY, PersistentDataType.STRING, display.uniqueId.toString())
        return display
    }

    /**
     * Returns the [Display] associated with [block].
     */
    protected fun getDisplay(block: Block): ItemDisplay? {
        return DisplayListener.displayIds.getOrDefault(UUID.fromString(readBlock(block, BLOCK_DISPLAY_ID_KEY, PersistentDataType.STRING)), null) as ItemDisplay?
    }

    /**
     * Covers the [Block] of [e] using [coverBlock] and also turns the cover to face the [Player] of [e].
     */
    override fun place(e: BlockPlaceEvent) {
        DisplayUtil.facePlayer(coverBlock(e.block, item), e.player)
    }

    /**
     * Removes [Display] covering this block when destroyed.
     */
    override fun destroy(e: CustomBlockDataRemoveEvent) {
        val currentDisplay = UUID.fromString(readBlock(e.block, BLOCK_DISPLAY_ID_KEY, PersistentDataType.STRING))
        currentDisplay?.apply { DisplayListener.displayIds[this]?.remove() }
    }
}