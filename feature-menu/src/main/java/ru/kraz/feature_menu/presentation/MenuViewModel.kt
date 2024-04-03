package ru.kraz.feature_menu.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kraz.feature_menu.domain.MenuRepository
import ru.kraz.feature_menu.presentation.Utils.toLevelUi

class MenuViewModel(
    private val router: MenuRouter,
    private val repository: MenuRepository
) : ViewModel() {

    private var levels = mutableListOf<LevelUi>()

    private val _uiState = MutableStateFlow(listOf<LevelUi>())

    val uiState: StateFlow<List<LevelUi>> get() = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
             levels = repository.fetchLevels().map { it.toLevelUi() }.toMutableList()
            _uiState.value = levels.toList()
        }
    }

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

    fun settingTimer(id: Int) = router.openSettingTimer(id)

    fun coup() = router.coup()
}