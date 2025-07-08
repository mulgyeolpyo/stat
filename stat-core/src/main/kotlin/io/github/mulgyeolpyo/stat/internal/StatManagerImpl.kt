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
    private val lock = Any()

    private val values: MutableMap<String, Float> = mutableMapOf()
    private val levels: MutableMap<String, Int> = mutableMapOf()

    private fun requireValidStat(stat: String) {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }
    }

    override fun unregister(stat: String) {
        this.requireValidStat(stat)
        synchronized(this.lock) {
            this.values.remove(stat)
            this.levels.remove(stat)
        }
    }

    override fun get(stat: String): Float {
        this.requireValidStat(stat)
        synchronized(this.lock) {
            this.values[stat]?.let { return it }
        }
        return this.load(stat)
    }

    override fun level(stat: String): Int {
        this.requireValidStat(stat)
        synchronized(this.lock) {
            this.levels[stat]?.let { return it }
        }

        val level =
            this.manager.config
                .get(stat)
                .level(this.get(stat))
        synchronized(this.lock) {
            this.levels[stat] = level
        }
        return level
    }

    override fun set(
        stat: String,
        value: Float,
    ) {
        this.requireValidStat(stat)
        synchronized(this.lock) {
            this.values[stat] = value
        }
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
        this.requireValidStat(stat)
        val value =
            synchronized(this.lock) {
                val result = operation(this.get(stat).toBigDecimal(), value.toBigDecimal()).toFloat()
                this.values[stat] = result
                result
            }
        return value
    }

    override fun increment(
        stat: String,
        value: Float,
    ): Float = this.updateStat(stat, value, BigDecimal::add)

    override fun increment(
        stat: String,
        value: Int,
    ): Float = this.increment(stat, value.toFloat())

    override fun decrement(
        stat: String,
        value: Float,
    ): Float = this.updateStat(stat, value, BigDecimal::subtract)

    override fun decrement(
        stat: String,
        value: Int,
    ): Float = this.decrement(stat, value.toFloat())

    override fun load(stat: String): Float {
        this.requireValidStat(stat)
        val config = this.manager.config.get(stat)
        val value =
            Bukkit
                .getPlayer(this.playerId)
                ?.persistentData
                ?.get(stat, PersistentDataType.FLOAT) ?: config.random
        synchronized(this.lock) {
            this.values[stat] = value
        }
        return value
    }

    override fun load() {
        for (stat in this.manager.stats) {
            this.load(stat)
        }
    }

    override fun save(stat: String) {
        this.requireValidStat(stat)
        val value = synchronized(this.lock) { this.get(stat) }
        Bukkit
            .getPlayer(this.playerId)
            ?.persistentData
            ?.set(stat, PersistentDataType.FLOAT, value)
    }

    override fun save() {
        val stats = this.values.keys.toList()
        for (stat in stats) {
            this.save(stat)
        }
    }
}
