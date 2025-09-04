package io.github.mulgyeolpyo.stat

import io.github.mulgyeolpyo.stat.config.StatConfig
import org.bukkit.entity.Player
import java.util.UUID

interface StatManager<T : Stat<StatConfig>> {
    val config: T

    fun get(uniqueId: UUID): Long

    fun get(player: Player): Long

    fun clear(player: Player)

    fun clear(uniqueId: UUID)

    fun level(uniqueId: UUID): Int

    fun level(player: Player): Int

    fun set(
        uniqueId: UUID,
        value: Long,
    )

    fun set(
        player: Player,
        value: Long,
    )

    fun set(
        uniqueId: UUID,
        value: Int,
    )

    fun set(
        player: Player,
        value: Int,
    )

    fun increment(
        uniqueId: UUID,
        value: Long,
    )

    fun increment(
        player: Player,
        value: Long,
    )

    fun increment(
        uniqueId: UUID,
        value: Int,
    )

    fun increment(
        player: Player,
        value: Int,
    )

    fun decrement(
        uniqueId: UUID,
        value: Long,
    )

    fun decrement(
        player: Player,
        value: Long,
    )

    fun decrement(
        uniqueId: UUID,
        value: Int,
    )

    fun decrement(
        player: Player,
        value: Int,
    )

    fun save(
        player: Player,
        value: Long,
    )

    fun load(player: Player): Long

    fun save(
        uniqueId: UUID,
        value: Long,
    )

    fun load(uniqueId: UUID): Long

    fun save(player: Player)

    fun save(uniqueId: UUID)

    fun save()
}
