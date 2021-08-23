package ch.unibas.dmi.dbis.som.grids

import ch.unibas.dmi.dbis.som.Node
import ch.unibas.dmi.dbis.som.util.DistanceFunction
import kotlin.random.Random

/**
 * Regular 3D cube/cuboid grid.
 *
 * @property height The height of the grid.
 * @property width The width of the grid.
 * @property depth The depth of the grid.
 * @property distanceFunction A distance function to calculate the distance between the sample and the best matching node.
 * @property neighborhoodFunction A distance function to calculate the neighborhood of a node.
 * @property rand The random seed to use.
 */
class Grid3DCube(
    val height: Int,
    val width: Int,
    val depth: Int,
    distanceFunction: DistanceFunction = DistanceFunction.squaredDistance(),
    neighborhoodFunction: DistanceFunction = DistanceFunction.euclideanNorm(),
    rand: Random = Random(Random.nextInt())
) : Grid3D(intArrayOf(height, width, depth), distanceFunction, neighborhoodFunction, rand) {

    override val nodeGrid: Array<Array<Array<Node>>> = run {
        val nodes = Array(height) { Array(width) { Array(depth) { Node() } } }

        for (i in 0 until height) {
            for (j in 0 until width) {
                for (k in 0 until depth) {
                    nodes[i][j][k].initCoords(i.toDouble(), j.toDouble(), k.toDouble())
                }
            }
        }

        nodes
    }

    override val nodes: Array<Node> = nodeGrid.flatMap { it.flatten() }.toTypedArray()

    override fun node(vararg idx: Int): Node = nodeGrid[idx[0]][idx[1]][idx[2]]

}
