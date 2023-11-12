package io.github.fhanko.kplugin.blocks

import com.destroystokyo.paper.profile.ProfileProperty
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
        BlockBase.markBlock(e.block, ANIMATION_KEY, PersistentDataType.INTEGER, 0)
    }

    protected fun getFrame(block: Block): Int? = readBlock(block, ANIMATION_KEY, PersistentDataType.INTEGER)

    protected fun setFrame(block: Block, frame: Int?) {
        val modFrame = frame?.rem(texture.size) ?: return
        removeCover(block)
        coverBlock(block, skulls[modFrame])
        BlockBase.markBlock(block, ANIMATION_KEY, PersistentDataType.INTEGER, modFrame)
    }

    protected fun nextFrame(block: Block) {
        val frame = getFrame(block) ?: return
        setFrame(block, frame.plus(1))
    }
}