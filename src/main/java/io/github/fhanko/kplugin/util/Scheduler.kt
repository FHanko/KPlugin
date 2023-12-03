package io.github.fhanko.kplugin.util

import io.github.fhanko.kplugin.KPlugin
import org.bukkit.Bukkit

data class Schedule(val action: (List<Any>) -> Unit, val params: List<Any>)

object Scheduler {
    var delayedSchedules = mutableListOf<Pair<String, Int>>()

    /**
     * Schedule queues by their tick intervals
     */
    private val schedules = mutableMapOf<Long, MutableMap<String, Schedule>>()

    fun add(tick: Long, key: String, schedule: Schedule) {
        if (!schedules.containsKey(tick)) {
            schedules[tick] = mutableMapOf()
            Bukkit.getScheduler().scheduleSyncRepeatingTask(KPlugin.instance, { runTick(tick) }, 0, tick)
        }

        schedules[tick]!![key] = schedule
    }

    fun remove(key: String) {
        Bukkit.getScheduler().runTask(KPlugin.instance, Runnable { schedules.values.forEach { m -> m.remove(key) } })
    }

    private fun runTick(tick: Long) {
        schedules[tick]?.values?.forEach { s -> s.action(s.params) }
    }

    /**
     * Schedules [action] for running on the next server tick.
     */
    fun nextTick(action: (List<Any>) -> Unit, vararg params: Any) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(KPlugin.instance) { action(params.asList()) }
    }
}

/**
 * Provides functions to run tasks at a later time or repeating.
 */
interface Schedulable {
    private fun getHash(key: String): String = hash(key + hashCode())

    /**
     * Schedules [action] for running after [delayTicks]. Provide a unique [key] to enable cancellation of the scheduled task.
     */
    fun schedule(key: String, delayTicks: Long, action: (List<Any>) -> Unit, vararg params: Any) {
        Scheduler.delayedSchedules.add(getHash(key) to
            Bukkit.getScheduler().runTaskLater(KPlugin.instance, Runnable { action(params.asList()) }, delayTicks).taskId
        )
    }

    /**
     * Schedules [action] for running every [intervalTicks]. Provide a unique [key] to enable cancellation of the scheduled task.
     */
    fun scheduleRepeat(key: String, intervalTicks: Long, action: (List<Any>) -> Unit, vararg params: Any) {
        Scheduler.add(intervalTicks, key, Schedule(action, params.asList()))
    }

    /**
     * Cancels delayed and repeated tasks associated with [key].
     */
    fun scheduleCancel(key: String) {
        Scheduler.remove(key)
        Scheduler.delayedSchedules.find { p -> p.first == key }?.also { Bukkit.getScheduler().cancelTask(it.second) }
    }
}