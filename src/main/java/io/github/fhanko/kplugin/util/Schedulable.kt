package io.github.fhanko.kplugin.util

import io.github.fhanko.kplugin.KPlugin
import org.bukkit.Bukkit

/**
 * Provides functions to run tasks at a later time or repeating.
 */
interface Schedulable {
    fun runSchedule(vararg params: Any) { }

    companion object {
        var scheduleId: MutableMap<String, Int> = mutableMapOf()
    }

    fun getHash(key: String): String = hash(key + hashCode())

    fun schedule(key: String, delayMilliseconds: Long, vararg params: Any) {
        scheduleId[getHash(key)] = Bukkit.getScheduler().runTaskLater(KPlugin.instance, Runnable { runSchedule(*params) }, delayMilliseconds / 50).taskId
    }

    fun scheduleRepeat(key: String, intervalMilliseconds: Long, vararg params: Any) {
        scheduleId[getHash(key)] = Bukkit.getScheduler().scheduleSyncRepeatingTask(KPlugin.instance, Runnable { runSchedule(*params) }, 0, intervalMilliseconds / 50)
    }

    fun cancelSchedule(key: String) {
        if (scheduleId.containsKey(getHash(key)))
            Bukkit.getScheduler().cancelTask(scheduleId[getHash(key)]!!)
    }
}