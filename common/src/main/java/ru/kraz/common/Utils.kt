package ru.kraz.common

import android.text.Spanned
import androidx.core.text.HtmlCompat

object Utils {
    fun textFromHtml(text: String, value: String): Spanned =
        HtmlCompat.fromHtml(
            String.format(
                text,
                value
            ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )
}