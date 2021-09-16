package ch.unibas.dmi.dbis.som.grids

import ch.unibas.dmi.dbis.som.Node
import ch.unibas.dmi.dbis.som.functions.DistanceFunction
import kotlin.random.Random

/**
 * Generic 2D grid.
 *
 * @property dims An int array describing the size of every dimension of this grid.
 * @property distanceFunction A distance function to calculate the distance between two nodes on the grid.
 * @property rand The random seed to use.
 */
abstract class Grid2D(
    dims: IntArray,
    distanceFunction: DistanceFunction,
    rand: Random,
) : Grid(dims, distanceFunction, rand) {

    /**
     * 2D array for easier access (in addition to the 1D array used by the [Grid] base class).
     */
    abstract val nodeGrid: Array<Array<Node>>

    override fun node(vararg idx: Int): Node = nodeGrid[idx[0]][idx[1]]

}
