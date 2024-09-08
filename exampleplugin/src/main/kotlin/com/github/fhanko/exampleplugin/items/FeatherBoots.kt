package com.github.fhanko.exampleplugin.items

import com.github.fhanko.items.ItemBase
import com.github.fhanko.items.itemhandler.EquipHandler
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object FeatherBoots: ItemBase(2, Material.LEATHER_BOOTS, "Feather Boots", listOf("These boots make you feel lighter"))
    , EquipHandler {
    override fun equip(p: Player, e: EquipHandler.EquipType) {
        if (e == EquipHandler.EquipType.Armour)
            p.player?.addPotionEffect(PotionEffect(PotionEffectType.JUMP_BOOST, Int.MAX_VALUE, 0))
    }

    override fun unequip(p: Player, e: EquipHandler.EquipType) {
        if (e == EquipHandler.EquipType.Armour)
            p.player?.removePotionEffect(PotionEffectType.JUMP_BOOST)
    }

    override fun armourSlot() = EquipHandler.ArmourSlot.Boots
}