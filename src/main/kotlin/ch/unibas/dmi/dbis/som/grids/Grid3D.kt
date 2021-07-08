package ch.unibas.dmi.dbis.som.grids

import ch.unibas.dmi.dbis.som.Node
import ch.unibas.dmi.dbis.som.util.NeighborhoodFunction
import kotlin.random.Random

class Grid3D(
    val height: Int,
    val width: Int,
    val depth: Int,
    val featureDepth: Int,
    neighborhoodFunction: NeighborhoodFunction = NeighborhoodFunction.euclideanNorm(),
    val rand: Random = Random(Random.nextInt())
) : GridRegularND(intArrayOf(height, width, depth), neighborhoodFunction) {

    val nodeGrid: Array<Array<Array<Node>>> = run {
        val nodes = Array(height) { Array(width) { Array(depth) { Node(featureDepth, rand) } } }

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
