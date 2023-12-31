package io.github.fhanko.kplugin.util

import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.Serializable
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

interface FilePersistable<T: Serializable>: Initializable {
    fun save(path: String, item: T): Boolean {
        val out = BukkitObjectOutputStream(GZIPOutputStream(FileOutputStream(path)))
        out.writeObject(item)
        out.close()
        return true
    }

    fun load(filePath: String): T? {
        if (!File(filePath).exists()) return null
        val item = BukkitObjectInputStream(GZIPInputStream(FileInputStream(filePath)))
        @Suppress("UNCHECKED_CAST") val data: T = item.readObject() as T
        item.close()
        return data
    }
}