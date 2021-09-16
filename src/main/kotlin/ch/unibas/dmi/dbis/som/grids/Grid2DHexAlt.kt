package ch.unibas.dmi.dbis.som.grids

import ch.unibas.dmi.dbis.som.Node
import ch.unibas.dmi.dbis.som.functions.DistanceFunction
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Alternating 2D hexagonal grid, where every other row is shorter by 1 element, like so:
 *
 * ```
 * x x x x
 *  x x x
 * x x x x
 *  x x x
 * x x x x
 * ```
 *
 * This still means that the coordinates are chosen such that every (non-edge) node has 6 direct/equidistant neighbors,
 * compared to the 4 in a regular square/rectangular grid.
 *
 * @property height The height of the grid.
 * @property width The width of the grid.
 * @property distanceFunction A distance function to calculate the distance between two nodes on the grid.
 * @property rand The random seed to use.
 */
class Grid2DHexAlt(
    val height: Int,
    val width: Int,
    distanceFunction: DistanceFunction = DistanceFunction.euclideanNorm(),
    rand: Random = Random(Random.nextInt())
) : Grid2D(intArrayOf(height, width), distanceFunction, rand) {

    companion object {
        val HEX_HEIGHT_SCALE = sqrt(3.0) / 2.0
        const val HEX_WIDTH_ADDEND = 0.5
    }

    override val nodeGrid: Array<Array<Node>> = run {
        val nodes = Array(height) { Array(0) { Node() } }

        for (i in 0 until height) {
            // If we are at an uneven row, have width - 1 columns.
            nodes[i] = Array(if (i % 2 == 0) width else width - 1) { Node() }

            for (j in 0 until nodes[i].size) {
                nodes[i][j].initCoords(
                    i.toDouble() * HEX_HEIGHT_SCALE, // Always scale height depending on current row.
                    j.toDouble() + HEX_WIDTH_ADDEND * (i % 2) // Only scale width for uneven rows.
                )
            }
        }

        nodes
    }

    override val nodes: Array<Node> = nodeGrid.flatten().toTypedArray()

}
