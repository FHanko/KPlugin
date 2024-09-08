package com.github.fhanko.persistence

import org.bukkit.configuration.file.YamlConfiguration

object YamlUtil {
    private var yamlConfig: YamlConfiguration = YamlConfiguration()

    fun objToString(obj: Any?): String {
        yamlConfig = YamlConfiguration()
        yamlConfig.set("obj", obj)
        return yamlConfig.saveToString()
    }

    fun <T> stringToObj(str: String?): T {
        yamlConfig = YamlConfiguration()
        yamlConfig.loadFromString(str!!)
        @Suppress("UNCHECKED_CAST") return yamlConfig.get("obj") as T
    }
}