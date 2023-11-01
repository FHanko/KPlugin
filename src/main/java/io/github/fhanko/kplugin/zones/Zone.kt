package io.github.fhanko.kplugin.zones

import io.github.fhanko.kplugin.util.toward
import org.bukkit.Chunk
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Player
import java.io.Serializable
import kotlin.math.max
import kotlin.math.min

abstract class Zone: Serializable {
    /**
     * Returns true if that location is in the Zone
     */
    abstract fun isIn(that: Location): Boolean

    /**
     * Returns all chunks that contain blocks of the Zone
     */
    abstract fun chunkList(): Set<Chunk>

    /**
     * Called when a player enters this Zone
     */
    open fun enter(p: Player) { }

    /**
     * Called when a player leaves this Zone
     */
    open fun leave(p: Player) { }

    /**
     * Borders of the zone
     */
    abstract val borders: List<Location>
    abstract val borderColor: Color
}
 open class ZoneCube(private var start: Location, private var end: Location): Zone() {
    final override val borders = mutableListOf<Location>()

    init {
        for (x in 0 toward (end.x - start.x).toInt()) {
            borders.add(Location(start.world, start.x + x, start.y, start.z))
            borders.add(Location(start.world, start.x + x, end.y, start.z))
            borders.add(Location(start.world, start.x + x, end.y, end.z))
            borders.add(Location(start.world, start.x + x, start.y, end.z))
        }
        for (y in 0 toward (end.y - start.y).toInt()) {
            borders.add(Location(start.world, start.x, start.y + y, start.z))
            borders.add(Location(start.world, end.x, start.y + y, start.z))
            borders.add(Location(start.world, end.x, start.y + y, end.z))
            borders.add(Location(start.world, start.x, start.y + y, end.z))
        }
        for (z in 0 toward (end.z - start.z).toInt()) {
            borders.add(Location(start.world, start.x, start.y, start.z + z))
            borders.add(Location(start.world, end.x, start.y, start.z + z))
            borders.add(Location(start.world, end.x, end.y, start.z + z))
            borders.add(Location(start.world, start.x, end.y, start.z + z))
        }
    }

    override fun chunkList(): Set<Chunk> {
        val ret = mutableSetOf<Chunk>()
        // Block coordinate shr 4 is the chunk coordinate
        for (x in (start.x.toInt() shr 4) toward (end.x.toInt() shr 4)) {
            for (z in (start.z.toInt() shr 4) toward (end.z.toInt() shr 4)) {
                ret.add(start.world.getChunkAt(x, z))
            }
        }
        return ret
    }

    override fun isIn(that: Location) =
        (that.x > min(start.x, end.x) && that.z > min(start.z, end.z) && that.y > min(start.y, end.y) &&
                that.x < max(start.x, end.x) && that.z < max(start.z, end.z) && that.y < max(start.y, end.y))

     override val borderColor: Color = Color.RED
}