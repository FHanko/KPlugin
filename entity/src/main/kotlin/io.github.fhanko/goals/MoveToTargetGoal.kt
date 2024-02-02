package io.github.fhanko.goals

import com.destroystokyo.paper.entity.ai.Goal
import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.GoalType
import io.github.fhanko.PluginInstance
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Mob
import java.util.*

class MoveToTargetGoal(private val mob: Mob, private val target: Location, private val squaredMaxDistance: Double) : Goal<Mob> {
    private val key = GoalKey.of(Mob::class.java, NamespacedKey(PluginInstance.instance, "move_to_target"))
    override fun getKey() = key
    override fun getTypes(): EnumSet<GoalType> = EnumSet.of(GoalType.MOVE, GoalType.LOOK)
    override fun shouldActivate() = mob.location.distanceSquared(target) > squaredMaxDistance
    override fun stop() = mob.pathfinder.stopPathfinding()
    override fun tick() { mob.pathfinder.moveTo(target) }
}