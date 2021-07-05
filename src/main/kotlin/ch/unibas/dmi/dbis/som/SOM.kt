package ch.unibas.dmi.dbis.som

import ch.unibas.dmi.dbis.som.util.minus
import mu.KotlinLogging
import kotlin.math.exp

private val logger = KotlinLogging.logger {}

class SOM(
    val grid: Grid,
    val initialLearnRate: Double,
    val sigma: Double,
    val epochs: Int,
) {

    var learnRate = initialLearnRate

    private fun step(sample: DoubleArray) {
        // Get coordinates of best matching node.
        val coords = grid.findBestNodeCoords(sample)

        // Calculate distances of all nodes to the best matching node.
        val dists = grid.calcNodeDistancesToPoint(coords)

        // Calculate neighborhood factor for every node (further away = smaller factor).
        // TODO Adjust neighborhood selection and learn rate.
        val neighborhood = dists.map { d -> learnRate * exp(-0.1 * (d / (sigma * sigma))) }.toDoubleArray()

        // Calculate delta for every node.
        var delta: DoubleArray

        for (i in grid.nodeWeights.indices) {
            // 1. Calculate difference of node weights to sample.
            delta = sample - grid.nodeWeights[i]

            // 2. Multiply differences by neighborhood factor and add.
            for (j in delta.indices) {
                grid.nodeWeights[i][j] += delta[j] * neighborhood[i]
            }
        }
    }

    fun train(data: Array<DoubleArray>) {
        val maxIter = epochs * data.size
        var localIter = 0

        for (e in 0 until epochs) {
            val idx = data.indices.shuffled()

            for (i in idx) {
                if (localIter % 100 == 0) {
                    logger.info { "Current iteration: $localIter/$maxIter" }
                }

                step(data[i])

                learnRate = (1.0 - (localIter.toDouble() / maxIter)) * initialLearnRate

                localIter++
            }
        }
    }

}
