package io.github.fhanko.kplugin.blocks

import org.bukkit.block.Block

/**
 * Provides the abstract function that is used to compare ItemBlock objects.
 */
interface BlockComparable {
    fun compareBlockId(other: Block?): Boolean
}