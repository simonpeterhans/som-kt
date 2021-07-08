package ch.unibas.dmi.dbis.som

import kotlin.random.Random

class Node() {

    lateinit var coords: DoubleArray
    lateinit var weights: DoubleArray

    constructor(coords: DoubleArray) : this() {
        this.coords = coords
    }

    constructor(depth: Int, rand: Random = Random(Random.nextInt())) : this() {
        initWeights(depth, rand)
    }

    fun initCoords(vararg coords: Double) {
        this.coords = coords
    }

    fun initWeights(depth: Int, rand: Random) {
        weights = DoubleArray(depth) { rand.nextDouble() }
    }

}
