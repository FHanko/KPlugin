package io.github.fhanko

import com.destroystokyo.paper.entity.ai.Goal
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftVillager
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.entity.Villager
import org.bukkit.persistence.PersistentDataType


abstract class EntityBase(val id: Int, val type: EntityType) {
    protected val key = EntityData(PersistentDataType.INTEGER, "Id")

    companion object {
        @JvmStatic protected val idKey = NamespacedKey(PluginInstance.instance, "Id")
        val entityMap = mutableMapOf<Int, EntityBase>()

        fun get(entity: Entity?): EntityBase? {
            val id = entity?.persistentDataContainer?.get(idKey, PersistentDataType.INTEGER) ?: return null
            return entityMap[id]
        }
        fun addGoal(mob: Mob, goal: Goal<Mob>, priority: Int) { Bukkit.getMobGoals().addGoal(mob, priority, goal) }
    }

    init {
        entityMap[id] = this
    }

    final fun spawn(location: Location): Entity {
        val entity = location.world.spawnEntity(location, type)
        entity.isPersistent = true
        // Remove AI
        if (entity is Mob) {
            Bukkit.getMobGoals().removeAllGoals(entity)
        }
        if (entity is Villager) {
            val nmsEntity = (entity as CraftVillager).handle
            nmsEntity.brain.removeAllBehaviors()
        }
        key.set(entity, id)
        onSpawn(entity)
        return entity
    }

    protected open fun onSpawn(entity: Entity) { }
}