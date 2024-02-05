package io.github.fhanko

import com.jeff_media.morepersistentdatatypes.datatypes.serializable.ConfigurationSerializableArrayDataType
import io.github.fhanko.goals.EntityGoal
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftVillager
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.entity.Villager
import org.bukkit.persistence.PersistentDataType


abstract class EntityBase(val id: Int, val type: EntityType) {
    companion object {
        protected val key = EntityData(PersistentDataType.INTEGER, "id")
        val goals = EntityData(ConfigurationSerializableArrayDataType(Array<EntityGoal>::class.java), "goals")
        val entityMap = mutableMapOf<Int, EntityBase>()

        fun get(entity: Entity?): EntityBase? {
            val id = key.get(entity) ?: return null
            return entityMap[id]
        }

        fun addGoal(mob: Mob, goal: EntityGoal) {
            Bukkit.getMobGoals().addGoal(mob, goal.priority, goal)
            var goalArray = goals.get(mob) ?: arrayOf()
            goalArray += goal
            goals.set(mob, goalArray)
        }

        fun removeAI(entity: Entity) {
            if (entity is Mob) {
                Bukkit.getMobGoals().removeAllGoals(entity)
            }
            if (entity is Villager) {
                val nmsEntity = (entity as CraftVillager).handle
                nmsEntity.brain.removeAllBehaviors()
            }
        }
    }

    init {
        entityMap[id] = this
    }

    fun spawn(location: Location): Entity {
        val entity = location.world.spawnEntity(location, type)
        entity.isPersistent = true
        removeAI(entity)
        key.set(entity, id)
        onSpawn(entity)
        return entity
    }

    protected open fun onSpawn(entity: Entity) { }
}