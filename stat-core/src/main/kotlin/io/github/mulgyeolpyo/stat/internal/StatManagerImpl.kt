package io.github.mulgyeolpyo.stat.internal

import io.github.monun.tap.data.persistentData
import io.github.mulgyeolpyo.stat.StatManager
import org.bukkit.Bukkit
import org.bukkit.persistence.PersistentDataType
import java.math.BigDecimal
import java.util.UUID

@Suppress("unused")
class StatManagerImpl(
    private val manager: GlobalStatManagerImpl,
    private val playerId: UUID,
) : StatManager {
    private val values = mutableMapOf<String, Float>()
    private val levels = mutableMapOf<String, Int>()

    init {
        this.load()
    }

    override fun unregister(stat: String) {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }
        if (stat in this.values.keys) {
            this.save(stat)
        }

        this.values.remove(stat)
        this.levels.remove(stat)
    }

    override fun get(stat: String): Float {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }
        return this.values[stat] ?: load(stat)
    }

    override fun level(stat: String): Int {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }
        return this.levels[stat]
            ?: this.manager.config
                .get(stat)
                .level(this.get(stat))
                .also { level -> this.levels[stat] = level }
    }

    override fun set(
        stat: String,
        value: Float,
    ) {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }
        this.values[stat] = value
    }

    override fun set(
        stat: String,
        value: Int,
    ) = this.set(stat, value.toFloat())

    private fun updateStat(
        stat: String,
        value: Float,
        operation: (BigDecimal, BigDecimal) -> BigDecimal,
    ): Float {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }

        val result = operation(this.get(stat).toBigDecimal(), value.toBigDecimal()).toFloat()
        this.values[stat] = result
        return result
    }

    override fun increment(
        stat: String,
        value: Float,
    ): Float = updateStat(stat, value, BigDecimal::add)

    override fun increment(
        stat: String,
        value: Int,
    ): Float = increment(stat, value.toFloat())

    override fun decrement(
        stat: String,
        value: Float,
    ): Float = updateStat(stat, value, BigDecimal::subtract)

    override fun decrement(
        stat: String,
        value: Int,
    ): Float = decrement(stat, value.toFloat())

    override fun load(stat: String): Float {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }

        val config = this.manager.config.get(stat)
        val value =
            Bukkit
                .getPlayer(playerId)
                ?.persistentData
                ?.get(stat, PersistentDataType.FLOAT) ?: config.random
        this.values[stat] = value
        return value
    }

    override fun load() {
        for (stat in this.manager.stats) {
            this.load(stat)
        }
    }

    override fun save(stat: String) {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }
        val value = this.get(stat)
        Bukkit
            .getPlayer(playerId)
            ?.persistentData
            ?.set(stat, PersistentDataType.FLOAT, value)
    }

    override fun save() {
        for (stat in this.values.keys) {
            this.save(stat)
        }
    }
}
