package ch.unibas.dmi.dbis.som.grids

import ch.unibas.dmi.dbis.som.Node
import ch.unibas.dmi.dbis.som.util.NeighborhoodFunction
import ch.unibas.dmi.dbis.som.util.minus
import ch.unibas.dmi.dbis.som.util.squaredSum
import kotlin.random.Random

abstract class Grid(
    val dims: IntArray,
    val neighborhoodFunction: NeighborhoodFunction,
    val rand: Random,
) {

    abstract val nodes: Array<Node>

    abstract fun node(vararg idx: Int): Node

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
