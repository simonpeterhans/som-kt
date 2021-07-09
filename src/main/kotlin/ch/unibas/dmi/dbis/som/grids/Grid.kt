package ch.unibas.dmi.dbis.som.grids

import ch.unibas.dmi.dbis.som.Node
import ch.unibas.dmi.dbis.som.util.DistanceFunction
import ch.unibas.dmi.dbis.som.util.minus
import ch.unibas.dmi.dbis.som.util.squaredSum
import kotlin.random.Random

/**
 * Generic grid object holding nodes and performs basic operations like calculating the best matching unit
 * or the distance from a node to a point (e.g., the best matching unit).
 *
 * In general, the extensions provided here will assume the grid to start top left at 0/0,
 * with the first index moving down and the second index moving right.
 *
 * @property dims An int array describing the size of every dimension of this grid.
 * @property distanceFunction A distance function to calculate the neighborhood of a node.
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
     * Obtains a node from the grid by the given coordinates (NOT necessarily checked!).
     *
     * @param idx Integers describing the index of the node to retrieve.
     * @return The node at the specified index.
     */
    abstract fun node(vararg idx: Int): Node

    /**
     * Finds the id of the best (i.e., closest/smallest sum of squares) node for a given sample.
     *
     * @param sample The sample to find the closest node for.
     * @return The index of the best node for the provided sample.
     */
    fun findBestNodeId(sample: DoubleArray): Int {
        val bestList = ArrayList<Int>(nodes.size)
        var currBestVal = Double.MAX_VALUE

        for (i in nodes.indices) {
            val currVal = (nodes[i].weights - sample).squaredSum()

            if (currVal < currBestVal) {
                currBestVal = currVal

                bestList.clear()
                bestList.add(i)
            } else if (currVal == currBestVal) {
                bestList.add(i)
            }
        }

        return bestList[rand.nextInt(0, bestList.size)]
    }

    /**
     * Finds the best (i.e., closest/smallest sum of squares) node for a given sample.
     *
     * @param sample The sample to find the closest node for.
     * @return The best node for the provided sample.
     */
    fun findBestNode(sample: DoubleArray): Node {
        return nodes[findBestNodeId(sample)]
    }

    /**
     * Calculates distances of all nodes to a given point on the grid (e.g., to the best matching unit/node).
     *
     * Uses the provided distance function to calculate the distances.
     *
     * @param point The point to calculate the distances to.
     * @return An array of doubles containing the distances for all the nodes in the grid.
     */
    fun calcNodeDistancesToPoint(point: DoubleArray): DoubleArray {
        val distances = DoubleArray(nodes.size) { 0.0 }

        for (i in nodes.indices) {
            distances[i] = distanceFunction.apply(nodes[i].coords, point)
        }

        return distances
    }

}
