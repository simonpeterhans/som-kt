package ch.unibas.dmi.dbis.som.grids

import ch.unibas.dmi.dbis.som.Node
import ch.unibas.dmi.dbis.som.PredictionResult
import ch.unibas.dmi.dbis.som.functions.DistanceFunction
import kotlin.random.Random

/**
 * Generic grid object holding nodes and performs basic operations like calculating the best matching unit
 * or the distance from a node to a point (e.g., the best matching unit).
 *
 * In general, the extensions provided here will assume the grid to start top left at 0/0,
 * with the first index moving down and the second index moving right.
 *
 * @property dims An int array describing the size of every dimension of this grid.
 * @property distanceFunction A distance function to calculate the distance between two nodes on the grid.
 * @property rand The random seed to use.
 */
abstract class Grid(
    val dims: IntArray,
    val distanceFunction: DistanceFunction,
    val rand: Random,
) {

    /**
     * 1 dimensional array of node objects, used for computation.
     */
    abstract val nodes: Array<Node>

    /**
     * A distance function to calculate the distance between the sample and the best matching node.
     */
    var bmuDistanceFunction: DistanceFunction = DistanceFunction.squaredDistance()

    /**
     * Obtains a node from the grid by the given coordinates (NOT necessarily checked!).
     *
     * @param idx Integers describing the index of the node to retrieve.
     * @return The node at the specified index.
     */
    abstract fun node(vararg idx: Int): Node

    /**
     * Returns the number of nodes in the grid.
     *
     * @return The number of nodes in the grid.
     */
    fun getSize(): Int = nodes.size

    /**
     * Initializes weights for all nodes randomly within a specified range for every dimension of the feature.
     *
     * @param depth The feature depth (i.e., the dimension of the weight vector).
     * @param lowerBoundArray An array of minimum values in the range to randomize from for every index.
     * @param upperBoundArray An array of maximum values in the range to randomize from for every index.
     * @param altRand An alternative random seed to use (the random seed of this object will be used if not provided).
     * @return This Grid object for chaining.
     */
    fun initializeWeights(
        depth: Int,
        lowerBoundArray: DoubleArray,
        upperBoundArray: DoubleArray,
        altRand: Random = rand
    ): Grid {
        for (n in nodes) {
            n.initWeights(depth, lowerBoundArray, upperBoundArray, altRand)
        }

        return this
    }

    /**
     * Initializes weights for all nodes randomly within a range (defaults to [0.0, 1.0]).
     *
     * @param depth The feature depth (i.e., the dimension of the weight vector).
     * @param lowerBound The minimum value of the range to randomize from.
     * @param upperBound The maximum value of the range to randomize from
     * @param altRand An alternative random seed to use (the random seed of this object will be used if not provided).
     * @return This Grid object for chaining.
     */
    fun initializeWeights(
        depth: Int,
        lowerBound: Double = 0.0,
        upperBound: Double = 1.0,
        altRand: Random = rand
    ): Grid {
        for (n in nodes) {
            n.initWeights(depth, lowerBound, upperBound, altRand)
        }

        return this
    }

    /**
     * Finds the id and distance of the best (i.e., closest/smallest sum of squares) node for a given sample.
     *
     * @param sample The sample to find the closest node index for.
     * @return The index and distance of the best node for the provided sample as a pair.
     */
    fun findBestNodeIdAndScore(sample: DoubleArray): PredictionResult {
        val bestList = ArrayList<Int>(nodes.size)
        var currBestVal = Double.MAX_VALUE

        for (i in nodes.indices) {
            val currVal = bmuDistanceFunction.apply(nodes[i].weights, sample)

            if (currVal < currBestVal) {
                currBestVal = currVal

                bestList.clear()
                bestList.add(i)
            } else if (currVal == currBestVal) {
                bestList.add(i)
            }
        }

        return PredictionResult(bestList[rand.nextInt(0, bestList.size)], currBestVal)
    }

    /**
     * Finds the best (i.e., closest/smallest sum of squares) node for a given sample.
     *
     * @param sample The sample to find the closest node for.
     * @return The best node for the provided sample.
     */
    fun findBestNode(sample: DoubleArray): Node {
        return nodes[findBestNodeIdAndScore(sample).nodeId]
    }

    /**
     * Calculates distances of all nodes to a given point on the grid (e.g., to the best matching unit/node).
     *
     * Uses the provided distance function to calculate the distances.
     *
     * @param point The point to calculate the distances to.
     * @return An array of doubles containing the distances for all the nodes in the grid.
     */
    fun getNodeDistsToPoint(point: DoubleArray): DoubleArray {
        val distances = DoubleArray(nodes.size) { 0.0 }

        for (i in nodes.indices) {
            distances[i] = distanceFunction.apply(nodes[i].coords, point)
        }

        return distances
    }

}
