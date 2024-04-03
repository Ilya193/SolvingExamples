package ru.kraz.feature_game.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.kraz.common.BaseFragment
import ru.kraz.common.Constants
import ru.kraz.feature_game.R
import ru.kraz.feature_game.databinding.FragmentGameBinding

class GameFragment : BaseFragment<FragmentGameBinding>() {

    private val viewModel: GameViewModel by viewModel { parametersOf(id) }

    private val vibrator: Vibrator by lazy {
        requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private var id = Constants.DEFAULT_ID
    private var mode = false
    private var maxSec = Constants.MAX_SECONDS

    private var cacheTime = Constants.DEFAULT_CACHE_TIME

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.comeback()
        }
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentGameBinding =
        FragmentGameBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = requireArguments().getInt(KEY_ID)
        mode = requireArguments().getBoolean(KEY_MODE)
        maxSec = requireArguments().getInt(MAX_SECONDS)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) viewModel.comeback()
        else {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
            settingViewModel()
            settingClickListener()
        }
    }

    private fun settingViewModel() {
        val examplesAdapter = ExamplesAdapter()
        binding.examples.adapter = examplesAdapter
        binding.examples.isUserInputEnabled = false

        val solutionsAdapter = SolutionAdapter(select = {
            viewModel.select(binding.examples.currentItem, it)
        })
        binding.solutionOptions.adapter = solutionsAdapter

        val solvedAdapter = SolvedExamplesAdapter()
        binding.solvedList.adapter = solvedAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gameUiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                viewModel.gameUiState.collect {
                    binding.loading.visibility = if (it is GameUiState.Loading) View.VISIBLE else View.GONE
                    binding.btnAnswer.visibility = if (it is GameUiState.Success) View.VISIBLE else View.GONE
                    binding.examples.visibility = if (it is GameUiState.Success) View.VISIBLE else View.GONE
                    binding.solutionOptions.visibility = if (it is GameUiState.Success) View.VISIBLE else View.GONE
                    binding.containerError.visibility = if (it is GameUiState.Error) View.VISIBLE else View.GONE
                    binding.tvError.text = if (it is GameUiState.Error) getString(it.msg) else ""
                    binding.solvedList.visibility = if (it is GameUiState.Success) View.VISIBLE else View.GONE

                    if (it is GameUiState.Success) {
                        binding.btnAnswer.isEnabled = it.solutions.any { it.selected }
                        examplesAdapter.submitList(it.examples)
                        solutionsAdapter.submitList(it.solutions)
                        solvedAdapter.submitList(it.solvedExamples)
                    }
                }
            }
            viewModel.vibrateState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                if (!it) vibrate()
            }
            if (mode)
                viewModel.timerUiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                    if (it is TimerUiState.Tick) {
                        binding.icTimer.visibility = View.VISIBLE
                        binding.tvTime.visibility = View.VISIBLE
                        binding.tvTime.text = it.time
                        cacheTime = it.sec
                    }

                    if (it is TimerUiState.Finish) {
                        binding.btnAnswer.isEnabled = false
                        binding.tvTime.text = it.time
                        binding.tvTime.apply {
                            setTextColor(ContextCompat.getColor(context, R.color.red))
                        }
                    }
                }
        }

        viewModel.init(id, mode, maxSec)
    }

    private fun settingClickListener() {
        binding.btnAnswer.setOnClickListener {
            val newItem = binding.examples.currentItem + 1
            binding.examples.currentItem = newItem
            viewModel.answer(newItem)
        }
    }

    override fun onResume() {
        super.onResume()
        if (cacheTime != 0 && mode) viewModel.initTimer(cacheTime)
    }

    override fun onPause() {
        super.onPause()
        if (mode) viewModel.cancelTimer()
    }

    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= 26)
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    Constants.VIBRATE_MILLISECONDS,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        else vibrator.vibrate(Constants.VIBRATE_MILLISECONDS)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback.remove()
    }

    companion object {
        private const val KEY_ID = "KEY_ID"
        private const val KEY_MODE = "KEY_MODE"
        private const val MAX_SECONDS = "MAX_SECONDS"

        fun newInstance(id: Int, mode: Boolean, seconds: Int = 3600) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_ID, id)
                    putBoolean(KEY_MODE, mode)
                    putInt(MAX_SECONDS, seconds)
                }
            }
    }
}