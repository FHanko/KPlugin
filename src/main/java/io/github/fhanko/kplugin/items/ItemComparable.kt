package io.github.fhanko.kplugin.items

import org.bukkit.inventory.ItemStack

interface ItemComparable {
    fun compareId(other: ItemStack?): Boolean
}