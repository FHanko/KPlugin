package io.github.fhanko.kplugin.util

import io.github.fhanko.kplugin.KPlugin
import org.bukkit.Bukkit

/**
 * Provides functions to run tasks at a later time or repeating.
 */
interface Schedulable {
    companion object {
        var scheduleId: MutableMap<String, Int> = mutableMapOf()
    }

    private fun getHash(key: String): String = hash(key + hashCode())

    /**
     * Schedules [action] for running after [delayMilliseconds]. Provide a unique [key] to enable cancellation of the scheduled task.
     */
    fun schedule(key: String, delayMilliseconds: Long, action: (List<Any>) -> Unit, vararg params: Any) {
        scheduleId[getHash(key)] = Bukkit.getScheduler().runTaskLater(KPlugin.instance, Runnable { action(params.asList()) }, delayMilliseconds / 50).taskId
    }

    /**
     * Schedules [action] for running every [intervalMilliseconds]. Provide a unique [key] to enable cancellation of the scheduled task.
     */
    fun scheduleRepeat(key: String, intervalMilliseconds: Long, action: (List<Any>) -> Unit, vararg params: Any) {
        scheduleId[getHash(key)] = Bukkit.getScheduler().scheduleSyncRepeatingTask(KPlugin.instance, Runnable { action(params.asList()) }, 0, intervalMilliseconds / 50)
    }

    /**
     * Cancels task associated with [key].
     */
    fun cancelSchedule(key: String) {
        if (scheduleId.containsKey(getHash(key)))
            Bukkit.getScheduler().cancelTask(scheduleId[getHash(key)]!!)
    }
}