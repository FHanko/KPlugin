package io.github.fhanko.kplugin.items.objects

import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.items.ItemEquippable
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object FeatherBoots: ItemBase(2, Material.LEATHER_BOOTS, "Feather Boots", listOf("These boots make you feel lighter"))
    , ItemEquippable {
    override fun equip(p: Player, e: ItemEquippable.EquipType) {
        if (e == ItemEquippable.EquipType.Armour)
            p.player?.addPotionEffect(PotionEffect(PotionEffectType.JUMP, Int.MAX_VALUE, 0))
    }

    override fun unequip(p: Player, e: ItemEquippable.EquipType) {
        if (e == ItemEquippable.EquipType.Armour)
            p.player?.removePotionEffect(PotionEffectType.JUMP)
    }

    override fun armourSlot() = ItemEquippable.ArmourSlot.Boots
}