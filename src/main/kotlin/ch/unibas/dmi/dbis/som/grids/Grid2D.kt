package ch.unibas.dmi.dbis.som.grids

import ch.unibas.dmi.dbis.som.Node
import ch.unibas.dmi.dbis.som.util.NeighborhoodFunction
import kotlin.random.Random

abstract class Grid2D(
    dims: IntArray,
    neighborhoodFunction: NeighborhoodFunction,
    rand: Random,
) : Grid(dims, neighborhoodFunction, rand) {

    abstract val nodeGrid: Array<Array<Node>>

    override fun node(vararg idx: Int): Node = nodeGrid[idx[0]][idx[1]]

}
