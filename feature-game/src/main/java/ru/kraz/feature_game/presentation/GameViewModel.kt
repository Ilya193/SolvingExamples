package ru.kraz.feature_game.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kraz.common.ResultFDS
import ru.kraz.common.StringErrorProvider
import ru.kraz.feature_game.domain.GameRepository
import ru.kraz.feature_game.presentation.Utils.toExampleUi
import ru.kraz.feature_game.presentation.Utils.toSolutionsUi

class GameViewModel(
    private val repository: GameRepository,
    private val errorProvider: StringErrorProvider
) : ViewModel() {

    private val examples = mutableListOf<ExampleUi>()
    private val solutions = mutableListOf<SolutionsUi>()

    private val _uiState = MutableLiveData<GameUiState>(GameUiState.Loading)
    val uiState: LiveData<GameUiState> get() = _uiState

    fun init(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        when (val res = repository.fetchLevel(id)) {
            is ResultFDS.Success -> {
                examples.addAll(res.data.map { it.toExampleUi() })
                solutions.addAll(res.data.map { it.toSolutionsUi() })
                _uiState.postValue(GameUiState.Success(examples = examples.toList(), solutions = solutions.toList()))
            }
            is ResultFDS.Error -> _uiState.postValue(GameUiState.Error(errorProvider.getData(res.e)))
        }
    }

    fun select(solutionIndex: Int) = viewModelScope.launch(Dispatchers.IO) {
        // 0 0
        val newValue = solutions[0].solutions[0].copy(selected = true)
        val temp = solutions.map {
            it.copy(solutions = it.solutions.map {
                if (it.id == 0) newValue
                it.copy()
            })
        }
        _uiState.postValue(GameUiState.Success(examples = examples.toList(), solutions = temp.toList()))
    }

    fun answer() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.postValue(GameUiState.Success(examples = examples.toList(), solutions = solutions.toList()))
    }
}

data class ExampleUi(val id: Int, val example: String, val correctAnswer: Int)
data class SolutionsUi(val id: Int, val solutions: List<SolutionUi>)
data class SolutionUi(val id: Int, val solution: String, val selected: Boolean = false)

sealed interface GameUiState {
    data class Success(val examples: List<ExampleUi>, val solutions: List<SolutionsUi>) : GameUiState
    data object Loading : GameUiState
    data class Error(val msg: Int) : GameUiState
}