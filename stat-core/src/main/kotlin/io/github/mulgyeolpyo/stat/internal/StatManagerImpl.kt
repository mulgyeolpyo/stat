package io.github.mulgyeolpyo.stat.internal

import io.github.monun.tap.data.persistentData
import io.github.mulgyeolpyo.stat.StatManager
import org.bukkit.Bukkit
import org.bukkit.persistence.PersistentDataType
import java.util.UUID
import kotlin.synchronized

@Suppress("unused")
class StatManagerImpl(
    private val manager: GlobalStatManagerImpl,
    private val playerId: UUID,
) : StatManager {
    private val lock = Any()

    private val exps: MutableMap<String, Long> = mutableMapOf()
    private val levels: MutableMap<String, Int> = mutableMapOf()

    private fun requireValidStat(stat: String) {
        require(stat in this.manager.stats) { "StatError: name '$stat' is not registered." }
    }

    override fun unregister(stat: String) {
        this.requireValidStat(stat)
        synchronized(this.lock) {
            this.exps.remove(stat)
            this.levels.remove(stat)
        }
    }

    override fun get(stat: String): Long {
        this.requireValidStat(stat)
        synchronized(this.lock) {
            this.exps[stat]?.let { return it }
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
        value: Long,
    ) {
        this.requireValidStat(stat)
        synchronized(this.lock) {
            this.exps[stat] = value
        }
    }

    override fun set(
        stat: String,
        value: Int,
    ) = this.set(stat, value.toLong())

    override fun increment(
        stat: String,
        value: Long,
    ): Long {
        this.requireValidStat(stat)
        val value =
            synchronized(this.lock) {
                val value = this.get(stat) + value
                this.exps[stat] = value
                value
            }
        return value
    }

    override fun increment(
        stat: String,
        value: Int,
    ): Long = this.increment(stat, value.toLong())

    override fun decrement(
        stat: String,
        value: Long,
    ): Long {
        this.requireValidStat(stat)
        val value =
            synchronized(this.lock) {
                val value = this.get(stat) - value
                this.exps[stat] = value
                value
            }
        return value
    }

    override fun decrement(
        stat: String,
        value: Int,
    ): Long = this.decrement(stat, value.toLong())

    override fun load(stat: String): Long {
        this.requireValidStat(stat)
        val config = this.manager.config.get(stat)
        val value =
            Bukkit
                .getPlayer(this.playerId)
                ?.persistentData
                ?.get(stat, PersistentDataType.LONG) ?: config.random.toLong()
        synchronized(this.lock) {
            this.exps[stat] = value
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
            ?.set(stat, PersistentDataType.LONG, value)
    }

    override fun save() {
        val exps = this.exps.keys.toList()
        for (exp in exps) {
            this.save(exp)
        }
    }
}
