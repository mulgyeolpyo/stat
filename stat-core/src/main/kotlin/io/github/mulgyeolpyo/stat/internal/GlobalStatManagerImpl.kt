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
    private val plugin: JavaPlugin,
    dataFolder: File,
) : GlobalStatManager {
    private val lock = Any()
    override val dataFolder = File(dataFolder, "stat")

    init {
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs()
        }
    }

    override val config = StatConfigManager.create(this, this.dataFolder)
    override val event = StatEventManager.create(this, this.plugin, this.dataFolder)

    private val _stats: MutableList<String> = mutableListOf()
    private val _players: MutableMap<UUID, StatManager> = mutableMapOf()

    override val stats: List<String>
        get() = _stats.toList()
    override val players: Map<UUID, StatManager>
        get() = _players.toMap()

    init {
        Bukkit.getPluginManager().registerEvents(
            object : Listener {
                @EventHandler
                fun onDisable(event: PluginDisableEvent) {
                    this@GlobalStatManagerImpl.config.save()
                    synchronized(this@GlobalStatManagerImpl.lock) {
                        this@GlobalStatManagerImpl._players.forEach { (_, manager) -> manager.save() }
                    }
                }

                @EventHandler
                fun onPlayerQuit(event: PlayerQuitEvent) {
                    val playerId = event.player.uniqueId
                    synchronized(this@GlobalStatManagerImpl.lock) {
                        this@GlobalStatManagerImpl._players.remove(playerId)?.save()
                    }
                }
            },
            this.plugin,
        )
    }

    private fun requireValidStat(stat: String) {
        require(stat.isNotBlank()) { "[Mulgyeolpyo.Stat] StatError: name for '$stat' must not be blank." }
        require(
            stat.all { it.isLowerCase() },
        ) { "[Mulgyeolpyo.Stat] StatError: Stat name for '$stat' must not be written with uppercase letters." }
        synchronized(this.lock) {
            require(stat !in this._stats) { "[Mulgyeolpyo.Stat] StatError: name '$stat' is already registered." }
            this._stats.add(stat)
        }
    }

    override fun register(stat: String) {
        this.requireValidStat(stat)
        this.event.register(stat)
        Bukkit.getLogger().info { "[Mulgyeolpyo.Stat] Successfully registered '$stat'." }
    }

    override fun register(
        stat: String,
        event: Class<out StatEventListener>,
    ) {
        this.requireValidStat(stat)
        this.event.register(stat, event)
        Bukkit.getLogger().info { "[Mulgyeolpyo.Stat] Successfully registered '$stat'." }
    }

    override fun register(event: Class<out StatEventListener>) {
        val stat = event.simpleName.removeSuffix("StatEvent").lowercase()
        this.register(stat, event)
    }

    override fun unregister(stat: String) {
        synchronized(this.lock) {
            require(this._stats.remove(stat)) { "[Mulgyeolpyo.Stat] StatError: name '$stat' is not registered." }
        }
        this.config.unregister(stat)
        this.event.unregister(stat)
        synchronized(this.lock) {
            this._players.forEach { (_, manager) -> manager.unregister(stat) }
        }
        Bukkit.getLogger().info { "[Mulgyeolpyo.Stat] Successfully unregistered '$stat'." }
    }

    override fun unregister(event: Class<out StatEventListener>) {
        val stat = event.simpleName.removeSuffix("StatEvent").lowercase()
        this.unregister(stat)
    }

    override fun create(playerId: UUID): StatManager {
        synchronized(this.lock) {
            return this._players[playerId] ?: StatManagerImpl(this, playerId).also {
                this._players[playerId] = it
            }
        }
    }

    override fun create(player: Player): StatManager = create(player.uniqueId)

    override fun load() {
        val statFolders =
            this.dataFolder
                .walkTopDown()
                .filter { it.isDirectory && it != this.dataFolder }
        for (statFolder in statFolders) {
            val stat = statFolder.name.lowercase()
            register(stat)
        }
    }
}
