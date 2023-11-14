package io.github.fhanko.kplugin.util

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile

interface Initializable

/**
 * Initializes objects (avoids lazy initialization) for [Initializable] subtype objects.
 */
object Init {
    /**
     * This budget reflection is used because the reflection package comes with considerable file size increase.
     */
    private fun getClassNamesFromJarFile(givenFile: File?, prefix: String): Set<String> {
        val classNames: MutableSet<String> = HashSet()
        JarFile(givenFile).use { jarFile ->
            val e: Enumeration<JarEntry> = jarFile.entries()
            while (e.hasMoreElements()) {
                val jarEntry: JarEntry = e.nextElement()
                if (jarEntry.name.startsWith(prefix) && jarEntry.name.endsWith(".class") && !jarEntry.name.contains("$")) {
                    val className: String = jarEntry.name
                        .replace("/", ".")
                        .replace(".class", "")
                    classNames.add(className)
                }
            }
            return classNames
        }
    }

    fun initialize(plugin: JavaPlugin, prefix: String) {
        val cl = plugin::class.java.classLoader
        val regex = Regex("""file:/(.*)!""")
        val prefixS = prefix.replace(".", "/")
        cl.getResource(prefixS)?.apply {
            val filePath = regex.find(file)?.groups?.get(1)?.value ?: return@apply
            getClassNamesFromJarFile(File(filePath), prefixS).forEach { clazzName ->
                val clazz = Class.forName(clazzName)
                if (clazz.interfaces.contains(Initializable::class.java) && clazz.interfaces.contains(Listener::class.java)) {
                    Bukkit.getPluginManager().registerEvents(clazz.getField("INSTANCE").get(null) as Listener, plugin)
                }
            }
        }
    }
}