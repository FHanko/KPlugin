package com.github.fhanko.entity.goals

import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.GoalType
import com.jeff_media.morepersistentdatatypes.datatypes.serializable.ConfigurationSerializableDataType
import com.github.fhanko.util.PluginInstance
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Mob
import java.util.*

class LookAtPlayerGoal(mob: Mob, private val maxDistance: Double, priority: Int) : EntityGoal(mob, priority) {
    private val key = GoalKey.of(Mob::class.java, NamespacedKey(PluginInstance.instance, "look_at_player"))
    override fun getKey() = key
    override fun getTypes(): EnumSet<GoalType> = EnumSet.of(GoalType.LOOK)
    override fun shouldActivate() = !mob.world.getNearbyPlayers(mob.location, maxDistance).isEmpty()
    override fun tick() {
        val nearest = mob.world.getNearbyPlayers(mob.location, maxDistance)
            .map { p -> mob.location.distanceSquared(p.location) to p }
            .minBy { pair -> pair.first }.second
        mob.lookAt(nearest)
    }

    override val primitives: Map<String, Any> = mapOf("id" to mob.uniqueId, "distance" to maxDistance, "priority" to priority)

    constructor(map: MutableMap<String, Any>) : this(
        Bukkit.getEntity(map["id"] as UUID) as Mob,
        map["distance"] as Double,
        map["priority"] as Int
    )
}