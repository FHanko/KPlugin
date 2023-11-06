package io.github.fhanko.kplugin.util

import io.github.fhanko.kplugin.KPluginPostWorldSaveEvent
import io.github.fhanko.kplugin.KPluginPreWorldSaveEvent
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.world.WorldSaveEvent

interface PreWorldSaveable {
    fun preWorldSave(e: WorldSaveEvent) {}
    @EventHandler
    fun onWorldSave(kpe: KPluginPreWorldSaveEvent) {
        val e = kpe.baseEvent
        if (e.world.environment != World.Environment.NORMAL) return
        preWorldSave(e)
    }
}

interface PostWorldSaveable {
    fun postWorldSave(e: WorldSaveEvent) {}
    @EventHandler
    fun onWorldSave(kpe: KPluginPostWorldSaveEvent) {
        val e = kpe.baseEvent
        if (e.world.environment != World.Environment.NORMAL) return
        postWorldSave(e)
    }
}