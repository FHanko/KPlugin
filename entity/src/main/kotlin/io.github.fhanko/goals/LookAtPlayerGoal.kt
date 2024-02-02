package io.github.fhanko.goals

import com.destroystokyo.paper.entity.ai.Goal
import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.GoalType
import io.github.fhanko.PluginInstance
import org.bukkit.NamespacedKey
import org.bukkit.entity.Mob
import java.util.*

class LookAtPlayerGoal(private val mob: Mob, private val maxDistance: Double) :
    Goal<Mob> {
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
}