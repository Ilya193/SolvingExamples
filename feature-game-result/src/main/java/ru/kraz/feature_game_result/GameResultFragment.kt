package ru.kraz.feature_game_result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.kraz.common.BaseFragment
import ru.kraz.common.Utils
import ru.kraz.feature_game_result.databinding.FragmentGameResultBinding

class GameResultFragment : BaseFragment<FragmentGameResultBinding>() {

    private val viewModel: GameResultViewModel by viewModel()

    private var id = -1
    private var solved = 0
    private var unSolved = 0
    private var time = 0
    private var levelPassed = false

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.openMenu(id)
        }
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentGameResultBinding =
        FragmentGameResultBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = requireArguments().getInt(LEVEL_ID)
        solved = requireArguments().getInt(SOLVED_EXAMPLES)
        unSolved = requireArguments().getInt(UNSOLVED_EXAMPLES)
        time = requireArguments().getInt(TIME_SPENT)
        levelPassed = requireArguments().getBoolean(LEVEL_PASSED)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.tvLevelPassed.apply {
            text =
                if (levelPassed) getString(R.string.level_passed) else getString(R.string.level_failed)
            val color = if (levelPassed) R.color.green else R.color.red
            setTextColor(ContextCompat.getColor(context, color))
        }

        binding.tvSolved.text = Utils.textFromHtml(getString(R.string.solved), solved.toString())
        binding.tvUnsolved.text =
            Utils.textFromHtml(getString(R.string.unsolved), unSolved.toString())
        if (time != 0) {
            binding.tvTimespent.visibility = View.VISIBLE
            binding.tvTimespent.text =
                Utils.textFromHtml(getString(R.string.timespent), time.toString())
        }

        binding.icHome.setOnClickListener {
            viewModel.openMenu(id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback.remove()
    }

    companion object {
        private const val LEVEL_ID = "LEVEL_ID"
        private const val SOLVED_EXAMPLES = "SOLVED_EXAMPLES"
        private const val UNSOLVED_EXAMPLES = "UNSOLVED_EXAMPLES"
        private const val TIME_SPENT = "TIME_SPENT"
        private const val LEVEL_PASSED = "LEVEL_PASSED"

        fun newInstance(
            levelId: Int,
            solved: Int,
            unSolved: Int,
            timeSpent: Int,
            levelPassed: Boolean,
        ) =
            GameResultFragment().apply {
                arguments = Bundle().apply {
                    putInt(LEVEL_ID, levelId)
                    putInt(SOLVED_EXAMPLES, solved)
                    putInt(UNSOLVED_EXAMPLES, unSolved)
                    putInt(TIME_SPENT, timeSpent)
                    putBoolean(LEVEL_PASSED, levelPassed)
                }
            }
    }
}