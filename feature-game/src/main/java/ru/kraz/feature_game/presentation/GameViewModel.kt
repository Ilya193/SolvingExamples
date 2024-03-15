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
    private val router: GameRouter,
    private val repository: GameRepository,
    private val errorProvider: StringErrorProvider,
) : ViewModel() {

    private val examples = mutableListOf<ExampleUi>()
    private var solutions = mutableListOf<MutableList<SolutionUi>>()

    private val solvedExamples = mutableListOf<SolvedExample>()

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
                solvedExamples.addAll((0..<solutions.size).map {
                    SolvedExample(it, "${it + 1}")
                })
                _uiState.value = GameUiState.Success(
                    examples = examples.toList(),
                    solutions = solutions[0],
                    solvedExamples = solvedExamples
                )
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
        _uiState.value = GameUiState.Success(
            examples = examples.toList(),
            solutions = list,
            solvedExamples.toList()
        )
    }

    fun answer(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        val list = solutions[page - 1]
        val answer = list.first { it.selected }
        val index = list.indexOf(answer)
        if (page < solutions.size) {
            if (examples[page - 1].correctAnswer == index)
                solvedExamples[page - 1] = solvedExamples[page - 1].copy(solved = true)
            else solvedExamples[page - 1] = solvedExamples[page - 1].copy(solved = false)
            _uiState.value =
                GameUiState.Success(
                    examples = examples.toList(),
                    solutions = solutions[page],
                    solvedExamples.toList()
                )
        }
    }

    fun comeback() = router.comeback()
}