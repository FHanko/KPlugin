package io.github.fhanko.kplugin.items

import org.bukkit.inventory.ItemStack

/**
 * Provides the abstract function that is used to compare ItemBase objects.
 */
interface ItemComparable {
    fun compareId(other: ItemStack?): Boolean
}