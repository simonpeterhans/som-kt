package ch.unibas.dmi.dbis.som

import ch.unibas.dmi.dbis.som.grids.Grid
import ch.unibas.dmi.dbis.som.util.DistanceScalingFunction
import ch.unibas.dmi.dbis.som.util.TimeFunction
import mu.KotlinLogging
import kotlin.math.max
import kotlin.random.Random

private val logger = KotlinLogging.logger {}

/**
 * Creates a new Self Organizing Map for a given grid and parameters.
 *
 * Used to train the network defined on the [Grid] object in order to classify samples after training.
 *
 * Depending on the use case it is advisable to make use of different [TimeFunction] implementations
 * for alpha (learning rate) and sigma as well as a different neighborhood [DistanceScalingFunction]
 * to fit the problem at hand.
 *
 * @property grid The grid object defining the structure to use for this SOM instance.
 * @property distanceScaling A function to scale the delta of sample and node weights.
 * @property alpha A time function (depending on iteration number) for the learn rate.
 * @property sigma A time function (depending on the iteration number) to scale neighborhood distance factor.
 * @property rand The random seed to use (randomized by default).
 */
class SOM(
    val grid: Grid,
    private val distanceScaling: DistanceScalingFunction = DistanceScalingFunction.exponentialDecreasing(),
    private val alpha: TimeFunction = TimeFunction.linearDecreasingFactorScaled(),
    private val sigma: TimeFunction = TimeFunction.defaultSigmaFunction(grid.dims),
    private val rand: Random = Random(Random.nextInt())
) {

    /**
     * Performs a single training step for this SOM instance for a given sample.
     *
     * @param sample An array of doubles containing the features of the sample.
     * @param t The current time step (iteration).
     * @param T The maximum time step (total number of iterations).
     */
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
            val deltaFactor = a * distanceScaling.apply(dists[i], s, a)

            // Get node weights to update.
            val nodeWeights = grid.nodes[i].weights

            // Update every node weight.
            for (j in nodeWeights.indices) {
                nodeWeights[j] += (sample[j] - nodeWeights[j]) * deltaFactor
            }
        }
    }

    /**
     * Trains the SOM for the given samples.
     *
     * @param data An array of the samples with their features as double array.
     * @param epochs The number of epochs to train for (total iterations = epochs * samples).
     */
    fun train(data: Array<DoubleArray>, epochs: Int) {
        val maxIter = epochs * data.size
        var currIter = 0

        for (e in 0 until epochs) {
            val idx = data.indices.shuffled(rand)

            for (i in idx) {
                step(data[i], currIter, maxIter)

                currIter++

                if (currIter % max(maxIter / 100, 1) == 0) {
                    logger.info { "Training: ${100 * currIter / maxIter}% ($currIter/$maxIter)" }
                }
            }
        }
    }

    /**
     * Predicts the label/node for the given samples and returns its ID and the distance.
     *
     * The best node is (like during training) the best matching unit, i.e., the node with the smallest distance
     * from the sample according to the distance measure.
     *
     * @param data An array of samples and their features.
     * @return A list of pairs consisting of the assigned node and distance per sample.
     */
    fun predict(data: Array<DoubleArray>): ArrayList<Pair<Node, Double>> {
        val res = ArrayList<Pair<Node, Double>>(data.size)

        for (e in data) {
            res.add(grid.findBestNodeAndScore(e))
        }

        return res
    }

}
