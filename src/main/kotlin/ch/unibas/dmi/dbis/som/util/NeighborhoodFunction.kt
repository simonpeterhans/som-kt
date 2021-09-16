package ch.unibas.dmi.dbis.som.util

import kotlin.math.exp

/**
 * Neighborhood function to scale the distances from a node to the best matching unit (node),
 * can use the alpha (learn rate) and sigma parameters.
 *
 * While the learn rate is rarely used to for scaling as time is often already integrated in sigma,
 * it is available to enable all possibilities for the distance scaling function.
 */
fun interface NeighborhoodFunction {

    /**
     * Scales a distance by the sigma and alpha parameters.
     *
     * In the SOM use case, this distance is usually the distance of a node to another node
     * (usually to the best matching unit).
     *
     * @param d The distance value (previously calculated by some distance function).
     * @param sigma The sigma parameter.
     * @param alpha The alpha parameter (learn rate).
     * @return The scaled distance.
     */
    fun apply(d: Double, sigma: Double, alpha: Double): Double

    companion object {

        /**
         * Instantiates an exponentially decreasing scaling function based on sigma and alpha.
         *
         * @param factor The scaling factor to use (defaults to -0.5).
         * @return An exponentially decreasing scaling function.
         */
        fun exponentialDecreasing(factor: Double = -0.5): NeighborhoodFunction {
            return NeighborhoodFunction { d, sigma, alpha -> alpha * exp(factor * d / (sigma * sigma)) }
        }

    }

}
