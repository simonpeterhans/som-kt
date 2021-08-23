package ch.unibas.dmi.dbis.som.grids

import ch.unibas.dmi.dbis.som.Node
import ch.unibas.dmi.dbis.som.util.DistanceFunction
import kotlin.random.Random

/**
 * Generic 3D grid.
 *
 * @property dims An int array describing the size of every dimension of this grid.
 * @property distanceFunction A distance function to calculate the distance between the sample and the best matching node.
 * @property neighborhoodFunction A distance function to calculate the neighborhood of a node.
 * @property rand The random seed to use.
 */
abstract class Grid3D(
    dims: IntArray,
    distanceFunction: DistanceFunction,
    neighborhoodFunction: DistanceFunction,
    rand: Random
) : Grid(dims, distanceFunction, neighborhoodFunction, rand) {

    /**
     * 3D array for easier access (in addition to the 1D array used by the [Grid] base class).
     */
    abstract val nodeGrid: Array<Array<Array<Node>>>

    override fun node(vararg idx: Int): Node = nodeGrid[idx[0]][idx[1]][idx[2]]

}
