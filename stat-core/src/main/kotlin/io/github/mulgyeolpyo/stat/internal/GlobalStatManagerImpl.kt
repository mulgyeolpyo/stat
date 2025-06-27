package io.github.mulgyeolpyo.stat.internal

import io.github.mulgyeolpyo.stat.GlobalStatManager
import io.github.mulgyeolpyo.stat.StatManager
import io.github.mulgyeolpyo.stat.config.StatConfigManager
import io.github.mulgyeolpyo.stat.event.StatEventListener
import io.github.mulgyeolpyo.stat.event.StatEventManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.UUID

@Suppress("unused")
class GlobalStatManagerImpl(
    plugin: JavaPlugin? = null,
    dataFolder: File? = null,
) : GlobalStatManager {
    private val plugin: JavaPlugin = plugin ?: JavaPlugin.getProvidingPlugin(this.javaClass)
    override val config: StatConfigManager =
        if (dataFolder == null) {
            StatConfigManager.create(this, this.plugin)
        } else {
            StatConfigManager.create(this, dataFolder)
        }
    override val event: StatEventManager =
        if (dataFolder == null) {
            StatEventManager.create(this, this.plugin)
        } else {
            StatEventManager.create(this, this.plugin, dataFolder)
        }

    private val _stats: MutableList<String> = mutableListOf()

//    private val _configs: MutableMap<String, StatConfig> = mutableMapOf()
    private val _players: MutableMap<UUID, StatManager> = mutableMapOf()
//    private val _events: MutableMap<String, Class<out StatEventListener>> = mutableMapOf()
//    private val _listeners: MutableMap<String, StatEventListener> = mutableMapOf()

    override val stats: List<String>
        get() = this._stats.toList()

    // override val configs: Map<String, StatConfig> = this._configs.toMap()
    override val players: Map<UUID, StatManager>
        get() = this._players.toMap()
//    override val events: Map<String, Class<out StatEventListener>>
//        get() = this._events.toMap()
//    override val listeners: Map<String, StatEventListener>
//        get() = this._listeners.toMap()

    override val dataFolder: File =
        File((dataFolder ?: this.plugin.dataFolder), "stat").apply {
            if (!this.exists()) {
                this.mkdirs()
            }
        }

    init {
        Bukkit.getPluginManager().registerEvents(
            object : Listener {
                @EventHandler
                fun onDisable(event: PluginDisableEvent) {
                    this@GlobalStatManagerImpl.config.save()
                    this@GlobalStatManagerImpl._players.forEach { (playerId, manager) -> manager.save() }
                }

                @EventHandler
                fun onPlayerQuick(event: PlayerQuitEvent) {
                    val playerId = event.player.uniqueId
                    this@GlobalStatManagerImpl._players.remove(playerId).let { player ->
                        player?.save()
                    }
                }
            },
            this.plugin,
        )
    }

    override fun register(stat: String) {
        require(stat.isNotBlank()) { "스탯 이름에 공백이 포함될 수 없습니다." }
        require(stat.all { it.isLowerCase() }) { "스탯 이름에 대문자가 포함될 수 없습니다." }
        require(!this._stats.contains(stat)) { "이미 스탯 '$stat'이 존재합니다." }

        this._stats.add(stat)
        this.event.register(stat)
    }

    override fun register(
        stat: String,
        event: Class<out StatEventListener>,
    ) {
        this.register(stat)
        this.event.register(stat, event)
    }

    override fun register(event: Class<out StatEventListener>) {
        val stat = event.simpleName.removeSuffix("StatEvent").lowercase()
        this.register(stat, event)
    }

    override fun unregister(stat: String) {
        require(this._stats.contains(stat)) { "스탯 '$stat'이 존재하지 않습니다." }
        this._stats.remove(stat)

        this.config.unregister(stat)
        this.event.unregister(stat)
        this._players.forEach { (_, manager) -> manager.unregister(stat) }
    }

    override fun unregister(event: Class<out StatEventListener>) {
        val stat = event.simpleName.removeSuffix("StatEvent").lowercase()
        this.unregister(stat)
    }
//
//    override fun registerEvent(listener: Class<out StatEventListener>) {
//        val stat = listener.simpleName.removeSuffix("StatEvent").lowercase()
//        this.register(stat)
//        this.registerEvent(stat, listener)
//        this.registerListener(stat)
//    }
//
//    override fun unregisterEvent(stat: String) {
//        require(this._events.containsKey(stat)) { "스탯 '$stat'에 대한 이벤트가 존재하지 않습니다." }
//        this._events.remove(stat)
//        this.unregisterListener(stat)
//    }
//
//    override fun unregisterEvent(listener: Class<out StatEventListener>) {
//        val stat = listener.simpleName.removeSuffix("StatEvent").lowercase()
//        this.unregister(stat)
//    }
//
//    override fun registerListener(stat: String) {
//        require(this._stats.contains(stat)) { "스탯 '$stat'이 존재하지 않습니다." }
//        require(this._events.containsKey(stat)) { "스탯 '$stat'에 대한 이벤트가 존재하지 않습니다." }
//
//        try {
//            val listenerClass = this._events[stat]
//            val listenerInstance: StatEventListener =
//                try {
//                    listenerClass!!
//                        .getDeclaredConstructor(GlobalStatManager::class.java)
//                        .newInstance(this)
//                } catch (e: NoSuchMethodException) {
//                    try {
//                        listenerClass!!
//                            .getDeclaredConstructor(String::class.java, GlobalStatManager::class.java)
//                            .newInstance(stat, this)
//                    } catch (e: NoSuchMethodException) {
//                        throw IllegalArgumentException("스탯 '$stat'에 대한 이벤트 리스너 생성을 실패했습니다.", e)
//                    }
//                }
//
//            this._listeners[stat] = listenerInstance
//            this.plugin.server.pluginManager
//                .registerEvents(this._listeners[stat]!!, this.plugin)
//        } catch (e: Exception) {
//            throw IllegalArgumentException("스탯 '$stat'에 대한 이벤트 리스너 생성을 실패했습니다.", e)
//        }
//    }
//
//    override fun unregisterListener(stat: String) {
//        require(this._listeners.contains(stat)) { "스탯 '$stat'에 대한 이벤트 리스너가 존재하지 않습니다." }
//        // 아래 코드 한 줄로도 가능하긴 해요.
//        val listener = this._listeners.remove(stat)
//        HandlerList.unregisterAll(listener!!)
//    }

    override fun create(playerId: UUID): StatManager =
        this._players[playerId] ?: StatManagerImpl(this, playerId).also {
            this._players[playerId] = it
        }

    override fun create(player: Player): StatManager = this.create(player.uniqueId)

//    override fun loadEvent(stat: String) {
//        require(stat in this._stats) { "스탯 '$stat'이 존재하지 않습니다." }
//
//        val jarFile = dataFolder.resolve(stat).resolve("event.jar")
//        if (!jarFile.exists()) return
//
//        URLClassLoader(arrayOf(jarFile.toURI().toURL()), javaClass.classLoader).use { classLoader ->
//            JarFile(jarFile).use { jar ->
//                jar
//                    .entries()
//                    .asSequence()
//                    .filter { !it.isDirectory && it.name.endsWith(".class") }
//                    .map { it.name.replace('/', '.').removeSuffix(".class") }
//                    .mapNotNull { className ->
//                        try {
//                            val clazz = classLoader.loadClass(className)
//                            if (StatEventListener::class.java.isAssignableFrom(clazz) &&
//                                !Modifier.isAbstract(clazz.modifiers) &&
//                                !clazz.isInterface
//                            ) {
//                                clazz.asSubclass(StatEventListener::class.java)
//                            } else {
//                                null
//                            }
//                        } catch (e: ClassNotFoundException) {
//                            null
//                        } catch (e: NoClassDefFoundError) {
//                            null
//                        }
//                    }.forEach(::registerEvent)
//            }
//        }
//    }
//
//    override fun loadEvent() {
//        for (stat in this._stats) {
//            this.loadEvent(stat)
//        }
//    }

//    override fun load(stat: String) {
//    }

    override fun load() {
        val statFolders =
            this.dataFolder
                .walkTopDown()
                .filter { it.isDirectory && it != this.dataFolder }
        for (statFolder in statFolders) {
            val stat = statFolder.name.lowercase()
            this.register(stat)
//            this.config.load(stat)
//            this.event.register(stat)
        }
    }
}
