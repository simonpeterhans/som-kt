package ch.unibas.dmi.dbis.som

import ch.unibas.dmi.dbis.som.grids.Grid
import ch.unibas.dmi.dbis.som.util.NeighborhoodFactor
import ch.unibas.dmi.dbis.som.util.TimeFunction
import ch.unibas.dmi.dbis.som.util.minus
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
        val a = alpha.atTime(t, T)
        val s = sigma.atTime(t, T)

        // Get coordinates of best matching node.
        val bestNode = grid.findBestNode(sample)

        // Calculate distances of all nodes to the best matching node.
        val dists = grid.calcNodeDistancesToPoint(bestNode.coords)

        // Calculate neighborhood factor for every node (further away = smaller factor).
        val neighborhoods = dists.map { d -> a * neighborhood.getDistanceFactor(d, s, a) }.toDoubleArray()

        // Calculate delta for every node.
        var delta: DoubleArray

        for (i in grid.nodes.indices) {
            // 1. Calculate difference of node weights to sample.
            delta = sample - grid.nodes[i].weights

            // 2. Multiply differences by neighborhood factor and add.
            for (j in delta.indices) {
                grid.nodes[i].weights[j] += delta[j] * neighborhoods[i]
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
