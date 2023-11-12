package io.github.fhanko.kplugin.blocks

import com.destroystokyo.paper.profile.ProfileProperty
import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Display
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.util.Vector
import org.joml.Vector3f
import java.util.*

val OFFSET = Vector(0.5, 1.01, 0.5)
//Get textures from
//https://minecraft-heads.com
abstract class TexturedBlock(texture: String, id: Int, private val overrideMaterial: Material, name: Component, lore: List<Component> = mutableListOf()): BlockBase(id, Material.PLAYER_HEAD, name, lore) {
    init {
        val profile = Bukkit.getServer().createProfile(UUID.randomUUID())
        val property = ProfileProperty("textures", texture)
        profile.properties.add(property)
        item.editMeta { it as SkullMeta; it.playerProfile = profile }
    }

    /**
     * Removes Skull texture from block by removing any entity at the blocks position. This can not be done with entity
     * id as entities cannot be fetched by id, but iterating chunk entities should not be computationally concerning for now.
     */
    protected fun removeCover(block: Block) {
        block.chunk.entities.forEach { if (it.location == block.location.add(OFFSET)) it.remove() }
    }

    protected fun coverBlock(block: Block, coverItem: ItemStack): ItemDisplay {
        val display = block.world.spawn(block.location.add(OFFSET), ItemDisplay::class.java)
        display.itemStack = coverItem
        val t = display.transformation.apply { scale.set(OFFSET.y * 2); display.transformation = this }
        display.brightness = Display.Brightness(7, 7)
        block.type = overrideMaterial
        return display
    }

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
     */
    override fun destroy(e: CustomBlockDataRemoveEvent) {
        removeCover(e.block)
    }
}