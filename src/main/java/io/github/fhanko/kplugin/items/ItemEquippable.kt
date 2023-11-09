@file:Suppress("unused")
package io.github.fhanko.kplugin.items

import org.bukkit.entity.Player
import org.bukkit.event.Listener

/**
 * Implementable for subclasses of ItemBase to override item equip and unequip functions.
 */
interface ItemEquippable: Listener, ItemComparable {
    enum class EquipType { Hand, Armour }
    fun equip(p: Player, e: EquipType) { }
    fun unequip(p: Player, e: EquipType) { }

    enum class ArmourSlot(var slot: Int) {
        None(-1), Boots(36), Legs(37), Body(38), Helmet(39), Shield(40)
    }
    fun armourSlot(): ArmourSlot = ArmourSlot.None
}