package ru.kraz.solvingexamples

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    private val navigation: Navigation<Screen>
) : ViewModel() {

    fun init(first: Boolean) {
        if (first) navigation.update(MenuScreen())
    }

    fun read(): StateFlow<Screen> = navigation.read()
}