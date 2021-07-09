package ch.unibas.dmi.dbis.som.util

import kotlin.math.pow
import kotlin.math.sqrt

operator fun DoubleArray.minus(other: DoubleArray): DoubleArray {
    val arr = DoubleArray(this.size) { 0.0 }

    for (i in this.indices) {
        arr[i] = this[i] - other[i]
    }

    return arr
}

fun DoubleArray.powSum(power: Double): Double {
    var sum = 0.0

    for (e in this) {
        sum += e.pow(power)
    }

    return sum
}

fun DoubleArray.squaredSum(): Double {
    return powSum(2.0)
}

fun DoubleArray.norm(): Double {
    return sqrt(squaredSum())
}
