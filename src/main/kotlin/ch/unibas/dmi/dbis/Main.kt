package ch.unibas.dmi.dbis

import ch.unibas.dmi.dbis.som.Grid
import ch.unibas.dmi.dbis.som.SOM
import mu.KotlinLogging
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.random.Random

private val logger = KotlinLogging.logger {}

fun main() {

    val width = 100
    val height = 100
    val dim = 3

    val data: Array<DoubleArray> = Array(10000) { DoubleArray(dim) { Random.nextDouble() } }

    val g = Grid(width, height, dim)
    val s = SOM(g, 0.7, 1.0, 5)

    logger.info("Training...")

    val start = System.currentTimeMillis()

    s.train(data)

    logger.info("Train time: ${System.currentTimeMillis() - start} ms.")
    logger.info("Writing image...")

    val pixels = s.grid.nodeWeights

    val im = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    for (i in 0 until height) {
        for (j in 0 until width) {
            val currPixel = s.grid.index2Dto1D(i, j)
            val color =
                Color(pixels[currPixel][0].toFloat(), pixels[currPixel][1].toFloat(), pixels[currPixel][2].toFloat())
            im.setRGB(j, i, color.rgb)
        }
    }

    val out = File("img.png")
    ImageIO.write(im, "png", out)

    logger.info("Done.")

}

