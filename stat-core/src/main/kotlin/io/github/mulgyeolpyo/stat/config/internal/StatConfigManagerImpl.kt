package io.github.mulgyeolpyo.stat.config.internal

import io.github.monun.tap.config.ConfigSupport
import io.github.mulgyeolpyo.stat.config.StatConfig
import io.github.mulgyeolpyo.stat.config.StatConfigField
import io.github.mulgyeolpyo.stat.config.StatConfigManager
import io.github.mulgyeolpyo.stat.loader.internal.LibraryLoader
import java.io.File

class StatConfigManagerImpl(
    private val path: File,
) : StatConfigManager {
    private val configs: MutableMap<String, StatConfig> = mutableMapOf()

    override fun <T : StatConfig> get(
        name: String,
        clazz: Class<T>,
    ): T {
        val config = configs[name]

        if (clazz.isInstance(config)) {
            @Suppress("UNCHECKED_CAST")
            return config as T
        }

        return this.load(name, clazz)
    }

    // get(name: String, clazz: Class<T>)를 참조할 순 있지만, 비효율적임.
    override fun get(name: String): StatConfig = configs[name] ?: this.load(name)

    override fun <T : StatConfig> get(clazz: Class<T>): T = this.get(clazz.simpleName.replace("StatConfig", ""), clazz)

    // = this.get(clazz.simpleName.replace("StatConfig", ""), clazz)

    override fun get(
        name: String,
        field: StatConfigField,
    ): Any {
        val config = this.get(name)

        return when (field) {
            StatConfigField.NAME -> config.name
            StatConfigField.DEFAULT -> config.default
            StatConfigField.RANDOM -> config.random
            StatConfigField.MAX -> config.default + config.random
            StatConfigField.WEIGHT -> config.weight
            StatConfigField.LEVELS -> config.levels
        }
    }

    override fun clear(name: String) {
        val config = this.configs.remove(name)

        if (config == null) {
            return
        }

        this.save(name, config)
    }

    override fun clear() {
        for (name in this.configs.keys) {
            val config = this.configs.remove(name)

            if (config == null) {
                continue
            }

            this.save(name, config)
        }
    }

    override fun set(
        name: String,
        config: StatConfig,
    ) {
        this.configs[name] = config
        this.save(name, config)
    }

    override fun set(config: StatConfig) = this.set(config.name, config)

    override fun set(
        name: String,
        field: StatConfigField,
        value: Any,
    ) {
        val config = this.get(name)

        try {
            when (field) {
                StatConfigField.DEFAULT -> config.default = value as Long
                StatConfigField.RANDOM -> config.random = value as Long
                StatConfigField.MAX -> config.max = value as Int
                StatConfigField.WEIGHT -> config.weight = value as Int
                else -> throw IllegalArgumentException("[Mulgyeolpyo.Stat] 스탯의 '$field'라는 값은 찾을/수정할 수 없습니다.")
            }
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("[Mulgyeolpyo.Stat] 스탯의 '$field'에 유효하지 않는 값을 넣을 순 없습니다.")
        }

        this.set(name, config)
    }

    override fun <T : StatConfig> load(
        name: String,
        clazz: Class<T>,
    ): T {
        val jar = this.path.resolve("$name.jar")
        val configClass = LibraryLoader.loadJarClass(jar, clazz)

        val config =
            requireNotNull(configClass) {
                "[Mulgyeolpyo.Stat] 지정된 경로에 '$name'이라는 스탯의 설정 클래스가 존재하지 않습니다."
            }.getDeclaredConstructor().newInstance()

        val path = this.path.resolve("config").resolve("$name.yml")

        ConfigSupport.compute(config, path, separateByClass = true)
        config.initLevels()

        return config
    }

    override fun load(name: String): StatConfig = this.load(name, StatConfig::class.java)

    override fun <T : StatConfig> load(clazz: Class<T>): T = this.load(clazz.simpleName.replace("StatConfig", ""), clazz)

    override fun save(
        name: String,
        config: StatConfig,
    ) {
        val path = this.path.resolve("config").resolve("$name.yml")

        path.delete()
        ConfigSupport.compute(config, path, separateByClass = true)
    }

    override fun save(name: String) = this.save(name, this.get(name))

    override fun save(config: StatConfig) = this.save(config.name, config)

    override fun save() {
        for ((name, config) in this.configs) {
            this.save(name, config)
        }
    }
}
