package ru.kraz.solvingexamples

import androidx.lifecycle.ViewModel

class MainViewModel(
    private val navigation: Navigation<Screen>
) : ViewModel() {

    fun init(first: Boolean) {
        if (first) navigation.update(MenuScreen())
    }

    fun read() = navigation.read()
}