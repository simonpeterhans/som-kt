package ch.unibas.dmi.dbis.som.grids

import ch.unibas.dmi.dbis.som.Node
import ch.unibas.dmi.dbis.som.util.DistanceFunction
import kotlin.random.Random

/**
 * Regular 2D square/rectangular grid, like so:
 *
 * ```
 * x x x x
 * x x x x
 * x x x x
 * x x x x
 * x x x x
 * ```
 *
 * @property height The height of the grid.
 * @property width The width of the grid.
 * @property featureDepth The dimension of the feature vector that will be used by the nodes on this grid.
 * @property distanceFunction A distance function to calculate the neighborhood of a node.
 * @property rand The random seed to use.
 */
class Grid2DSquare(
    val height: Int,
    val width: Int,
    val featureDepth: Int,
    distanceFunction: DistanceFunction = DistanceFunction.euclideanNorm(),
    rand: Random = Random(Random.nextInt())
) : Grid2D(intArrayOf(height, width), distanceFunction, rand) {

    override val nodeGrid: Array<Array<Node>> = run {
        val nodes = Array(height) { Array(width) { Node(featureDepth, rand) } }

        for (i in 0 until height) {
            for (j in 0 until width) {
                nodes[i][j].initCoords(i.toDouble(), j.toDouble())
            }
        }

        nodes
    }

    override val nodes: Array<Node> = nodeGrid.flatten().toTypedArray()

    override fun node(vararg idx: Int): Node = nodeGrid[idx[0]][idx[1]]

}
