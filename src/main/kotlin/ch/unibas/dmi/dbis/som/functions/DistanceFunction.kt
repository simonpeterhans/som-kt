package ch.unibas.dmi.dbis.som.functions

import ch.unibas.dmi.dbis.som.util.minus
import ch.unibas.dmi.dbis.som.util.norm
import ch.unibas.dmi.dbis.som.util.squaredSum
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Generic neighborhood functions to evaluate the distance from one vector (1D array) to another.
 *
 * In the SOM use case, this is used to evaluate the distance of every node to the best matching unit
 * (i.e., the node closest to the current sample).
 *
 * Depending on the implementation, the distances can, for instance, be circular (Euclidean) or square (min/max norm).
 */
fun interface DistanceFunction {

    /**
     * Evaluates the distance between two arrays of doubles.
     *
     * @param first The first vector.
     * @param second The second vector.
     * @return The calculated distance.
     */
    fun apply(first: DoubleArray, second: DoubleArray): Double

    companion object {

        /**
         * Creates a distance function for the Euclidean norm (sqrt of sum of squared distances).
         *
         * @return A DistanceFunction object for the Euclidean norm.
         */
        fun euclideanNorm(): DistanceFunction {
            return DistanceFunction { first, second -> (first - second).norm() }
        }

        /**
         * Creates a distance function for the scalar product (dot product),
         * i.e., the sum of the products of the corresponding entries of the two arrays.
         *
         * @return A DistanceFunction object for the scalar product.
         */
        fun scalarProduct(): DistanceFunction {
            return DistanceFunction { first, second ->
                var sum = 0.0

                for (i in first.indices) {
                    sum += first[i] * second[i]
                }

                sum
            }
        }

        /**
         * Creates a distance function for the minimum norm,
         * i.e., the smallest difference between two corresponding entries of the two arrays.
         *
         * @return A DistanceFunction object for the minimum norm.
         */
        fun minNorm(): DistanceFunction {
            return DistanceFunction { first, second -> (first - second).minOf { d -> abs(d) } }
        }

        /**
         * Creates a distance function for the maximum norm,
         * i.e., the largest difference between two corresponding entries of the two arrays.
         *
         * @return A DistanceFunction object for the maximum norm.
         */
        fun maxNorm(): DistanceFunction {
            return DistanceFunction { first, second -> (first - second).maxOf { d -> abs(d) } }
        }

        /**
         * Creates a distance function for the sum of squares of the two arrays.
         *
         * @return A DistanceFunction object for the squared distance.
         */
        fun squaredDistance(): DistanceFunction {
            return DistanceFunction { first, second -> (first - second).squaredSum() }
        }

        /**
         * Creates a distance function for the Euclidean norm (sqrt of sum of squared distances),
         * wrapping around the specified dimensions (height and/or width).
         *
         * @param dims The dimension of the grid.
         * @param doWrapDim An array of Booleans, wrapping around for the dimensions for the true entries.
         * @return A DistanceFunction object for the Euclidean norm.
         */
        fun euclideanNormToroidal(dims: IntArray, doWrapDim: BooleanArray): DistanceFunction {
            return DistanceFunction { first, second ->
                var sum = 0.0

                for (i in dims.indices) {
                    val diff = first[i] - second[i]

                    sum += if (doWrapDim[i]) {
                        min((diff), dims[i] - abs(diff)).pow(2.0)
                    } else {
                        (diff).pow(2.0)
                    }
                }

                sqrt(sum)
            }
        }

        /**
         * Creates a distance function for the Euclidean norm (sqrt of sum of squared distances),
         * wrapping the grid around in both dimensions.
         *
         * @param dims The dimension of the grid.
         * @return A DistanceFunction object for the Euclidean norm.
         */
        fun euclideanNorm2DToroidal(dims: IntArray): DistanceFunction {
            return euclideanNormToroidal(dims, booleanArrayOf(true, true))
        }

    }

}
