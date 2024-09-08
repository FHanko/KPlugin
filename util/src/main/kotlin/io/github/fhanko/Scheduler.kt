package io.github.fhanko

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
            Bukkit.getScheduler().scheduleSyncRepeatingTask(PluginInstance.instance, { runTick(tick) }, 0, tick)
        }

        schedules[tick]!![key] = schedule
    }

    fun remove(key: String) {
        now { schedules.values.forEach { m -> m.remove(key) } }
    }

    private fun runTick(tick: Long) {
        schedules[tick]?.values?.forEach { s -> s.action(s.params) }
    }

    /**
     * Schedules [action] for running on the next server tick.
     */
    fun nextTick(action: (List<Any>) -> Unit, vararg params: Any) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(PluginInstance.instance) { action(params.asList()) }
    }

    /**
     * Runs the given task on the scheduler.
     */
    fun now(action: () -> Unit) {
        Bukkit.getScheduler().runTask(PluginInstance.instance, Runnable { action() } )
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
            Bukkit.getScheduler().runTaskLater(PluginInstance.instance, Runnable { action(params.asList()) }, delayTicks).taskId
        )
    }

    /**
     * Schedules [action] for running every [intervalTicks]. Provide a unique [key] to enable cancellation of the scheduled task.
     */
    fun scheduleRepeat(key: String, intervalTicks: Long, action: (List<Any>) -> Unit, vararg params: Any) {
        Scheduler.add(intervalTicks, getHash(key), Schedule(action, params.asList()))
    }

    /**
     * Schedules [action] for running every [intervalTicks] for the next [durationTicks].
     * Provide a unique [key] to enable cancellation of the scheduled task.
     */
    fun scheduleTimer(key: String, intervalTicks: Long, durationTicks: Long, action: (List<Any>) -> Unit, vararg params: Any) {
        schedule(key, durationTicks, { _ -> scheduleCancel(key) })
        scheduleRepeat(key, intervalTicks, action, params)
    }

    /**
     * Cancels delayed and repeated tasks associated with [key].
     */
    fun scheduleCancel(key: String) {
        Scheduler.remove(getHash(key))
        Scheduler.delayedSchedules.find { p -> p.first == getHash(key) }?.also { Bukkit.getScheduler().cancelTask(it.second) }

        if (Scheduler.delayedSchedules.size > 128) Scheduler.delayedSchedules.removeAll { !Bukkit.getScheduler().isQueued(it.second) }
    }
}