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
    override val dataFolder: File = dataFolder
        get() =
            File(field, "stat").apply {
                if (!exists()) {
                    mkdirs()
                }
            }

    override val config: StatConfigManager = StatConfigManager.create(this, dataFolder)
    override val event: StatEventManager = StatEventManager.create(this, this.plugin, dataFolder)

    private val _stats: MutableList<String> = mutableListOf()
    private val _players: MutableMap<UUID, StatManager> = mutableMapOf()

    override val stats: List<String>
        get() = this._stats.toList()
    override val players: Map<UUID, StatManager>
        get() = this._players.toMap()

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

    override fun create(playerId: UUID): StatManager =
        this._players[playerId] ?: StatManagerImpl(this, playerId).also {
            this._players[playerId] = it
        }

    override fun create(player: Player): StatManager = this.create(player.uniqueId)

    override fun load() {
        val statFolders =
            this.dataFolder
                .walkTopDown()
                .filter { it.isDirectory && it != this.dataFolder }
        for (statFolder in statFolders) {
            val stat = statFolder.name.lowercase()
            this.register(stat)
        }
    }
}
