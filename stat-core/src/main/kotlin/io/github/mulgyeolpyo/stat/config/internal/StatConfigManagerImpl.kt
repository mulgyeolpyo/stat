package io.github.mulgyeolpyo.stat.config.internal

import io.github.monun.tap.config.ConfigSupport
import io.github.mulgyeolpyo.stat.GlobalStatManager
import io.github.mulgyeolpyo.stat.config.StatConfig
import io.github.mulgyeolpyo.stat.config.StatConfigManager
import org.bukkit.Bukkit
import java.io.File

@Suppress("unused")
class StatConfigManagerImpl(
    private val manager: GlobalStatManager,
    private val dataFolder: File,
) : StatConfigManager {
    private val configs: MutableMap<String, StatConfig> = mutableMapOf()

    private fun requireValidStat(stat: String) {
        require(stat in this.manager.stats) { "Stat '$stat' does not exist." }
    }

    override fun unregister(stat: String) {
        this.requireValidStat(stat)
        val config = this.configs.remove(stat) ?: return
        this.save(stat, config)
        Bukkit.getLogger().info { "[Mulgyeolpyo.Stat] Stat config manager for '$stat' has been unregistered." }
    }

    override fun get(stat: String): StatConfig {
        this.requireValidStat(stat)
        this.configs[stat]?.let { return it }
        return this.load(stat)
    }

    override fun set(
        stat: String,
        config: StatConfig,
    ) {
        this.requireValidStat(stat)
        this.configs[stat] = config
    }

    override fun load(stat: String): StatConfig {
        this.requireValidStat(stat)
        val config = StatConfig()
        ConfigSupport.compute(config, File(this.dataFolder, "stat.yml"), separateByClass = true).let {
            config.default = config.default
        }
        this.configs[stat] = config
        Bukkit.getLogger().info { "[Mulgyeolpyo.Stat] Stat '$stat' config has been loaded." }
        return config
    }

    override fun load() {
        for (stat in this.manager.stats) {
            this.load(stat)
        }
    }

    private fun save(
        stat: String,
        config: StatConfig,
    ) {
        val file = File(this.dataFolder, "$stat.yml").apply { delete() }
        ConfigSupport.compute(config, file, separateByClass = true)
        Bukkit.getLogger().info { "[Mulgyeolpyo.Stat] Stat '$stat' config has been saved." }
    }

    override fun save(stat: String) {
        this.requireValidStat(stat)
        val config = this.configs[stat] ?: return
        this.save(stat, config)
    }

    override fun save() {
        val configs = this.configs.toMap()
        configs.forEach { (stat, config) ->
            this.save(stat, config)
        }
    }
}
