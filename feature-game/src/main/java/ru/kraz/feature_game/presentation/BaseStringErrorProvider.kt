package ru.kraz.feature_game.presentation

import ru.kraz.common.ErrorType
import ru.kraz.common.StringErrorProvider
import ru.kraz.feature_game.R

class BaseStringErrorProvider : StringErrorProvider {
    override fun getData(e: ErrorType): Int {
        return when (e) {
            ErrorType.NO_CONNECTION -> R.string.no_connection
            ErrorType.GENERIC_ERROR -> R.string.generic_error
        }
    }
}