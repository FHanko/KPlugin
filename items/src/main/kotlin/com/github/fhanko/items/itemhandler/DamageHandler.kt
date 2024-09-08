package com.github.fhanko.items.itemhandler

import com.github.fhanko.items.ItemData
import com.github.fhanko.util.mm
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import kotlin.math.max
import kotlin.math.min

private val itemDurability = ItemData(PersistentDataType.INTEGER_ARRAY, "itemDurability")
/**
 * Implementable for subclasses of ItemBase to override onDamage function which is called on the loss of durability.
 * If the implementing ItemStack of item does not have a durability, a durability based on [PersistentDataContainer]s can
 * be set using the durability setter and getter functions.
 */
interface DamageHandler {
    fun getDurability(item: ItemStack): Pair<Int, Int>? {
        val arr = itemDurability.get(item) ?: return null
        return Pair(arr[0], arr[1])
    }

    fun durabilityText(durability: Int, maxDurability: Int): Component? = null

    fun setDurability(item: ItemStack, durability: Int, maxDurability: Int) {
        val dur = min(durability, maxDurability)
        itemDurability.set(item, arrayOf(dur, maxDurability).toIntArray())
        // Remove old durabilityText and add the new one
        if (durabilityText(dur, maxDurability) != null) {
            val regex = Regex("[a-zA-Z]+")
            val im = item.itemMeta
            val lore = im.lore() ?: mutableListOf()
            lore.removeIf { regex.find(mm.serialize(it))?.value == regex.find(mm.serialize(durabilityText(dur, maxDurability)!!))?.value }
            lore.add(durabilityText(dur, maxDurability))
            im.lore(lore)
            item.itemMeta = im
        }
    }

    fun damageItem(player: Player, item: ItemStack, damage: Int) {
        val dur = getDurability(item) ?: return
        setDurability(item, max(0, dur.first - damage), dur.second)
        Bukkit.getPluginManager().callEvent(PlayerItemDamageEvent(player, item, damage, damage))
    }

    fun damage(e: PlayerItemDamageEvent) { }
}