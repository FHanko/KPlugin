package io.github.fhanko

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.util.BoundingBox

abstract class Zone(val start: Location, val end: Location): ConfigurationSerializable {
    private val box: BoundingBox = BoundingBox.of(start, end)

    /**
    * Returns all chunks that contain blocks of the Zone
    */
    fun chunkList(): Set<Chunk> {
        val ret = mutableSetOf<Chunk>()
        // Block coordinate shr 4 is the chunk coordinate
        for (x in (box.minX.toInt() shr 4) .. (box.maxX.toInt() shr 4))
        for (z in (box.minZ.toInt() shr 4) .. (box.maxZ.toInt() shr 4)) {
            ret.add(start.world.getChunkAt(x, z))
        }
        return ret
    }

    lateinit var blocks: MutableList<Block>
        private set

    /**
     * Caches blocks in this zone to [blocks].
     */
    open fun addBlocks() {
        blocks = mutableListOf()
        for (x in box.minX.rangeTo(box.maxX))
        for (y in box.minY.rangeTo(box.maxY))
        for (z in box.minZ.rangeTo(box.maxZ)) {
            blocks.add(end.world.getBlockAt(x, y, z))
        }
    }

    /**
     * Called when the zone is created.
     */
    open fun create() { }

    /**
     * Called when the zone is removed.
     */
    open fun remove() { }

    /**
    * Returns true if that location is in the Zone.
    */
    fun isIn(that: Location) = box.contains(that.x, that.y, that.z)

    // ConfigurationSerializable serialization.
    override fun serialize() = mapOf("start" to start, "end" to end)
}