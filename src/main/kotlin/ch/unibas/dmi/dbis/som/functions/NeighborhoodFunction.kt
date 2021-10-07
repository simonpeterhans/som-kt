package ch.unibas.dmi.dbis.som.functions

import kotlin.math.exp
import kotlin.math.max

/**
 * Neighborhood function to scale the distances from a node to the best matching unit (node),
 * can use the alpha (learn rate) and sigma parameters.
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
         * Instantiates an exponentially decreasing neighborhood function based on sigma and alpha.
         * The form of this neighborhood function is nh(d, s, a) = a * exp((factor * d) / sigma^2).
         *
         * @param factor The scaling factor to use (defaults to -0.5).
         * @return An exponentially decreasing neighborhood function.
         */
        fun exponentialDecreasing(factor: Double = -0.5): NeighborhoodFunction {
            return NeighborhoodFunction { d, sigma, alpha ->
                max(alpha * exp(factor * d / (sigma * sigma)), 0.0)
            }
        }

    }

}
