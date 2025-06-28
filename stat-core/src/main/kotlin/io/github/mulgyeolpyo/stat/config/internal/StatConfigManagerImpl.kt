package io.github.mulgyeolpyo.stat.config.internal

import io.github.monun.tap.config.ConfigSupport
import io.github.mulgyeolpyo.stat.GlobalStatManager
import io.github.mulgyeolpyo.stat.config.StatConfig
import io.github.mulgyeolpyo.stat.config.StatConfigManager
import java.io.File

@Suppress("unused")
class StatConfigManagerImpl(
    private val manager: GlobalStatManager,
    private val dataFolder: File,
) : StatConfigManager {
    private val configs = mutableMapOf<String, StatConfig>()

    override fun unregister(stat: String) {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }
        if (stat in this.configs.keys) {
            this.save(stat)
        }

        this.configs.remove(stat)
    }

    override fun get(stat: String): StatConfig {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }
        return this.configs[stat] ?: load(stat)
    }

    override fun set(
        stat: String,
        config: StatConfig,
    ) {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }
        this.configs[stat] = config
    }

    override fun load(stat: String): StatConfig {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }

        val config = StatConfig()
        ConfigSupport.compute(config, File(this.dataFolder, "stat.yml"), separateByClass = true)
        config.default = config.default

        this.set(stat, config)
        return config
    }

    override fun load() {
        for (stat in this.manager.stats) {
            this.load(stat)
        }
    }

    override fun save(stat: String) {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }

        val config = this.configs[stat]
        val file = File(this.dataFolder, "$stat.yml").apply { delete() }
        ConfigSupport.compute(config!!, file, separateByClass = true)
    }

    override fun save() {
        for (stat in this.configs.keys) {
            this.save(stat)
        }
    }
}
