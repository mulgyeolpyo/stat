package io.github.mulgyeolpyo.stat.utili

import kotlin.math.pow

/**
 * Returns the sum of this value and [other].
 *
 * If the result overflows the range of [Int], the original value is returned.
 *
 * @param other the value to add.
 * @return the sum, or this value if overflow occurs.
 *
 * Example:
 * ```
 * Int.MAX_VALUE.add(1) // returns Int.MAX_VALUE
 * ```
 */
@Suppress("unused")
fun Int.add(other: Int): Int {
    val sum = this + other
    val value =
        if ((this xor other) >= 0 && (this xor sum) < 0) {
            this
        } else {
            sum
        }
    return value
}

/**
 * Returns the difference of this value and [other].
 *
 * If the result overflows the range of [Int], the original value is returned.
 *
 * @param other the value to subtract.
 * @return the difference, or this value if overflow occurs.
 *
 * Example:
 * ```
 * Int.MIN_VALUE.subtract(1) // returns Int.MIN_VALUE
 * ```
 */
@Suppress("unused")
fun Int.subtract(other: Int): Int {
    val diff = this - other
    val value =
        if ((this xor other) < 0 && (this xor diff) < 0) {
            this
        } else {
            diff
        }
    return value
}

/**
 * Returns the product of this value and [other].
 *
 * If the result overflows the range of [Int], the original value is returned.
 *
 * @param other the value to multiply.
 * @return the product, or this value if overflow occurs.
 *
 * Example:
 * ```
 * Int.MAX_VALUE.multiply(2) // returns Int.MAX_VALUE
 * ```
 */
@Suppress("unused")
fun Int.multiply(other: Int): Int {
    val value =
        if (this == 0 || other == 0) {
            0
        } else {
            val product = this * other
            if (product / other != this) {
                this
            } else {
                product
            }
        }
    return value
}

/**
 * Returns the quotient of this value and [other].
 *
 * If [other] is zero, the original value is returned.
 *
 * @param other the value to divide by.
 * @return the quotient, or this value if division by zero occurs.
 *
 * Example:
 * ```
 * 10.divide(2) // returns 5
 * 10.divide(0) // returns 10
 * ```
 */
@Suppress("unused")
fun Int.divide(other: Int): Int {
    val value =
        if (other == 0) {
            this
        } else {
            this / other
        }
    return value
}

/**
 * Returns this value raised to the power of the [exponent] value.
 *
 * The result is calculated as this^exponent.
 *
 * If [exponent] is zero, the result is always 1.
 * If [exponent] is negative, an [IllegalArgumentException] is thrown.
 *
 * The calculation is performed using fast exponentiation.
 * If the result overflows the range of [Int], the original value is returned.
 *
 * @param exponent the exponent (must be non-negative)
 * @return the value of this number raised to the given [exponent].
 * @throws IllegalArgumentException if [exponent] is negative.
 *
 * Example:
 * ```
 * 2.pow(3) // returns 8
 * 10.pow(0) // returns 1
 * ```
 */
@Suppress("unused")
fun Int.pow(exponent: Int): Int {
    require(exponent >= 0) { "Exponent must be non-negative." }
    val max = Int.MAX_VALUE
    val min = Int.MIN_VALUE

    val double = this.toDouble().pow(exponent)
    if (double in min.toDouble()..max.toDouble()) {
        val value = double.toInt()
        return value
    }

    var result = 1
    var base = this
    var exp = exponent
    while (exp > 0) {
        if (exp % 2 == 1) {
            if (base != 0 && (result > max / base || result < min / base)) {
                val value = this
                return value
            }
            result *= base
        }
        exp /= 2
        if (exp > 0) {
            if (base != 0 && (base > max / base || base < min / base)) {
                val value = this
                return value
            }
            base *= base
        }
    }
    val value = result
    return value
}

/**
 * Returns the sum of this value and [other].
 *
 * If the result overflows the range of [Long], the original value is returned.
 *
 * @param other the value to add.
 * @return the sum, or this value if overflow occurs.
 *
 * Example:
 * ```
 * Long.MAX_VALUE.add(1L) // returns Long.MAX_VALUE
 * ```
 */
@Suppress("unused")
fun Long.add(other: Long): Long {
    val sum = this + other
    val value =
        if ((this xor other) >= 0L && (this xor sum) < 0L) {
            this
        } else {
            sum
        }
    return value
}

/**
 * Returns the difference of this value and [other].
 *
 * If the result overflows the range of [Long], the original value is returned.
 *
 * @param other the value to subtract.
 * @return the difference, or this value if overflow occurs.
 *
 * Example:
 * ```
 * Long.MIN_VALUE.subtract(1L) // returns Long.MIN_VALUE
 * ```
 */
@Suppress("unused")
fun Long.subtract(other: Long): Long {
    val diff = this - other
    val value =
        if ((this xor other) < 0L && (this xor diff) < 0L) {
            this
        } else {
            diff
        }
    return value
}

/**
 * Returns the product of this value and [other].
 *
 * If the result overflows the range of [Long], the original value is returned.
 *
 * @param other the value to multiply.
 * @return the product, or this value if overflow occurs.
 *
 * Example:
 * ```
 * Long.MAX_VALUE.multiply(2L) // returns Long.MAX_VALUE
 * ```
 */
@Suppress("unused")
fun Long.multiply(other: Long): Long {
    val value =
        if (this == 0L || other == 0L) {
            0L
        } else {
            val product = this * other
            if (product / other != this) {
                this
            } else {
                product
            }
        }
    return value
}

/**
 * Returns the quotient of this value and [other].
 *
 * If [other] is zero, the original value is returned.
 *
 * @param other the value to divide by.
 * @return the quotient, or this value if division by zero occurs.
 *
 * Example:
 * ```
 * 10L.divide(2L) // returns 5
 * 10L.divide(0L) // returns 10
 * ```
 */
@Suppress("unused")
fun Long.divide(other: Long): Long {
    val value =
        if (other == 0L) {
            this
        } else {
            this / other
        }
    return value
}

/**
 * Returns this value raised to the power of the [exponent] value.
 *
 * The result is calculated as this^exponent.
 *
 * If [exponent] is zero, the result is always 1.
 * If [exponent] is negative, an [IllegalArgumentException] is thrown.
 *
 * The calculation is performed using fast exponentiation.
 * If the result overflows the range of [Long], the original value is returned.
 *
 * @param exponent the exponent (must be non-negative)
 * @return the value of this number raised to the given [exponent].
 * @throws IllegalArgumentException if [exponent] is negative.
 *
 * Example:
 * ```
 * 2L.pow(3) // returns 8
 * 10L.pow(0) // returns 1
 * ```
 */
@Suppress("unused")
fun Long.pow(exponent: Int): Long {
    require(exponent >= 0) { "Exponent must be non-negative." }
    val max = Long.MAX_VALUE
    val min = Long.MIN_VALUE

    val double = this.toDouble().pow(exponent)
    if (double in min.toDouble()..max.toDouble()) {
        val value = double.toLong()
        return value
    }

    var result = 1L
    var base = this
    var exp = exponent
    while (exp > 0) {
        if (exp % 2 == 1) {
            if (base != 0L && (result > max / base || result < min / base)) {
                val value = this
                return value
            }
            result *= base
        }
        exp /= 2
        if (exp > 0) {
            if (base != 0L && (base > max / base || base < min / base)) {
                val value = this
                return value
            }
            base *= base
        }
    }
    val value = result
    return value
}
