package ch.unibas.dmi.dbis.som

import ch.unibas.dmi.dbis.som.util.minus
import ch.unibas.dmi.dbis.som.util.norm
import ch.unibas.dmi.dbis.som.util.square
import kotlin.random.Random

class Grid(val width: Int, val height: Int, val depth: Int) {

    val numNodes = width * height
    val nodeWeights = Array(numNodes) { DoubleArray(depth) { 0.0 } }
    val nodeCoords: Array<DoubleArray> = Array(numNodes) { DoubleArray(2) { 0.0 } }

    init {
        initializeCoords()
        initializeWeights()
    }

    fun initializeCoords() {
        var currNode = 0

        for (i in 0 until height) {
            for (j in 0 until width) {
                nodeCoords[currNode++] = doubleArrayOf(i.toDouble(), j.toDouble())
            }
        }
    }

    fun initializeWeights(f: () -> Double = { Random.nextDouble() }) {
        for (i in nodeWeights.indices) {
            nodeWeights[i] = DoubleArray(depth) { f() }
        }
    }

    fun index2Dto1D(idxX: Int, idxY: Int): Int {
        return idxX * width + idxY
    }

    fun index1DTo2D(idx: Int): Pair<Int, Int> {
        return Pair(idx / width, idx % height)
    }

    fun findBestNodeId(sample: DoubleArray): Int {
        var currBestIdx1D = -1
        var currBestVal = Double.MAX_VALUE

        for (i in nodeWeights.indices) {
            val currVal: Double = (nodeWeights[i] - sample).square()

            if (currVal < currBestVal) {
                currBestVal = currVal
                currBestIdx1D = i
            }
        }

        return currBestIdx1D
    }

    fun findBestNodeCoords(sample: DoubleArray): DoubleArray {
        return nodeCoords[findBestNodeId(sample)]
    }

    fun calcNodeDistancesToPoint(point: DoubleArray): DoubleArray {
        val distances = DoubleArray(numNodes) { 0.0 }

        for (i in nodeCoords.indices) {
            distances[i] = (nodeCoords[i] - point).norm()
        }

        return distances
    }

}
