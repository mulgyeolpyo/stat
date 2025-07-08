package io.github.mulgyeolpyo.stat.event.internal

import io.github.mulgyeolpyo.stat.GlobalStatManager
import io.github.mulgyeolpyo.stat.event.StatEventListener
import io.github.mulgyeolpyo.stat.event.StatEventManager
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.lang.reflect.Modifier
import java.net.URLClassLoader
import java.util.jar.JarFile

@Suppress("unused")
class StatEventManagerImpl(
    private val manager: GlobalStatManager,
    private val plugin: JavaPlugin,
    private val dataFolder: File,
) : StatEventManager {
    private val events = mutableMapOf<String, Class<out StatEventListener>>()
    private val listeners = mutableMapOf<String, StatEventListener>()

    private fun requireValidStat(stat: String) {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }
    }

    override fun register(stat: String) {
        this.requireValidStat(stat)
        this.load(stat)
    }

    override fun register(
        stat: String,
        event: Class<out StatEventListener>,
    ) {
        this.requireValidStat(stat)

        this.events[stat] = event
        this.enable(stat, event)
    }

    override fun register(event: Class<out StatEventListener>) {
        val stat = event.simpleName.removeSuffix("StatEvent").lowercase()
        this.register(stat, event)
    }

    override fun unregister(stat: String) {
        this.requireValidStat(stat)
        this.disable(stat)
    }

    override fun enable(stat: String) {
        this.requireValidStat(stat)
        val event = this.events[stat] ?: return
        this.enable(stat, event)
    }

    private fun enable(
        stat: String,
        event: Class<out StatEventListener>,
    ) {
        try {
            val listener: StatEventListener =
                try {
                    event
                        .getDeclaredConstructor(GlobalStatManager::class.java)
                        .newInstance(this.manager)
                } catch (e: NoSuchMethodException) {
                    throw IllegalArgumentException("스탯 '$stat'에 대한 이벤트 리스너 생성을 실패했습니다.", e)
                }
            this.listeners[stat] = listener
            this.plugin.server.pluginManager
                .registerEvents(listener, this.plugin)
        } catch (e: Exception) {
            throw IllegalArgumentException("스탯 '$stat'에 대한 이벤트 리스너 생성을 실패했습니다.", e)
        }
    }

    private fun enable(event: Class<out StatEventListener>) {
        val stat = event.simpleName.removeSuffix("StatEvent").lowercase()
        this.enable(stat, event)
    }

    override fun disable(stat: String) {
        this.requireValidStat(stat)
        val listener = this.listeners.remove(stat) ?: return
        this.disable(listener)
    }

    private fun disable(listener: StatEventListener) {
        HandlerList.unregisterAll(listener)
    }

    override fun load(stat: String): Class<out StatEventListener>? {
        this.requireValidStat(stat)

        val jarFile = File(this.dataFolder, stat).resolve("event.jar")
        if (!jarFile.exists()) {
            return null
        }

        val eventListenerClassName = stat.replaceFirstChar { it.uppercaseChar() } + "EventListener"
        URLClassLoader(arrayOf(jarFile.toURI().toURL()), this.javaClass.classLoader).use { classLoader ->
            val event =
                JarFile(jarFile).use { jar ->
                    jar
                        .entries()
                        .asSequence()
                        .filter { !it.isDirectory && it.name.endsWith(".class") }
                        .map { it.name.replace('/', '.').removeSuffix(".class") }
                        .find { className ->
                            className.endsWith(eventListenerClassName) &&
                                runCatching {
                                    val clazz = classLoader.loadClass(className)
                                    StatEventListener::class.java.isAssignableFrom(clazz) &&
                                        !Modifier.isAbstract(clazz.modifiers) &&
                                        !clazz.isInterface
                                }.getOrDefault(false)
                        }?.let { className ->
                            classLoader.loadClass(className).asSubclass(StatEventListener::class.java)
                        }
                } ?: return null

            this.register(stat, event)
            return event
        }
    }

    override fun load() {
        for (stat in this.manager.stats) {
            this.load(stat)
        }
    }
}
