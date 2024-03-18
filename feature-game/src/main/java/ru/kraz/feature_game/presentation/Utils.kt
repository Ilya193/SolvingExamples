package ru.kraz.feature_game.presentation

import android.text.Spanned
import androidx.core.text.HtmlCompat
import ru.kraz.common.ErrorType
import ru.kraz.feature_game.R
import ru.kraz.feature_game.domain.ExampleDomain

object Utils {
    fun ExampleDomain.toExampleUi(): ExampleUi =
        ExampleUi(id, example, correctAnswer)

    fun ExampleDomain.toSolutionUi(solution: String): SolutionUi =
        SolutionUi(id, solution)

    fun Int.convertToTime(): String {
        var displayTime = ""
        if (this < 10) displayTime = "00:0$this"
        else if (this in 10..59) displayTime = "00:$this"
        else {
            val min = this / 60
            val sec = this % 60
            if (min < 10) {
                if (sec < 10) displayTime = "0$min:0$sec"
                else if (sec in 10..59) displayTime = "0$min:$sec"
            } else {
                if (sec < 10) displayTime = "$min:0$sec"
                else if (sec in 10..59) displayTime = "$min:$sec"
            }
        }
        return displayTime
    }

    fun ErrorType.getData(): Int {
        return when (this) {
            ErrorType.NO_CONNECTION -> R.string.no_connection
            ErrorType.GENERIC_ERROR -> R.string.generic_error
        }
    }

    fun textFromHtml(text: String, value: String): Spanned =
        HtmlCompat.fromHtml(
            String.format(
                text,
                value
            ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )
}
