package ch.unibas.dmi.dbis.som.util

import kotlin.math.exp

fun interface NeighborhoodFactor {

    fun getDistanceFactor(d: Double, sigma: Double, alpha: Double): Double

    companion object {

        fun exponentialDecreasing(factor: Double = -0.5): NeighborhoodFactor {
            return NeighborhoodFactor { d, sigma, _ -> exp(factor * d / (sigma * sigma)) }
        }

    }

}
