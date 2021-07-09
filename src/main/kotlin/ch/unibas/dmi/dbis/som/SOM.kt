package ch.unibas.dmi.dbis.som

import ch.unibas.dmi.dbis.som.grids.Grid
import ch.unibas.dmi.dbis.som.util.NeighborhoodFactor
import ch.unibas.dmi.dbis.som.util.TimeFunction
import mu.KotlinLogging
import kotlin.random.Random

private val logger = KotlinLogging.logger {}

class SOM(
    val grid: Grid,
    val neighborhood: NeighborhoodFactor = NeighborhoodFactor.exponentialDecreasing(),
    val alpha: TimeFunction = TimeFunction.linearDecreasingFactorScaled(),
    val sigma: TimeFunction = TimeFunction.linearDecreasingFactorScaled(grid.dims),
    val rand: Random = Random(Random.nextInt())
) {

    private fun step(sample: DoubleArray, t: Int, T: Int) {
        // Get learn rate (a) and sigma for current iteration.
        val a = alpha.atTime(t, T)
        val s = sigma.atTime(t, T)

        // Get coordinates of best matching node.
        val bestNode = grid.findBestNode(sample)

        // Calculate distances of all nodes to the best matching node.
        val dists = grid.calcNodeDistancesToPoint(bestNode.coords)

        // Update every node based on the distance factor and weight delta.
        for (i in dists.indices) {
            // Calculate delta factor based on distance.
            val deltaFactor = a * neighborhood.getDistanceFactor(dists[i], s, a)

            // Get node weights to update.
            val nodeWeights = grid.nodes[i].weights

            // Update every node weight.
            for (j in nodeWeights.indices) {
                nodeWeights[j] += (sample[j] - nodeWeights[j]) * deltaFactor
            }
        }
    }

    fun train(data: Array<DoubleArray>, epochs: Int) {
        val maxIter = epochs * data.size
        var currIter = 0

        for (e in 0 until epochs) {
            val idx = data.indices.shuffled(rand)

            for (i in idx) {
                step(data[i], currIter, maxIter)

                currIter++

                if (currIter % (maxIter / 100) == 0) {
                    logger.info { "Training: ${100 * currIter / maxIter}% ($currIter/$maxIter)" }
                }
            }
        }
    }

}
