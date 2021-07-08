package ch.unibas.dmi.dbis.som.grids

import ch.unibas.dmi.dbis.som.Node
import ch.unibas.dmi.dbis.som.util.NeighborhoodFunction
import kotlin.math.sqrt
import kotlin.random.Random

class Grid2DHexIrr(
    val height: Int,
    val width: Int,
    val featureDepth: Int,
    neighborhoodFunction: NeighborhoodFunction = NeighborhoodFunction.euclideanNorm(),
    val rand: Random = Random(Random.nextInt())
) : Grid(neighborhoodFunction) {

    companion object {
        val HEX_HEIGHT_SCALE = sqrt(3.0) / 2.0
        const val HEX_WIDTH_ADDEND = 0.5
    }

    val nodeGrid: Array<Array<Node>> = run {
        val nodes = Array(height) { Array(0) { Node() } }

        for (i in 0 until height) {
            nodes[i] = Array(if (i % 2 == 0) width else width - 1) { Node(featureDepth, rand) }

            for (j in 0 until nodes[i].size) {
                nodes[i][j].initCoords(
                    i.toDouble() * HEX_HEIGHT_SCALE,
                    j.toDouble() + HEX_WIDTH_ADDEND * (i % 2)
                )
            }
        }

        nodes
    }

    override val nodes: Array<Node> = nodeGrid.flatten().toTypedArray()

    override fun indexTo1D(vararg idx: Int): Int {
        TODO("Not yet implemented")
    }

    override fun node(vararg idx: Int): Node = nodeGrid[idx[0]][idx[1]]

}
