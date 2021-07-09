package ch.unibas.dmi.dbis.som.util

import kotlin.math.sqrt

fun interface TimeFunction {

    fun atTime(t: Int, T: Int): Double

    companion object {

        fun linearDecreasingFactorScaled(initVal: Double = 1.0): TimeFunction {
            return TimeFunction { t, T -> initVal * (1.0 - (t.toDouble() / T)) }
        }

        fun linearDecreasingFactorScaled(dims: IntArray, factor: Double = 1.0): TimeFunction {
            return linearDecreasingFactorScaled(factor * sqrt(dims.fold(0.0) { a, i -> a + i * i }))
        }

    }

}
