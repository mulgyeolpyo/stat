package io.github.mulgyeolpyo.stat.config.internal

import io.github.monun.tap.config.ConfigSupport
import io.github.mulgyeolpyo.stat.GlobalStatManager
import io.github.mulgyeolpyo.stat.config.StatConfig
import io.github.mulgyeolpyo.stat.config.StatConfigManager
import java.io.File

@Suppress("unused")
class StatConfigManagerImpl(
    private val manager: GlobalStatManager,
    dataFolder: File? = null,
) : StatConfigManager {
    private val configs = mutableMapOf<String, StatConfig>()
    private val dataFolder: File =
        dataFolder.let {
            if (it == null) {
                return@let this@StatConfigManagerImpl.manager.dataFolder
            }

            return@let File(it, "stat").apply {
                if (!this.exists()) {
                    this.mkdirs()
                }
            }
        }

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
        ConfigSupport.compute(config, File(File(this.dataFolder, stat), "stat.yml"), separateByClass = true)
        config.default = config.default

        // this.configs[stat] = config
        this.set(stat, config)
        return config
    }

    override fun load() {
        for (stat in this.manager.stats) {
            this.load(stat)
//            val config = StatConfig()
//            ConfigSupport.compute(config, File(this.dataFolder, "$stat.yml"), separateByClass = true)
//            config.levels = config.calculateLevels()
//
//            this.stats[stat] = config
        }
    }

    override fun save(stat: String) {
        require(stat in this.manager.stats) { "스탯 '$stat'이 존재하지 않습니다." }

        val file = File(File(this.dataFolder, stat), "$stat.yml")
        file.delete()

        val config = this.configs[stat]
        ConfigSupport.compute(config!!, file, separateByClass = true)
    }

    override fun save() {
        for (stat in this.configs.keys) {
            this.save(stat)
//            val file = File(this.dataFolder, "$stat.yml")
//            file.delete()
//
//            ConfigSupport.compute(config, file, separateByClass = true)
        }
    }
}
