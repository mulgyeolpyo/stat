package io.github.mulgyeolpyo.stat.internal

import io.github.monun.tap.data.persistentData
import io.github.mulgyeolpyo.stat.Stat
import io.github.mulgyeolpyo.stat.StatManager
import io.github.mulgyeolpyo.stat.config.StatConfig
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.util.UUID

class StatManagerImpl<T : Stat<StatConfig>>(
    public val stat: T,
) : StatManager<T> {
    private val cache: MutableMap<UUID, Long> = mutableMapOf()

    override fun get(uniqueId: UUID): Long = this.cache[uniqueId] ?: this.load(uniqueId)

    override fun get(player: Player): Long = this.get(player.uniqueId)

    override fun clear(uniqueId: UUID) {
    }

    override fun load(player: Player): Long {
        val value = player.persistentData[stat.config.name, PersistentDataType.LONG] ?: 0
        this.cache[player.uniqueId] = value

        return value
    }

    override fun load(uniqueId: UUID): Long {
        val player =
            Bukkit
                .getPlayer(uniqueId) ?: return 0L

        return this.load(player)
    }

    override fun save(
        player: Player,
        value: Long,
    ) {
        player.persistentData[stat.config.name, PersistentDataType.LONG] = value
    }

    override fun save(player: Player) = this.save(player, this.get(player))

    override fun save(
        uniqueId: UUID,
        value: Long,
    ) {
        val player =
            Bukkit
                .getPlayer(uniqueId) ?: return

        this.save(player, value)
    }

    override fun save(uniqueId: UUID) = this.save(uniqueId, this.get(uniqueId))

    override fun save() {
        this.cache
    }
}
