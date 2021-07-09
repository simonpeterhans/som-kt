package ch.unibas.dmi.dbis.som.util

import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Element-wise subtracts a double array from this array, returning a new double array.
 *
 * @param other The other array.
 * @return The resulting (new) array.
 */
operator fun DoubleArray.minus(other: DoubleArray): DoubleArray {
    val arr = DoubleArray(this.size) { 0.0 }

    for (i in this.indices) {
        arr[i] = this[i] - other[i]
    }

    return arr
}

/**
 * Calculates the sum of the provided power of the objects in this array.
 *
 * @param power The power to use for calculating the sum.
 * @return The summed up result.
 */
fun DoubleArray.powSum(power: Double): Double {
    var sum = 0.0

    for (e in this) {
        sum += e.pow(power)
    }

    return sum
}

/**
 * Calculates the sum of squares of this array.
 *
 * @return The sum of squares.
 */
fun DoubleArray.squaredSum(): Double {
    return powSum(2.0)
}

/**
 * Calculates the Euclidean norm of this array (sqrt of the sum of squares).
 *
 * @return The Euclidean norm of this array.
 */
fun DoubleArray.norm(): Double {
    return sqrt(squaredSum())
}
