package ch.unibas.dmi.dbis.som.grids

import ch.unibas.dmi.dbis.som.Node
import ch.unibas.dmi.dbis.som.util.NeighborhoodFunction
import ch.unibas.dmi.dbis.som.util.minus
import ch.unibas.dmi.dbis.som.util.squaredSum

abstract class Grid(
    val neighborhoodFunction: NeighborhoodFunction = NeighborhoodFunction.euclideanNorm()
) {

    abstract val nodes: Array<Node>

    abstract fun indexTo1D(vararg idx: Int): Int

    abstract fun node(vararg idx: Int): Node

    fun findBestNodeId(sample: DoubleArray): Int {
        var currBestIdx1D = -1
        var currBestVal = Double.MAX_VALUE

        // TODO Handle ties by randomizing from a list.
        for (i in nodes.indices) {
            val currVal = (nodes[i].weights - sample).squaredSum()

            if (currVal < currBestVal) {
                currBestVal = currVal
                currBestIdx1D = i
            }
        }

        return currBestIdx1D
    }

    fun findBestNode(sample: DoubleArray): Node {
        return nodes[findBestNodeId(sample)]
    }

    fun calcNodeDistancesToPoint(point: DoubleArray): DoubleArray {
        val distances = DoubleArray(nodes.size) { 0.0 }

        for (i in nodes.indices) {
            distances[i] = neighborhoodFunction.distanceTo(nodes[i].coords, point)
        }

        return distances
    }

}
