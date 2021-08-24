package ch.unibas.dmi.dbis.som

import kotlin.random.Random

/**
 * Node data structure to hold the coordinates on the grid and the weights.
 */
class Node() {

    private var _coords: DoubleArray? = null
    val coords: DoubleArray
        get() = _coords ?: throw UninitializedPropertyAccessException("Node coordinates have not been initialized!")

    private var _weights: DoubleArray? = null
    val weights: DoubleArray
        get() = _weights ?: throw UninitializedPropertyAccessException("Node weights have not been initialized!")

    constructor(coords: DoubleArray) : this() {
        this._coords = coords
    }

    /**
     * Convenience setter for node coordinates with varargs.
     *
     * @param coords The coordinates as doubles.
     */
    fun initCoords(vararg coords: Double) {
        this._coords = coords
    }

    /**
     * Initialize the weight array of this node.
     *
     * @param weights An array of doubles with the initial weights for this node.
     */
    fun initWeights(weights: DoubleArray) {
        this._weights = weights
    }

    /**
     * Initializes the weights for this node for a given depth (dimension).
     *
     * @param depth The number of features to create weights for (e.g., 3 for images).
     * @param lowerBound The minimum value in the range to randomize from.
     * @param upperBound The maximum value in the range to randomize from.
     * @param rand The random object (seed) to use to randomize the weights from.
     */
    fun initWeights(
        depth: Int,
        lowerBound: Double = 0.0,
        upperBound: Double = 1.0,
        rand: Random = Random(Random.nextInt())
    ) {
        _weights = if (lowerBound == upperBound) {
            DoubleArray(depth) { lowerBound }
        } else {
            DoubleArray(depth) { rand.nextDouble(lowerBound, upperBound) }
        }
    }

    /**
     * Initializes the weights for this node for a given depth (dimension), randomizing from a specified range for every
     * index of the weight array.
     *
     * @param depth The number of features to create weights for (e.g., 3 for images).
     * @param lowerBoundArray An array of minimum values in the range to randomize from for every index.
     * @param upperBoundArray An array of maximum values in the range to randomize from for every index.
     * @param rand The random object (seed) to use to randomize the weights from.
     */
    fun initWeights(
        depth: Int,
        lowerBoundArray: DoubleArray,
        upperBoundArray: DoubleArray,
        rand: Random = Random(Random.nextInt())
    ) {
        val arr = DoubleArray(depth)

        for (i in arr.indices) {
            if (lowerBoundArray[i] == upperBoundArray[i]) {
                arr[i] = lowerBoundArray[i]
            } else {
                arr[i] = rand.nextDouble(lowerBoundArray[i], upperBoundArray[i])
            }
        }

        _weights = arr
    }

}
