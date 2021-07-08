package ch.unibas.dmi.dbis.som.grids

import ch.unibas.dmi.dbis.som.util.NeighborhoodFunction

abstract class GridRegularND(
    private val dims: IntArray,
    neighborhoodFunction: NeighborhoodFunction = NeighborhoodFunction.euclideanNorm()
) : Grid(neighborhoodFunction) {

    override fun indexTo1D(vararg idx: Int): Int {
        var sum = 0

        for (i in idx.indices) {
            var tempSum = idx[i]

            for (j in idx.size - 1 downTo i + 1) {
                tempSum *= dims[j]
            }

            sum += tempSum
        }

        return sum
    }

}
