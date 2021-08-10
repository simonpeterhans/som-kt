package ch.unibas.dmi.dbis.som

/**
 * Convenience class for Pair<Int, Double>, used as a prediction result for a sample.
 *
 * @property nodeId The ID of the predicted node.
 * @property distance The distance of the sample to the predicted node.
 */
data class PredictionResult(val nodeId: Int, val distance: Double)
