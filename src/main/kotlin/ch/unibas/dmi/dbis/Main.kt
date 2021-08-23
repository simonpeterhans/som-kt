package ch.unibas.dmi.dbis

import ch.unibas.dmi.dbis.som.SOM
import ch.unibas.dmi.dbis.som.grids.Grid2DSquare
import ch.unibas.dmi.dbis.som.util.DistanceFunction
import ch.unibas.dmi.dbis.som.util.DistanceScalingFunction
import ch.unibas.dmi.dbis.som.util.TimeFunction
import mu.KotlinLogging
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import javax.imageio.ImageIO
import kotlin.math.sqrt
import kotlin.random.Random

private val logger = KotlinLogging.logger {}

fun main() {

    val rand = Random(Random.nextInt())
    val width = 16
    val height = 4
    val dim = 3
    val epochs = 1
    val size = 1000000
    val initAlpha = 1.0
    val initSigma = 2.0 * (sqrt(width.toDouble() * width.toDouble() + height.toDouble() * height.toDouble()))

    val data: Array<DoubleArray> = Array(size) { DoubleArray(dim) { rand.nextDouble() } }

    val g = Grid2DSquare(
        height,
        width,
        neighborhoodFunction = DistanceFunction.euclideanNorm2DTorus(
            intArrayOf(height, width),
            booleanArrayOf(false, true)
        ),
        rand = rand
    )

    g.initializeNormalizedWeights(dim)

    val s = SOM(
        g,
        DistanceScalingFunction.exponentialDecreasing(),
        alpha = TimeFunction.linearDecreasingFactorScaled(initAlpha),
        sigma = TimeFunction.linearDecreasingFactorScaled(initSigma),
        rand = rand
    )

    logger.info { "Training..." }

    val start = System.currentTimeMillis()

    s.train(data, epochs)

    logger.info { "Train time: ${System.currentTimeMillis() - start} ms." }

    logger.info { "Predicting..." }

    val res = s.predict(
        arrayOf(
            doubleArrayOf(1.0, 0.0, 0.0),
            doubleArrayOf(0.0, 1.0, 0.0),
            doubleArrayOf(0.0, 0.0, 1.0)
        )
    )

    logger.info { "Writing image..." }

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
    val fileName = "img.png"

    Files.createDirectories(path.toPath())

    val out = File(path.resolve(fileName).toURI())
    ImageIO.write(im, "png", out)

    logger.info { "Done." }

}
