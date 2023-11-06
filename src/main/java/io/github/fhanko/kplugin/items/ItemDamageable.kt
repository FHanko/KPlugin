package io.github.fhanko.kplugin.items

import io.github.fhanko.kplugin.KPluginPlayerItemDamageEvent
import io.github.fhanko.kplugin.util.mm
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import kotlin.math.max

private val DURABILITY_KEY = NamespacedKey("kplugin", "itemdamageable")
interface ItemDamageable: ItemComparable {
    fun getDurability(item: ItemStack): Pair<Int, Int>? {
        val arr = ItemBase.readItem(item, DURABILITY_KEY, PersistentDataType.INTEGER_ARRAY) ?: return null
        return Pair(arr[0], arr[1])
    }

    fun durabilityText(durability: Int, maxDurability: Int): Component? = null

    fun setDurability(item: ItemStack, durability: Int, maxDurability: Int) {
        ItemBase.markItem(item, DURABILITY_KEY, PersistentDataType.INTEGER_ARRAY, arrayOf(durability, maxDurability).toIntArray())
        // Remove old durabilityText and add the new one
        if (durabilityText(durability, maxDurability) != null) {
            val regex = Regex("[a-zA-Z]+")
            val im = item.itemMeta
            val lore = im.lore() ?: mutableListOf()
            lore.removeIf { regex.find(mm.serialize(it))?.value == regex.find(mm.serialize(durabilityText(durability, maxDurability)!!))?.value }
            lore.add(durabilityText(durability, maxDurability))
            im.lore(lore)
            item.itemMeta = im
        }
    }

    fun damageItem(player: Player, item: ItemStack, damage: Int) {
        val dur = getDurability(item) ?: return
        setDurability(item, max(0, dur.first - damage), dur.second)
        Bukkit.getPluginManager().callEvent(PlayerItemDamageEvent(player, item, damage, damage))
    }

    fun onDamage(e: PlayerItemDamageEvent) { }

    @EventHandler
    fun onItemDamaged(kpe: KPluginPlayerItemDamageEvent) {
        val e = kpe.baseEvent
        if (compareId(e.item)) { onDamage(e) }
    }
}