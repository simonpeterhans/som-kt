package ch.unibas.dmi.dbis.som.grids

import ch.unibas.dmi.dbis.som.Node
import ch.unibas.dmi.dbis.som.functions.DistanceFunction
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Regular 2D hexagonal grid, like so:
 *
 * ```
 * x x x x
 *  x x x x
 * x x x x
 *  x x x x
 * x x x x
 * ```
 *
 * This means that the coordinates are chosen such that every (non-edge) node has 6 direct/equidistant neighbors,
 * compared to the 4 in a regular square/rectangular grid.
 *
 * @property height The height of the grid.
 * @property width The width of the grid.
 * @property distanceFunction A distance function to calculate the distance between two nodes on the grid.
 * @property rand The random seed to use.
 */
class Grid2DHex(
    val height: Int,
    val width: Int,
    distanceFunction: DistanceFunction = DistanceFunction.euclideanNorm(),
    rand: Random = Random(Random.nextInt())
) : Grid2D(intArrayOf(height, width), distanceFunction, rand) {

    companion object {
        val HEX_SCALE = sqrt(3.0) / 2.0
        const val HEX_ADDEND = 0.5
    }

    override val nodeGrid: Array<Array<Node>> = run {
        val nodes = Array(height) { Array(width) { Node() } }

        for (i in 0 until height) {
            for (j in 0 until width) {
                nodes[i][j].initCoords(
                    i.toDouble() * HEX_SCALE, // Always scale height depending on current row.
                    j.toDouble() + HEX_ADDEND * (i % 2) // Only scale width for uneven rows.
                )
            }
        }

        nodes
    }

    override val nodes: Array<Node> = nodeGrid.flatten().toTypedArray()

}
