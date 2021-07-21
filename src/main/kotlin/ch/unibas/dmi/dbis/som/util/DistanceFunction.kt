package ch.unibas.dmi.dbis.som.util

import kotlin.math.abs

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

    }

}
