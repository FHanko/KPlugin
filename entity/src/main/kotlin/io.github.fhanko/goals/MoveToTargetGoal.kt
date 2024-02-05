package io.github.fhanko.goals

import com.destroystokyo.paper.entity.ai.Goal
import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.GoalType
import com.jeff_media.morepersistentdatatypes.datatypes.serializable.ConfigurationSerializableDataType
import io.github.fhanko.PluginInstance
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Mob
import java.util.*

class MoveToTargetGoal(mob: Mob, private val target: Location, private val squaredMaxDistance: Double, priority: Int) :
    EntityGoal(mob, priority) {
    private val key = GoalKey.of(Mob::class.java, NamespacedKey(PluginInstance.instance, "move_to_target"))
    override fun getKey() = key
    override fun getTypes(): EnumSet<GoalType> = EnumSet.of(GoalType.MOVE, GoalType.LOOK)
    override fun shouldActivate() = mob.location.distanceSquared(target) > squaredMaxDistance
    override fun stop() = mob.pathfinder.stopPathfinding()
    override fun tick() { mob.pathfinder.moveTo(target) }

    override val type = ConfigurationSerializableDataType(MoveToTargetGoal::class.java)
    override val primitives: Map<String, Any> = mapOf("id" to mob.uniqueId, "target" to target, "distance" to squaredMaxDistance,
        "priority" to priority)

    constructor(map: MutableMap<String, Any>) : this(
        Bukkit.getEntity(map["id"] as UUID) as Mob,
        map["target"] as Location,
        map["distance"] as Double,
        map["priority"] as Int
    )
}