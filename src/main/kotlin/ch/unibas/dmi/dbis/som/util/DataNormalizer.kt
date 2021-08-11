package ch.unibas.dmi.dbis.som.util

/**
 * Scales values from one range to another, can be used to normalize features.
 * For instance, to scale features with values ranging from [0, 255] to [0, 1], use a normalizer like
 * DataNormalizer(0.0, 255.0, 0.0, 1.0) and call normalize() for all samples.
 *
 * @property dataRangeMin Minimum value occurring in the data.
 * @property dataRangeMax Maximum value occurring in the data.
 * @property normRangeMin Minimum value of the range to map the data to.
 * @property normRangeMax Maximum value of the range to map the data to.
 */
class DataNormalizer(
    private val dataRangeMin: Double,
    private val dataRangeMax: Double,
    private val normRangeMin: Double = 0.0,
    private val normRangeMax: Double = 1.0,
) {

    companion object {
        /**
         * Creates a data normalizer from a series of samples (e.g., all R values for pixels) based on the values encountered.
         *
         * @param samples The samples to calculate the range for.
         * @param normRangeMin The normalized range minimum.
         * @param normRangeMax The normalized range maximum.
         * @return The resulting DataNormalizer object.
         */
        fun fromData(samples: Array<Double>, normRangeMin: Double, normRangeMax: Double): DataNormalizer {
            var min = Double.MAX_VALUE
            var max = Double.MIN_VALUE

            for (s in samples) {
                if (s < min) {
                    min = s
                }

                if (s > max) {
                    max = s
                }
            }

            return DataNormalizer(min, max, normRangeMin, normRangeMax)
        }
    }

    private val dataRange = dataRangeMax - dataRangeMin
    private val normRange = normRangeMax - normRangeMin

    /**
     * Normalizes a given value according to the specified ranges.
     *
     * @param sampleValue The value to normalize.
     * @return The normalized value.
     */
    fun normalize(sampleValue: Double): Double {
        return ((sampleValue - dataRangeMin) / dataRange) * (normRange) + normRangeMin
    }

}
