package io.github.fhanko.items

import io.github.fhanko.ItemBase
import io.github.fhanko.itemhandler.EquipHandler
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object FeatherBoots: ItemBase(2, Material.LEATHER_BOOTS, "Feather Boots", listOf("These boots make you feel lighter"))
    , EquipHandler {
    override fun equip(p: Player, e: EquipHandler.EquipType) {
        if (e == EquipHandler.EquipType.Armour)
            p.player?.addPotionEffect(PotionEffect(PotionEffectType.JUMP, Int.MAX_VALUE, 0))
    }

    override fun unequip(p: Player, e: EquipHandler.EquipType) {
        if (e == EquipHandler.EquipType.Armour)
            p.player?.removePotionEffect(PotionEffectType.JUMP)
    }

    override fun armourSlot() = EquipHandler.ArmourSlot.Boots
}