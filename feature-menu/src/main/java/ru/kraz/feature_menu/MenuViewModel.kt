package ru.kraz.feature_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MenuViewModel(
    private val router: MenuRouter
) : ViewModel() {

    private val levels = mutableListOf<LevelUi>().apply {
        addAll((0..5).map {
            LevelUi(it, "$it")
        })
    }

    private val _uiState = MutableStateFlow(levels.toList())

    val uiState: StateFlow<List<LevelUi>> get() = _uiState

    fun expand(index: Int) = viewModelScope.launch(Dispatchers.IO) {
        val value = levels[index].expanded
        if (value) levels[index] = levels[index].copy(expanded = false)
        else {
            for (i in 0..<levels.size) {
                if (i == index)
                    levels[i] = levels[i].copy(expanded = !levels[index].expanded)
                else
                    levels[i] = levels[i].copy(expanded = false)
            }
        }
        _uiState.value = levels.toList()
    }

    fun start(id: Int, mode: Boolean) {
        router.openGame(id, mode)
    }

    fun coup() = router.coup()
}