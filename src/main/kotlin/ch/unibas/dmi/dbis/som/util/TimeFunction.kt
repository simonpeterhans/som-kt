package ch.unibas.dmi.dbis.som.util

fun interface TimeFunction {

    fun atTime(t: Int, T: Int): Double

    companion object {

        fun linearDecreasingFactorScaled(initVal: Double = 1.0): TimeFunction {
            return TimeFunction { t, T -> initVal * (1.0 - (t.toDouble() / T)) }
        }

    }

}
