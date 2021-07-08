package ch.unibas.dmi.dbis

import ch.unibas.dmi.dbis.som.SOM
import ch.unibas.dmi.dbis.som.grids.Grid2DHexIrr
import ch.unibas.dmi.dbis.som.util.NeighborhoodFactor
import ch.unibas.dmi.dbis.som.util.TimeFunction
import mu.KotlinLogging
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.sqrt
import kotlin.random.Random

private val logger = KotlinLogging.logger {}

fun main() {

    val width = 16
    val height = 4
    val dim = 3
    val epochs = 100
    val size = 100000
    val initAlpha = 1.0
    val initSigma = 1.0 * (sqrt(width.toDouble() * width.toDouble() + height.toDouble() * height.toDouble()))

    val rand = Random(42)
    val data: Array<DoubleArray> = Array(size) { DoubleArray(dim) { rand.nextDouble() } }

    val g = Grid2DHexIrr(height, width, dim, rand = rand)
    val s = SOM(
        g,
        NeighborhoodFactor.exponentialDecreasing(),
        alpha = TimeFunction.linearDecreasingFactorScaled(initAlpha),
        sigma = TimeFunction.linearDecreasingFactorScaled(initSigma),
        rand = rand
    )

    logger.info { "Training..." }

    val start = System.currentTimeMillis()

    s.train(data, epochs)

    logger.info { "Train time: ${System.currentTimeMillis() - start} ms." }
    logger.info { "Writing image..." }

    val im = BufferedImage(2 * width - 1, height, BufferedImage.TYPE_INT_RGB)
    val nodes = g.nodeGrid

    for (i in 0 until height) {
        for (j in 0 until nodes[i].size) {
            val color = Color(
                nodes[i][j].weights[0].toFloat(),
                nodes[i][j].weights[1].toFloat(),
                nodes[i][j].weights[2].toFloat()
            )

            if (i % 2 == 0) {
                im.setRGB(2 * j, i, color.rgb)
            } else {
                im.setRGB(2 * j + 1, i, color.rgb)
            }
        }
    }

    val out = File("img.png")
    ImageIO.write(im, "png", out)

    logger.info { "Done." }

}
