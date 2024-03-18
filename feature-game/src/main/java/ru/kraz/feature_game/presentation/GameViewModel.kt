package ru.kraz.feature_game.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kraz.common.Constants
import ru.kraz.common.ResultFDS
import ru.kraz.feature_game.domain.GameRepository
import ru.kraz.feature_game.presentation.Utils.convertToTime
import ru.kraz.feature_game.presentation.Utils.getData
import ru.kraz.feature_game.presentation.Utils.toExampleUi
import ru.kraz.feature_game.presentation.Utils.toSolutionUi
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.schedule
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.concurrent.timerTask

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

    private var levelId = Constants.DEFAULT_ID
    private var sec = 0
    private var maxSec = Constants.MAX_SECONDS
    private var timer = Timer()
    private val _timerUiState = MutableStateFlow<TimerUiState>(TimerUiState.Loading)
    val timerUiState: StateFlow<TimerUiState> get() = _timerUiState

    private fun initTimer() {
        timer = Timer()
        timer.scheduleAtFixedRate(
            0,
            Constants.PERIOD_TIMER
        ) {
            val displayTime = sec.convertToTime()
            if (sec == maxSec) {
                cancelTimer()
                _timerUiState.value = TimerUiState.Finish(displayTime)
                Timer().schedule(Constants.PERIOD_TIMER) {
                    openGameResult()
                }
            } else {
                _timerUiState.value = TimerUiState.Tick(sec, displayTime)
                sec++
            }
        }
    }

    fun initTimer(cacheTime: Int) {
        sec = cacheTime
    }

    fun cancelTimer() {
        timer.cancel()
    }

    fun init(id: Int, mode: Boolean, maxSec: Int) = viewModelScope.launch(Dispatchers.IO) {
        levelId = id
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
                if (mode) {
                    this@GameViewModel.maxSec = maxSec
                    initTimer()
                }
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
                if (i == solutionIndex) solutionsOptions[i] =
                    solutionsOptions[i].copy(selected = value)
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
            Timer().schedule(Constants.DELAY_250L) {
                openGameResult()
            }
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
        val levelPassed = solved == solvedExamples.size && unSolved == 0
        router.openGameResult(levelId, solved, unSolved, sec, levelPassed)
    }

    fun comeback() = router.comeback()

    override fun onCleared() {
        super.onCleared()
        cancelTimer()
    }
}