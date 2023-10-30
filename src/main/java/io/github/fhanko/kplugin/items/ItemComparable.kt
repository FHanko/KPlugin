package io.github.fhanko.kplugin.items

import org.bukkit.inventory.ItemStack

interface ItemComparable {
    abstract fun compareId(other: ItemStack?): Boolean
}