package io.github.mulgyeolpyo.stat.config

import io.github.monun.tap.config.Config
import io.github.monun.tap.config.RangeInt
import io.github.monun.tap.config.RangeLong
import io.github.mulgyeolpyo.stat.utili.add
import io.github.mulgyeolpyo.stat.utili.pow
import io.github.mulgyeolpyo.stat.utili.subtract
import kotlin.math.max
import kotlin.math.min

@Suppress("unused")
open class StatConfig {
    val name: String

    @Config
    val description: String = ""

    init {
        val statName = this::class.java.getAnnotation(StatName::class.java)

        require(statName != null) {
            "[Mulgyeolpyo.Stat] 스탯 설정 클래스에는 @StatName 어노테이션이 없어선 안됩니다."
        }

        this.name = statName.name
    }

    @Config
    @RangeLong(min = 0L)
    var default: Long = 0L
        set(value) {
            val maxRandomPositiveEstimate = value.add(this.random)
            val maxRandomNegativeEstimate = value.subtract(this.random)

            val isPositive = value >= 0
            val isPositiveStable = this.max <= maxRandomPositiveEstimate
            val isNegativeStable = maxRandomNegativeEstimate >= 0

            require(isPositive || isPositiveStable || isNegativeStable) {
                "[Mulgyeolpyo.Stat] 스탯 기본값이 올바르지 않습니다."
            }

            field = value
        }

    @Config
    @RangeLong(min = 0L)
    var random: Long = 0L
        set(value) {
            val maxRandomPositiveEstimate = this.default.add(value)
            val maxRandomNegativeEstimate = this.default.subtract(value)

            val isPositive = value >= 0
            val isPositiveStable = this.max <= maxRandomPositiveEstimate
            val isNegativeStable = maxRandomNegativeEstimate >= 0

            require(isPositive || isPositiveStable || isNegativeStable) {
                "[Mulgyeolpyo.Stat] 스탯 기본값의 범위가 올바르지 않습니다."
            }

            field = value
        }

    @Config
    @RangeInt(min = 1)
    var max: Int = 1
        set(value) {
            val maxRandomEstimate = this.default.add(this.random)
            val maxStat = this.calculateLevels(max = value).last()

            val isPositive = value >= 0
            val isStable = value <= maxRandomEstimate
            val isMaxExp = value <= maxStat

            require(isPositive || isStable || isMaxExp) {
                "[Mulgyeolpyo.Stat] 스탯 최대값이 올바르지 않습니다."
            }

            field = value

            if (this.privateLevels.size < value) {
                this.privateLevels = this.calculateLevels(max = value)
            } else {
                this.privateLevels = this.privateLevels.take(value)
            }
        }

    @Config
    @RangeInt(min = 0)
    var weight: Int = 2
        set(value) {
            val maxRandomEstimate = this.default.add(this.random)
            val maxStat = this.calculateLevels(weight = value.toLong()).last()

            val isPositive = value > 0
            val isStable = value <= maxRandomEstimate
            val isMaxExp = value <= maxStat

            require(isPositive || isStable || isMaxExp) {
                "[Mulgyeolpyo.Stat] 스탯 가중치가 올바르지 않습니다."
            }

            field = value

            this.privateLevels = this.calculateLevels(weight = value.toLong())
        }

    private var privateLevels: List<Long> = calculateLevels()

    val levels: List<Long>
        get() = privateLevels.toList()

    private fun calculateLevels(
        max: Int = this.max,
        weight: Long = this.weight.toLong(),
    ): List<Long> {
        val levels = mutableListOf<Long>()
        var level = 0

        while (level <= max) {
            levels.add(weight.pow(level))
            level += 1
        }

        return levels
    }

    fun initLevels() {
        this.privateLevels = this.calculateLevels()
    }
}

enum class StatConfigField {
    NAME,
    DEFAULT,
    RANDOM,
    MAX,
    WEIGHT,
    LEVELS,
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class StatName(
    val name: String,
)
