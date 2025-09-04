package io.github.mulgyeolpyo.stat.config

interface StatConfigManager {
    fun <T : StatConfig> get(
        name: String,
        clazz: Class<T>,
    ): T

    // load << 즉, register를 내포한 코드
    fun get(name: String): StatConfig

    fun <T : StatConfig> get(clazz: Class<T>): T

    fun get(
        name: String,
        field: StatConfigField,
    ): Any

    // 이건 인스턴스 클리어. get에 맞는 remove 라는 이름도 고려하였지만 제거보단 정리에 가깝다고 생각함. (메모리 정리적 시스템?)
    fun clear(name: String)

    fun clear()

    fun set(
        name: String,
        config: StatConfig,
    )

    fun set(config: StatConfig)

    fun set(
        name: String,
        field: StatConfigField,
        value: Any,
    )

    fun <T : StatConfig> load(
        name: String,
        clazz: Class<T>,
    ): T

    fun load(name: String): StatConfig

    fun <T : StatConfig> load(clazz: Class<T>): T

    fun save(
        name: String,
        config: StatConfig,
    )

    fun save(name: String)

    fun save(config: StatConfig)

    fun save()
}
