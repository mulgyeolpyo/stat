package io.github.mulgyeolpyo.stat.config

import io.github.monun.tap.config.Config
import io.github.mulgyeolpyo.stat.utili.pow
import kotlin.random.Random

/**
 * Represents the configuration for a specific stat, such as its level range,
 * growth rate, and initial values.
 */
@Suppress("unused")
open class StatConfig {
    /**
     * The minimum possible level for the stat.
     * This defines the lower bound of the level range.
     */
    @Config
    var min: Int = 0
        set(value) {
            field = value
            this.levels = this.calculateLevels()
        }

    /**
     * The maximum possible level for the stat.
     * This defines the upper bound of the level range.
     */
    @Config
    var max: Int = 100
        set(value) {
            field = value
            this.levels = this.calculateLevels()
        }

    /**
     * The default value assigned to the stat upon creation.
     */
    @Config
    var default: Int = 0
        set(value) {
            field = value
            this.levels = this.calculateLevels()
        }

    /**
     * The range of random deviation to apply around the `default` value.
     * When accessed, this property returns a randomized value based on the formula:
     * `default ± (this.random * multiplier)`.
     */
    @Config
    var random: Int = 0
        get() = default + ((Random.nextFloat() * 2 * field - field) * Random.nextFloat()).toInt()

    /**
     * The growth factor that determines the required value for each level.
     * A higher weight means the required value for the next level increases more steeply,
     * making it harder to level up.
     */
    @Config
    var weight: Int = 2
        set(value) {
            field = value
            this.levels = this.calculateLevels()
        }

    /**
     * A pre-calculated list of value thresholds required for each level.
     * The index of the list corresponds to the level.
     */
    var levels: List<Int> = calculateLevels()

    private fun calculateLevels(): List<Int> {
        val levels = mutableListOf<Int>()

        var level = 10
        var next = this.weight
        var then = this.weight.pow(level)

        while (level <= this.max) {
            levels.add(next)
            next = then
            level++
            then = this.weight.pow(level)
        }

        return levels
    }

    /**
     * Determines the corresponding level for a given stat value.
     *
     * If the value is below the first threshold, it returns 0. If the value meets or
     * exceeds the final threshold, it returns the `max` level.
     *
     * @param value The stat value to check.
     * @return The calculated level, ranging from 0 to `max`.
     */
    fun level(value: Long): Int {
        if (value < this.levels.first()) return 0
        if (value >= this.levels.last()) return this.max

        for ((level, require) in this.levels.withIndex()) {
            if (value < require) {
                return level
            }
        }
        return this.max
    }
}
