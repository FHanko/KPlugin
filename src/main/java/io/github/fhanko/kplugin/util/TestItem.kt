package io.github.fhanko.kplugin.zones

import io.github.fhanko.kplugin.items.ItemBase
import io.github.fhanko.kplugin.items.ItemDroppable
import io.github.fhanko.kplugin.items.ItemEquippable
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.entity.Player

class TestItem: ItemBase(1, Material.DIAMOND, "test", listOf("Test")), ItemEquippable, ItemDroppable {
    override fun equip(p: Player) {
        println("E2")
    }

    override fun unequip(p: Player) {
        println("U2")
    }

    override fun drop(p: Player, i: Item) {
        println("Dropped")
    }
}