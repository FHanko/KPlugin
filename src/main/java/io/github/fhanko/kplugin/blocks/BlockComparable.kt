package io.github.fhanko.kplugin.blocks

import org.bukkit.block.Block

interface BlockComparable {
    fun compareBlockId(other: Block?): Boolean
}