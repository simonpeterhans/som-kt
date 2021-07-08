package ch.unibas.dmi.dbis.som.util

import kotlin.math.abs

fun interface NeighborhoodFunction {

    fun distanceTo(first: DoubleArray, second: DoubleArray): Double

    companion object {

        fun euclideanNorm(): NeighborhoodFunction {
            return NeighborhoodFunction { first, second -> (first - second).norm() }
        }

        fun scalarProduct(): NeighborhoodFunction {
            return NeighborhoodFunction { first, second ->
                var sum = 0.0

                for (i in first.indices) {
                    sum += first[i] * second[i]
                }

                sum
            }
        }

        fun minNorm(): NeighborhoodFunction {
            return NeighborhoodFunction { first, second -> (first - second).minOf { d -> abs(d) } }
        }

        fun maxNorm(): NeighborhoodFunction {
            return NeighborhoodFunction { first, second -> (first - second).maxOf { d -> abs(d) } }
        }

        fun squaredDistance(): NeighborhoodFunction {
            return NeighborhoodFunction { first, second -> (first - second).squaredSum() }
        }

    }

}
