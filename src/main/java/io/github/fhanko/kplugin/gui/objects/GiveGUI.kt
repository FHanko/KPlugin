package io.github.fhanko.kplugin.gui.objects

import io.github.fhanko.kplugin.gui.GUI
import io.github.fhanko.kplugin.gui.GUIItem
import io.github.fhanko.kplugin.items.ItemBase
import net.kyori.adventure.text.Component
import org.bukkit.Material

object GiveGUI: GUI(36) {
    init {
        val g = GUI.parse(  "#########"+
                                    "---------"+
                                    "---------"+
                                    "#########").setCharacter('#', GUIItem(Component.text(""), { _, _ -> }, Material.IRON_BLOCK))
        g.setItem(9, ItemBase.itemList.values.map { GUIItem(it.item) { _, p -> it.give(p); p.closeInventory() } })
        inventory = g.inventory
    }
}