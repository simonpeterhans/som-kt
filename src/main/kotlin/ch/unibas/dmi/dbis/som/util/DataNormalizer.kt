package ch.unibas.dmi.dbis.som.util

class DataNormalizer(
    private val dataRangeMin: Double,
    private val dataRangeMax: Double,
    private val normRangeMin: Double = 0.0,
    private val normRangeMax: Double = 1.0,
) {

    private val dataRange = dataRangeMax - dataRangeMin
    private val normRange = normRangeMax - normRangeMin

    fun normalize(sampleValue: Double): Double {
        return ((sampleValue - dataRangeMin) / dataRange) * (normRange) + normRangeMin
    }

}
