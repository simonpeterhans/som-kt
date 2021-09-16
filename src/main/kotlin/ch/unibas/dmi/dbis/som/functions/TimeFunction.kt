package ch.unibas.dmi.dbis.som.functions

import kotlin.math.sqrt

/**
 * Generic time function, based on a discrete timestamp and the maximum possible timestamp.
 *
 * In the SOM use case, this corresponds to the current iteration and the maximum number of iterations.
 */
fun interface TimeFunction {

    /**
     * Evaluates the time function at the given discrete timestamp for a maximum timestamp.
     *
     * @param t The current (discrete) timestamp.
     * @param T The maximum (discrete) timestamp.
     * @return The function value.
     */
    fun atTime(t: Int, T: Int): Double

    companion object {

        /**
         * Linearly decreasing function from 1.0 to 0.0 for t = [0, T].
         *
         * @param factor A factor that can be used to scale the result of the function (defaults to 1.0).
         * @return A time function with the described properties.
         */
        fun linearDecreasingFactorScaled(factor: Double = 1.0): TimeFunction {
            return TimeFunction { t, T -> factor * (1.0 - (t.toDouble() / T)) }
        }

        /**
         * Creates the default SOM sigma function based the grid dimensions.
         * Linearly decreasing, but scaled by the norm of the dimension vector of the grid.
         *
         * @param dims A vector of integers defining the dimensions of the grid.
         * @param factor The scaling factor (defaults to 1.0).
         * @return A time function with the described properties.
         */
        fun defaultSigmaFunction(dims: IntArray, factor: Double = 1.0): TimeFunction {
            return linearDecreasingFactorScaled(factor * sqrt(dims.fold(0.0) { a, i -> a + i * i }))
        }

    }

}
