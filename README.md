## Self-Organizing Maps in Kotlin

A *very* simple, lightweight Kotlin library to train self-organizing
maps [[1]](https://ieeexplore.ieee.org/abstract/document/58325) and experiment with grid topologies and parameters.

**Current functionality:**

- 2D/3D square/rectangular grids and 2D hexagonal grids
- New grids can easily be defined
- Arbitrary definitions of neighborhood functions, learning rate and neighborhood diameter possible
- Seeding for reproducibility

### Usage

Please understand that for now, the library is only available through your local maven repository. If you want to use or
test `som-kt`, clone this repository and build the artifact:

```
./gradlew publishToMavenLocal
```

Then, import the dependency into your project's `build.gradle`:

```
repositories {
    mavenLocal()
    ...
}

dependencies {
    implementation group: 'ch.unibas.dmi.dbis', name: 'som-kt', version: '0.0.0-SNAPSHOT'
    ...
}
```

### Example

A simple example of how to train a SOM using RGB vectors as samples and node weights.

```kotlin
import ch.unibas.dmi.dbis.som.SOM
import ch.unibas.dmi.dbis.som.functions.DistanceFunction
import ch.unibas.dmi.dbis.som.functions.NeighborhoodFunction
import ch.unibas.dmi.dbis.som.functions.TimeFunction
import ch.unibas.dmi.dbis.som.grids.Grid2DSquare
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import javax.imageio.ImageIO
import kotlin.math.max
import kotlin.math.sqrt
import kotlin.random.Random

fun main() {

    // Set seed for reproducibility.
    val rand = Random(0)

    // Generate some data samples.
    val dim = 3
    val size = 1000

    // 2D array of 1000 samples with 3 dimensions in range [0, 1].
    val data: Array<DoubleArray> = Array(size) { DoubleArray(dim) { rand.nextDouble() } }

    // Grid size (5x5).
    val width = 5
    val height = 5

    // Determine grid distance via squared Euclidean norm.
    val dist = DistanceFunction.squaredDistance()

    val g = Grid2DSquare(
        height,
        width,
        dist,
        rand
    )

    // Do not forget to initialize the weights of the nodes, or you will run into an exception.
    g.initializeWeights(dim)

    // Train for 1 epoch (= every sample presented once).
    val epochs = 1

    // Parameters: Alpha is the learning rate, sigma the neighborhood width.
    val initAlpha = 0.9
    val initSigma = 0.5 * sqrt((width * width + height * height).toDouble())
    val alpha = TimeFunction { t, T -> initAlpha * (1.0 - t.toDouble() / T) }
    val sigma = TimeFunction { t, T -> max(initSigma * ((T - t).toDouble() / T), 0.5) }

    //  Use default exponentially decreasing neighborhood function.
    val nf = NeighborhoodFunction.exponentialDecreasing()

    // Wrap the grid with a SOM object.
    val s = SOM(
        g,
        nf,
        alpha = alpha,
        sigma = sigma,
        rand = rand
    )

    // Do the training.
    s.train(data, epochs)

    // Create a 5x5 image and assign all node weights to the pixels.
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

    // Store the image as out.png.
    val path = File("data/")
    val fileName = "out"
    Files.createDirectories(path.toPath())

    val out = File(path.resolve("$fileName.png").toURI())
    ImageIO.write(im, "png", out)

}
```
