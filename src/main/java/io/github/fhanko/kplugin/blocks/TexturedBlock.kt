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
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.util.Vector
import org.joml.Vector3f
import java.util.*

val OFFSET = Vector(0.5, 1.01, 0.5)
//Get textures from
//https://minecraft-heads.com
abstract class TexturedBlock(texture: String, id: Int, private val overrideMaterial: Material, name: Component, lore: List<Component> = mutableListOf()): BlockBase(id, Material.PLAYER_HEAD, name, lore) {
    private lateinit var display: ItemDisplay
    init {
        val profile = Bukkit.getServer().createProfile(UUID.randomUUID())
        val property = ProfileProperty("textures", texture)
        profile.properties.add(property)
        item.editMeta { it as SkullMeta; it.playerProfile = profile }
    }

    private fun coverBlock(block: Block, p: Player) {
        display = block.world.spawn(block.location.add(OFFSET), ItemDisplay::class.java)
        display.itemStack = item
        val t = display.transformation
        t.scale.set(OFFSET.y * 2)
        val angle = (360 - p.yaw + 180)
        val angleSnapped = (Math.round(angle / 90) * 90) + 180
        t.leftRotation.fromAxisAngleDeg(Vector3f(0f, 1f, 0f), angleSnapped.toFloat())
        display.transformation = t
        display.brightness = Display.Brightness(7, 7)
    }

    /**
     * Covers a block in Skull texture when placed. Call super when overriding
     */
    override fun place(e: BlockPlaceEvent) {
        coverBlock(e.block, e.player)
        e.block.type = overrideMaterial
    }

    /**
     * Removes Skull texture when destroyed. Call super when overriding
     */
    override fun destroy(e: CustomBlockDataRemoveEvent) {
        e.block.chunk.entities.forEach { if (it.location == e.block.location.add(OFFSET)) it.remove() }
    }
}