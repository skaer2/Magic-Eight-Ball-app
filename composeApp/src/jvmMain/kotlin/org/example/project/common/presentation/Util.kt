package org.example.project.common.presentation

fun mapPredictionText(text: String): String {
    var boundedText = text.take(Constants.DEFAULT_ANSWER_LENGTH)
    val currentBoundaries = PredictionTextBoundaries.first {
        boundedText.length in it.first
    }
    val lines = currentBoundaries.second.map {
        val line = boundedText.take(it)
        boundedText = boundedText.drop(it)
        line
    }
    return lines.joinToString("\n")
}

private val PredictionTextBoundaries = listOf(
    0..8 to listOf(8),
    9..15 to listOf(9, 6),
    16..24 to listOf(11, 8, 5),
    25..30 to listOf(12, 9, 6, 3),
    31..40 to listOf(14, 11, 8, 5, 2),
)
