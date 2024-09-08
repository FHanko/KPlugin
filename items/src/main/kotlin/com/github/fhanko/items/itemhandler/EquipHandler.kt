@file:Suppress("unused")
package com.github.fhanko.items.itemhandler

import org.bukkit.entity.Player

/**
 * Implementable for subclasses of ItemBase to override item equip and unequip functions.
 * Fired when a subclass item is equipped or unequipped in hand or amour slot.
 */
interface EquipHandler {
    enum class EquipType { Hand, Armour }
    fun equip(p: Player, e: EquipType) { }
    fun unequip(p: Player, e: EquipType) { }

    enum class ArmourSlot(var slot: Int) {
        None(-1), Boots(36), Legs(37), Body(38), Helmet(39), Shield(40)
    }
    fun armourSlot(): ArmourSlot = ArmourSlot.None
}