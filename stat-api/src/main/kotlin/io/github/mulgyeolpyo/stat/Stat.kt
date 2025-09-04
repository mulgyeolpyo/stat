package io.github.mulgyeolpyo.stat

import io.github.mulgyeolpyo.stat.config.StatConfig
import kotlin.math.max
import kotlin.random.Random

@Suppress("unused")
class Stat<T : StatConfig>(
    val config: T,
) {
    val random: Long
        get() = max(this.config.default + ((Random.nextFloat() * 2 * this.config.random - config.random) * Random.nextFloat()).toLong(), 0L)

    fun level(exp: Long): Int {
        val levelsList = this.config.levels
        var low = 0
        var high = levelsList.size - 1
        var result = 0

        while (low <= high) {
            val mid = (low + high).ushr(1)

            if (levelsList[mid] <= exp) {
                result = mid
                low = mid + 1
            } else {
                high = mid - 1
            }
        }

        return result
    }
}
