package io.github.fhanko.gui

import io.github.fhanko.GUI
import io.github.fhanko.GUIItem
import io.github.fhanko.ItemBase
import io.github.fhanko.mm
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object GiveGUI: GUI(36, mm.deserialize("Give GUI")) {
    init {
        parse(  "#########"+
                        "---------"+
                        "---------"+
                        "#########")
        setCharacter('#', GUIItem(ItemStack(Material.IRON_BLOCK).apply { editMeta { it.displayName(mm.deserialize("")) } }) { _, _ -> })
        setItem(9, ItemBase.itemMap.values.map { GUIItem(it.item) { _, p -> it.give(p); p.closeInventory() } })
    }
}