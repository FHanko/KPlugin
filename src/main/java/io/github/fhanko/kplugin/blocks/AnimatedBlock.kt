package io.github.fhanko.kplugin.blocks

import com.destroystokyo.paper.profile.ProfileProperty
import io.github.fhanko.kplugin.display.DisplayList
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import java.util.*

private val ANIMATION_KEY = NamespacedKey("kplugin", "animatedblock")
private const val noTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVkMjAzMzBkYTU5YzIwN2Q3ODM1MjgzOGU5MWE0OGVhMWU0MmI0NWE5ODkzMjI2MTQ0YjI1MWZlOWI5ZDUzNSJ9fX0="
/**
 * Represents a [TexturedBlock] that holds multiple textures that can be changed dynamically.
 */
abstract class AnimatedBlock(private val texture: MutableList<String>, id: Int, material: Material, name: Component, lore: List<Component> = listOf())
    : TexturedBlock(texture[0], id, material, name, lore) {
    private val skulls = mutableListOf<ItemStack>()

    init {
        if (texture.size == 0) texture.add(noTexture)
        texture.forEach { tex ->
            skulls.add(ItemStack(Material.PLAYER_HEAD))
            val profile = Bukkit.getServer().createProfile(UUID.randomUUID())
            val property = ProfileProperty("textures", tex)
            profile.properties.add(property)
            skulls.last().editMeta { sm -> sm as SkullMeta; sm.playerProfile = profile }
        }
    }

    override fun place(e: BlockPlaceEvent) {
        coverBlock(e.block, skulls.first())
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
        val modFrame = frame?.rem(texture.size) ?: return
        // Remove old cover delayed to avoid the block being naked while the new display loads
        val currentDisplay = UUID.fromString(readBlock(block, BLOCK_DISPLAY_ID_KEY, PersistentDataType.STRING))
        currentDisplay?.apply { schedule(currentDisplay.toString(), 100, { i -> DisplayList.displayIds[i[0]]?.remove() }, this) }
        // Place new cover
        coverBlock(block, skulls[modFrame])
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