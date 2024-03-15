package ru.kraz.feature_game.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kraz.common.ResultFDS
import ru.kraz.common.StringErrorProvider
import ru.kraz.feature_game.domain.GameRepository
import ru.kraz.feature_game.presentation.Utils.toExampleUi
import ru.kraz.feature_game.presentation.Utils.toSolutionUi

class GameViewModel(
    private val repository: GameRepository,
    private val errorProvider: StringErrorProvider,
) : ViewModel() {

    private val examples = mutableListOf<ExampleUi>()
    private var solutions = mutableListOf<MutableList<SolutionUi>>()

    private val _uiState = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val uiState: StateFlow<GameUiState> get() = _uiState

    fun init(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        when (val res = repository.fetchLevel(id)) {
            is ResultFDS.Success -> {
                examples.addAll(res.data.map { it.toExampleUi() })
                solutions.addAll(res.data.map {
                    it.solutions.map { solution ->
                        it.toSolutionUi(solution)
                    }.toMutableList()
                })
                _uiState.value = GameUiState.Success(examples = examples.toList(), solutions = solutions[0])
            }

            is ResultFDS.Error -> _uiState.value = GameUiState.Error(errorProvider.getData(res.e))
        }
    }

    fun select(page: Int, solutionIndex: Int) = viewModelScope.launch(Dispatchers.IO) {
        val list = solutions[page].toMutableList()
        if (list[solutionIndex].selected) list[solutionIndex] =
            list[solutionIndex].copy(selected = false)
        else {
            val value = !list[solutionIndex].selected
            for (i in 0..<list.size) {
                if (i == solutionIndex) list[i] = list[i].copy(selected = value)
                else list[i] = list[i].copy(selected = !value)
            }
        }
        solutions[page] = list
        _uiState.value = GameUiState.Success(examples = examples.toList(), solutions = list)
    }

    fun answer(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (page < solutions.size)
            _uiState.value = GameUiState.Success(examples = examples.toList(), solutions = solutions[page])

    }
}

data class ExampleUi(val id: Int, val example: String, val correctAnswer: Int)
data class SolutionUi(val id: Int, val solution: String, val selected: Boolean = false)

sealed interface GameUiState {
    data class Success(val examples: List<ExampleUi>, val solutions: List<SolutionUi>) :
        GameUiState

    data object Loading : GameUiState
    data class Error(val msg: Int) : GameUiState
}