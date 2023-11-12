package io.github.fhanko.kplugin.blocks

import com.destroystokyo.paper.profile.ProfileProperty
import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent
import io.github.fhanko.kplugin.display.DisplayList
import io.github.fhanko.kplugin.util.Schedulable
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.entity.Display
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector
import org.joml.Vector3f
import java.util.*

val BLOCK_DISPLAY_ID_KEY = NamespacedKey("kplugin", "texturedblock")
val OFFSET = Vector(0.5, 1.01, 0.5)
/**
 * Represents a block that is covered by a supplied player head texture.
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
     * Covers the supplied block with a skull textured by coverItem. Marks the block with the display(cover) id for later removal.
     */
    protected fun coverBlock(block: Block, coverItem: ItemStack): ItemDisplay {
        val display = block.world.spawn(block.location.add(OFFSET), ItemDisplay::class.java)
        display.itemStack = coverItem
        val t = display.transformation.apply { scale.set(OFFSET.y * 2); display.transformation = this }
        display.brightness = Display.Brightness(7, 7)
        block.type = overrideMaterial
        // Mark block with display id for later removal
        markBlock(block, BLOCK_DISPLAY_ID_KEY, PersistentDataType.STRING, display.uniqueId.toString())
        return display
    }

    /**
     * Covers the supplied block using coverBlock(...) and also turns the cover to face the supplied player.
     */
    private fun placeBlock(block: Block, p: Player) {
        val display = coverBlock(block, item)
        val t = display.transformation
        val angle = (360 - p.yaw + 180)
        val angleSnapped = (Math.round(angle / 90) * 90) + 180
        t.leftRotation.fromAxisAngleDeg(Vector3f(0f, 1f, 0f), angleSnapped.toFloat())
        display.transformation = t
    }

    /**
     * Covers a block in Skull texture when placed. Call super when overriding
     */
    override fun place(e: BlockPlaceEvent) {
        placeBlock(e.block, e.player)
    }

    /**
     * Removes Skull texture when destroyed. Call super when overriding
     * This removes the display with entity id marked during covering.
     */
    override fun destroy(e: CustomBlockDataRemoveEvent) {
        val currentDisplay = UUID.fromString(readBlock(e.block, BLOCK_DISPLAY_ID_KEY, PersistentDataType.STRING))
        currentDisplay?.apply { DisplayList.displayIds[this]?.remove() }
    }
}