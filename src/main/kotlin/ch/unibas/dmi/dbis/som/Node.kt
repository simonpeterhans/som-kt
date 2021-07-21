package ch.unibas.dmi.dbis.som

import kotlin.random.Random

/**
 * Node data structure to hold the coordinates on the grid and the weights.
 */
class Node() {

    lateinit var coords: DoubleArray
    lateinit var weights: DoubleArray

    constructor(coords: DoubleArray) : this() {
        this.coords = coords
    }

    constructor(depth: Int, rand: Random = Random(Random.nextInt())) : this() {
        initWeights(depth, rand)
    }

    /**
     * Convenience setter for node coordinates with varargs.
     *
     * @param coords The coordinates as doubles.
     */
    fun initCoords(vararg coords: Double) {
        this.coords = coords
    }

    /**
     * Initializes the weights for this node for a given depth (dimension).
     *
     * @param depth The number of features to create weights for (e.g., 3 for images).
     * @param rand The random object (seed) to use to randomize the weights from.
     */
    fun initWeights(depth: Int, rand: Random = Random(Random.nextInt())) {
        weights = DoubleArray(depth) { rand.nextDouble() }
    }

}
