package io.github.mulgyeolpyo.stat.utili

/**
 * Returns the sum of this value and [other].
 *
 * If the result overflows the range of [Int], returns [Int.MAX_VALUE] or [Int.MIN_VALUE] accordingly.
 *
 * @param other the value to add.
 * @return the sum, or the saturated value if overflow occurs.
 *
 * Example:
 * ```
 * Int.MAX_VALUE.add(1) // returns Int.MAX_VALUE
 * Int.MIN_VALUE.add(-1) // returns Int.MIN_VALUE
 * ```
 */
@Suppress("unused")
fun Int.add(other: Int): Int {
    val sum = this + other
    return if ((this xor other) >= 0 && (this xor sum) < 0) {
        if (this >= 0) Int.MAX_VALUE else Int.MIN_VALUE
    } else {
        sum
    }
}

/**
 * Returns the difference of this value and [other].
 *
 * If the result overflows the range of [Int], returns [Int.MAX_VALUE] or [Int.MIN_VALUE] accordingly.
 *
 * @param other the value to subtract.
 * @return the difference, or the saturated value if overflow occurs.
 *
 * Example:
 * ```
 * Int.MIN_VALUE.subtract(1) // returns Int.MIN_VALUE
 * Int.MAX_VALUE.subtract(-1) // returns Int.MAX_VALUE
 * ```
 */
@Suppress("unused")
fun Int.subtract(other: Int): Int {
    val diff = this - other
    return if ((this xor other) < 0 && (this xor diff) < 0) {
        if (this >= 0) Int.MAX_VALUE else Int.MIN_VALUE
    } else {
        diff
    }
}

/**
 * Returns the product of this value and [other].
 *
 * If the result overflows the range of [Int], returns [Int.MAX_VALUE] or [Int.MIN_VALUE] accordingly.
 *
 * @param other the value to multiply.
 * @return the product, or the saturated value if overflow occurs.
 *
 * Example:
 * ```
 * Int.MAX_VALUE.multiply(2) // returns Int.MAX_VALUE
 * Int.MIN_VALUE.multiply(2) // returns Int.MIN_VALUE
 * ```
 */
@Suppress("unused")
fun Int.multiply(other: Int): Int {
    if (this == 0 || other == 0) return 0
    val product = this * other
    return if (product / other != this) {
        if ((this > 0) == (other > 0)) Int.MAX_VALUE else Int.MIN_VALUE
    } else {
        product
    }
}

/**
 * Returns the quotient of this value and [other].
 *
 * If [other] is zero, returns this value.
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
fun Int.divide(other: Int): Int = if (other == 0) this else this / other

/**
 * Returns this value raised to the power of [exponent].
 *
 * If [exponent] is zero, returns 1.
 * If [exponent] is negative, throws [IllegalArgumentException].
 *
 * If the result overflows the range of [Int], returns [Int.MAX_VALUE] or [Int.MIN_VALUE] accordingly.
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
    var result = 1
    var base = this
    var exp = exponent

    while (exp > 0) {
        if ((exp and 1) != 0) {
            if (base != 0 && (result > Int.MAX_VALUE / base || result < Int.MIN_VALUE / base)) {
                return if ((this > 0) || exponent % 2 == 0) Int.MAX_VALUE else Int.MIN_VALUE
            }
            result *= base
        }
        exp = exp shr 1
        if (exp > 0) {
            if (base != 0 && (base > Int.MAX_VALUE / base || base < Int.MIN_VALUE / base)) {
                return if ((this > 0) || exponent % 2 == 0) Int.MAX_VALUE else Int.MIN_VALUE
            }
            base *= base
        }
    }
    return result
}

/**
 * Returns the sum of this value and [other].
 *
 * If the result overflows the range of [Long], returns [Long.MAX_VALUE] or [Long.MIN_VALUE] accordingly.
 *
 * @param other the value to add.
 * @return the sum, or the saturated value if overflow occurs.
 *
 * Example:
 * ```
 * Long.MAX_VALUE.add(1L) // returns Long.MAX_VALUE
 * Long.MIN_VALUE.add(-1L) // returns Long.MIN_VALUE
 * ```
 */
@Suppress("unused")
fun Long.add(other: Long): Long {
    val sum = this + other
    return if ((this xor other) >= 0L && (this xor sum) < 0L) {
        if (this >= 0L) Long.MAX_VALUE else Long.MIN_VALUE
    } else {
        sum
    }
}

/**
 * Returns the difference of this value and [other].
 *
 * If the result overflows the range of [Long], returns [Long.MAX_VALUE] or [Long.MIN_VALUE] accordingly.
 *
 * @param other the value to subtract.
 * @return the difference, or the saturated value if overflow occurs.
 *
 * Example:
 * ```
 * Long.MIN_VALUE.subtract(1L) // returns Long.MIN_VALUE
 * Long.MAX_VALUE.subtract(-1L) // returns Long.MAX_VALUE
 * ```
 */
@Suppress("unused")
fun Long.subtract(other: Long): Long {
    val diff = this - other
    return if ((this xor other) < 0L && (this xor diff) < 0L) {
        if (this >= 0L) Long.MAX_VALUE else Long.MIN_VALUE
    } else {
        diff
    }
}

/**
 * Returns the product of this value and [other].
 *
 * If the result overflows the range of [Long], returns [Long.MAX_VALUE] or [Long.MIN_VALUE] accordingly.
 *
 * @param other the value to multiply.
 * @return the product, or the saturated value if overflow occurs.
 *
 * Example:
 * ```
 * Long.MAX_VALUE.multiply(2L) // returns Long.MAX_VALUE
 * Long.MIN_VALUE.multiply(2L) // returns Long.MIN_VALUE
 * ```
 */
@Suppress("unused")
fun Long.multiply(other: Long): Long {
    if (this == 0L || other == 0L) return 0L
    val product = this * other
    return if (product / other != this) {
        if ((this > 0L) == (other > 0L)) Long.MAX_VALUE else Long.MIN_VALUE
    } else {
        product
    }
}

/**
 * Returns the quotient of this value and [other].
 *
 * If [other] is zero, returns this value.
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
fun Long.divide(other: Long): Long = if (other == 0L) this else this / other

/**
 * Returns this value raised to the power of [exponent].
 *
 * If [exponent] is zero, returns 1.
 * If [exponent] is negative, throws [IllegalArgumentException].
 *
 * If the result overflows the range of [Long], returns [Long.MAX_VALUE] or [Long.MIN_VALUE] accordingly.
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
    var result = 1L
    var base = this
    var exp = exponent

    while (exp > 0) {
        if ((exp and 1) != 0) {
            if (base != 0L && (result > Long.MAX_VALUE / base || result < Long.MIN_VALUE / base)) {
                return if ((this > 0) || exponent % 2 == 0) Long.MAX_VALUE else Long.MIN_VALUE
            }
            result *= base
        }
        exp = exp shr 1
        if (exp > 0) {
            if (base != 0L && (base > Long.MAX_VALUE / base || base < Long.MIN_VALUE / base)) {
                return if ((this > 0) || exponent % 2 == 0) Long.MAX_VALUE else Long.MIN_VALUE
            }
            base *= base
        }
    }
    return result
}
