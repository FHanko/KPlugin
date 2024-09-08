package com.github.fhanko.entity.entityhandler

import org.bukkit.event.entity.EntityDeathEvent

/**
 * Implementable for subclasses of EntityBase to override on death function.
 */
interface DeathHandler {
    fun death(e: EntityDeathEvent) { }
}