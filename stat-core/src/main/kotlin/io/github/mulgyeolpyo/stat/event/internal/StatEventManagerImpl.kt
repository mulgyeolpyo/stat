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

    override fun register(stat: String) {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }
        this.load(stat)
    }

    override fun register(
        stat: String,
        event: Class<out StatEventListener>,
    ) {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }

        this.events[stat] = event
        this.enable(stat, event)
    }

    override fun register(event: Class<out StatEventListener>) {
        val stat = event.simpleName.removeSuffix("StatEvent").lowercase()
        this.register(stat, event)
    }

    override fun unregister(stat: String) {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }
        this.events.remove(stat)
        this.disable(stat)
    }

    override fun enable(stat: String) {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }
        this.events[stat]?.let { this.enable(stat, it) }
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
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }
        this.listeners.remove(stat)?.let { listener ->
            this.disable(listener)
        }
    }

    private fun disable(listener: StatEventListener) {
        HandlerList.unregisterAll(listener)
    }

    override fun load(stat: String): Class<out StatEventListener>? {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }

        val jarFile = dataFolder.resolve(stat).resolve("event.jar")
        if (jarFile.exists()) {
            URLClassLoader(arrayOf(jarFile.toURI().toURL()), javaClass.classLoader).use { classLoader ->
                val event =
                    JarFile(jarFile)
                        .use { jar ->
                            val eventListenerClassName = stat.replaceFirstChar { it.uppercaseChar() } + "EventListener"
                            return@use jar
                                .entries()
                                .asSequence()
                                .filter { !it.isDirectory && it.name.endsWith(".class") }
                                .map { it.name.replace('/', '.').removeSuffix(".class") }
                                .filter { className ->
                                    className.endsWith(eventListenerClassName)
                                }.mapNotNull { className ->
                                    try {
                                        val clazz = classLoader.loadClass(className)
                                        if (StatEventListener::class.java.isAssignableFrom(clazz) &&
                                            !Modifier.isAbstract(clazz.modifiers) &&
                                            !clazz.isInterface
                                        ) {
                                            clazz.asSubclass(StatEventListener::class.java)
                                        } else {
                                            null
                                        }
                                    } catch (e: Throwable) {
                                        null
                                    }
                                }
                        }.first()

                this.register(stat, event)
                this.enable(stat, event)
                return event
            }
        }

        return null
    }

    override fun load() {
        for (stat in this.manager.stats) {
            this.load(stat)
        }
    }
}
