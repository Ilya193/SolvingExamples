package ru.kraz.feature_game.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kraz.common.ResultFDS
import ru.kraz.feature_game.domain.GameRepository
import ru.kraz.feature_game.presentation.Utils.convertToTime
import ru.kraz.feature_game.presentation.Utils.getData
import ru.kraz.feature_game.presentation.Utils.toExampleUi
import ru.kraz.feature_game.presentation.Utils.toSolutionUi
import java.util.Timer
import java.util.TimerTask

class GameViewModel(
    private val router: GameRouter,
    private val repository: GameRepository
) : ViewModel() {

    private val examples = mutableListOf<ExampleUi>()
    private var solutions = mutableListOf<MutableList<SolutionUi>>()

    private val solvedExamples = mutableListOf<SolvedExample>()

    private val _vibrateState = MutableStateFlow(true)
    val vibrateState: StateFlow<Boolean> get() = _vibrateState

    private val _gameUiState = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val gameUiState: StateFlow<GameUiState> get() = _gameUiState

    private var sec = 0
    private var timer = Timer()
    private val _timerUiState = MutableStateFlow<TimerUiState>(TimerUiState.Loading)
    val timerUiState: StateFlow<TimerUiState> get() = _timerUiState

    private fun initTimer() {
        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                println("s149 Timer")
                if (sec == 3600) {
                    cancelTimer()
                    comeback()
                }
                else {
                    val displayTime = sec.convertToTime()
                    _timerUiState.value = TimerUiState.Tick(sec, displayTime)
                    sec++
                }
            }
        }, 1000, 1000)
    }

    fun initTimer(cacheTime: Int) {
        sec = cacheTime
    }

    fun cancelTimer() {
        timer.cancel()
    }

    fun init(id: Int, mode: Boolean) = viewModelScope.launch(Dispatchers.IO) {
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
                setGameState(solutions[0])
                if (mode) initTimer()
            }

            is ResultFDS.Error -> _gameUiState.value = GameUiState.Error(res.e.getData())
        }
    }

    fun select(page: Int, solutionIndex: Int) = viewModelScope.launch(Dispatchers.IO) {
        val solutionsOptions = solutions[page].toMutableList()
        if (solutionsOptions[solutionIndex].selected)
            solutionsOptions[solutionIndex] = solutionsOptions[solutionIndex].copy(selected = false)
        else {
            val value = !solutionsOptions[solutionIndex].selected
            for (i in 0..<solutionsOptions.size) {
                if (i == solutionIndex) solutionsOptions[i] = solutionsOptions[i].copy(selected = value)
                else solutionsOptions[i] = solutionsOptions[i].copy(selected = !value)
            }
        }
        solutions[page] = solutionsOptions
        setGameState(solutionsOptions)
    }

    fun answer(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        val prevPage = page - 1
        val solutionOptions = solutions[prevPage]
        val indexAnswer = solutionOptions.indexOfFirst { it.selected }
        if (page < solutions.size) {
            setVibrateState(prevPage, indexAnswer)
            setGameState(solutions[page])
        } else if (page == solutions.size) {
            cancelTimer()
            setVibrateState(prevPage, indexAnswer)
            setGameState(solutions[prevPage])
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    openGameResult()
                }
            }, 250)
        }
    }

    private fun setVibrateState(prevPage: Int, indexAnswer: Int) {
        val isCorrectAnswer = examples[prevPage].correctAnswer == indexAnswer
        solvedExamples[prevPage] = solvedExamples[prevPage].copy(solved = isCorrectAnswer)
        _vibrateState.value = isCorrectAnswer
    }

    private fun setGameState(solutions: MutableList<SolutionUi>) {
        _gameUiState.value =
            GameUiState.Success(
                examples = examples.toList(),
                solutions = solutions,
                solvedExamples.toList()
            )
    }

    private fun openGameResult() {
        val solved = solvedExamples.count { it.solved == true }
        val unSolved = solvedExamples.count { it.solved == false }
        router.openGameResult(solved, unSolved, sec)
    }

    fun comeback() = router.comeback()

    override fun onCleared() {
        super.onCleared()
        cancelTimer()
    }
}