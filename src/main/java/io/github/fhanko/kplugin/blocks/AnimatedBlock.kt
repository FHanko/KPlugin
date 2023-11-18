package io.github.fhanko.kplugin.blocks

import com.destroystokyo.paper.profile.ProfileProperty
import io.github.fhanko.kplugin.KPlugin
import io.github.fhanko.kplugin.display.DisplayUtil
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.entity.ItemDisplay
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import java.util.*

private val ANIMATION_KEY = NamespacedKey(KPlugin.instance, "animatedblock")
private const val noTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVkMjAzMzBkYTU5YzIwN2Q3ODM1MjgzOGU5MWE0OGVhMWU0MmI0NWE5ODkzMjI2MTQ0YjI1MWZlOWI5ZDUzNSJ9fX0="
/**
 * Represents a [TexturedBlock] that holds multiple textures that can be changed dynamically.
 */
abstract class AnimatedBlock(val textures: MutableList<String>, id: Int, material: Material, name: Component, lore: List<Component> = listOf())
    : TexturedBlock(textures[0], id, material, name, lore) {
    private val skulls = mutableListOf<ItemStack>()

    init {
        if (textures.size == 0) textures.add(noTexture)
        textures.forEach { tex ->
            skulls.add(ItemStack(Material.PLAYER_HEAD))
            val profile = Bukkit.getServer().createProfile(UUID.randomUUID())
            val property = ProfileProperty("textures", tex)
            profile.properties.add(property)
            skulls.last().editMeta { sm -> sm as SkullMeta; sm.playerProfile = profile }
        }
    }

    override fun place(e: BlockPlaceEvent) {
        DisplayUtil.facePlayer(coverBlock(e.block, skulls.first()), e.player)
        markBlock(e.block, ANIMATION_KEY, PersistentDataType.INTEGER, 0)
    }

    /**
     * Returns the id of current frame from [block].
     */
    protected fun getFrame(block: Block): Int? = readBlock(block, ANIMATION_KEY, PersistentDataType.INTEGER)

    /**
     * Sets the frame of [block]s [TexturedBlock] cover.
     */
    protected fun setFrame(block: Block, frame: Int?) {
        val modFrame = frame?.rem(textures.size) ?: return
        // Keep the transformation of the old display
        val oldDisplay = getDisplay(block)
        val oldTransformation = oldDisplay?.transformation
        // Remove old cover delayed to avoid the block being naked while the new display loads
        oldDisplay?.apply { schedule(oldDisplay.uniqueId.toString(), 100, { i -> (i[0] as ItemDisplay).remove() }, this) }
        // Place new cover
        val newCover = coverBlock(block, skulls[modFrame])
        oldTransformation?.apply { newCover.transformation = this }
        // Mark the current frame on the block
        markBlock(block, ANIMATION_KEY, PersistentDataType.INTEGER, modFrame)
    }

    /**
     * Increases the [block]s frame by 1.
     */
    protected fun nextFrame(block: Block) {
        val frame = getFrame(block) ?: return
        setFrame(block, frame.plus(1))
    }
}