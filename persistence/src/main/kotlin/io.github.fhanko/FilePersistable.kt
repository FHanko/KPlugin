package io.github.fhanko

import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.Serializable
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

interface FilePersistable<T: Serializable>: Initializable {
    val saveFile: String
    fun save(item: T): Boolean {
        val out = BukkitObjectOutputStream(GZIPOutputStream(FileOutputStream(saveFile)))
        out.writeObject(item)
        out.close()
        return true
    }

    fun load(): T? {
        if (!File(saveFile).exists()) return null
        val item = BukkitObjectInputStream(GZIPInputStream(FileInputStream(saveFile)))
        @Suppress("UNCHECKED_CAST") val data: T = item.readObject() as T
        item.close()
        return data
    }
}