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
     * Initializes the weights for this node for a given depth (dimension).
     *
     * @param depth The number of features to create weights for (e.g., 3 for images).
     * @param rand The random object (seed) to use to randomize the weights from.
     * @param from The minimum value in the range to randomize from.
     * @param to The maximum value in the range to randomize from.
     */
    fun initWeights(depth: Int, rand: Random = Random(Random.nextInt()), from: Double = 0.0, to: Double = 1.0) {
        _weights = DoubleArray(depth) { rand.nextDouble(from, to) }
    }

}
