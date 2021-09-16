package ch.unibas.dmi.dbis

import ch.unibas.dmi.dbis.som.SOM
import ch.unibas.dmi.dbis.som.functions.DistanceFunction
import ch.unibas.dmi.dbis.som.functions.NeighborhoodFunction
import ch.unibas.dmi.dbis.som.functions.TimeFunction
import ch.unibas.dmi.dbis.som.grids.Grid2DHex
import mu.KotlinLogging
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import javax.imageio.ImageIO
import kotlin.math.max
import kotlin.math.sqrt
import kotlin.random.Random

private val logger = KotlinLogging.logger {}

fun main() {

    logger.info { "Starting." }

    val seed = 0

    // Data.
    val dataRand = Random(seed - 1)
    val size = 1000
    val originalSize = 1000
    val dim = 3

    val data: Array<DoubleArray> = Array(size) { DoubleArray(dim) { dataRand.nextDouble() } }

    // SOM.
    val rand = Random(seed)
    val width = 5
    val height = 5

    val epochs = 1
    val initAlpha = 0.9
    val initSigma = 0.25 * sqrt((width * width + height * height).toDouble())
    val alpha = TimeFunction { t, T -> initAlpha * (1.0 - t.toDouble() / originalSize) }
    val sigma = TimeFunction { t, T -> max(initSigma * ((originalSize - t).toDouble() / originalSize), 0.5) }

    val g = Grid2DHex(
        height,
        width,
        DistanceFunction.squaredDistance(),
        rand
    )

    g.initializeWeights(dim)

    val s = SOM(
        g,
        NeighborhoodFunction.exponentialDecreasing(),
        alpha = alpha,
        sigma = sigma,
        rand = rand
    )

    logger.info { "Training..." }

    val start = System.currentTimeMillis()

    s.train(data, epochs, shuffle = false)

    logger.info { "Train time: ${System.currentTimeMillis() - start} ms." }

    logger.info { "Writing image and csv..." }

    val im = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val nodes = g.nodeGrid

    for (i in 0 until height) {
        for (j in 0 until nodes[i].size) {
            val color = Color(
                nodes[i][j].weights[0].toFloat(),
                nodes[i][j].weights[1].toFloat(),
                nodes[i][j].weights[2].toFloat()
            )

            im.setRGB(j, i, color.rgb)
        }
    }

    val path = File("data/")
    val fileName = "out"
    Files.createDirectories(path.toPath())

    val out = File(path.resolve("$fileName.png").toURI())
    ImageIO.write(im, "png", out)

    File(path.resolve("$fileName.csv").toURI()).printWriter().use { o ->
        o.println("i,r,g,b")
        for (i in g.nodes.indices) {
            val w = g.nodes[i].weights
            o.println("$i,${w.joinToString(separator = ",")}")
        }
    }

    logger.info { "Done." }

}
