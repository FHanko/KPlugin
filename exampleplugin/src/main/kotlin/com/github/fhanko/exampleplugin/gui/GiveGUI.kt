package com.github.fhanko.exampleplugin.gui

import com.github.fhanko.gui.GUI
import com.github.fhanko.gui.GUIItem
import com.github.fhanko.items.ItemBase
import com.github.fhanko.util.mm
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object GiveGUI: GUI(36, "Give GUI") {
    init {
        parse(  "#########"+
                        "---------"+
                        "---------"+
                        "#########")
        setCharacter('#', GUIItem(ItemStack(Material.IRON_BLOCK).apply { editMeta { it.displayName(mm.deserialize("")) } }) { _, _ -> })
        setItem(9, ItemBase.itemMap.values.map { GUIItem(it.item) { _, p -> it.give(p); p.closeInventory() } })
    }
}