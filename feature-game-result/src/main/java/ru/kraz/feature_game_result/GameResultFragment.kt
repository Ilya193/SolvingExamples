package ru.kraz.feature_game_result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.kraz.common.BaseFragment
import ru.kraz.common.Utils
import ru.kraz.feature_game_result.databinding.FragmentGameResultBinding

class GameResultFragment : BaseFragment<FragmentGameResultBinding>() {

    private val viewModel: GameResultViewModel by viewModel()

    private var solved = 0
    private var unSolved = 0
    private var time = 0

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentGameResultBinding =
        FragmentGameResultBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        solved = requireArguments().getInt(SOLVED_EXAMPLES)
        unSolved = requireArguments().getInt(UNSOLVED_EXAMPLES)
        time = requireArguments().getInt(TIME_SPENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSolved.text = Utils.textFromHtml(getString(R.string.solved), solved.toString())
        binding.tvUnsolved.text = Utils.textFromHtml(getString(R.string.unsolved), unSolved.toString())
        if (time != 0) {
            binding.tvTimespent.visibility = View.VISIBLE
            binding.tvTimespent.text = Utils.textFromHtml(getString(R.string.timespent), time.toString())
        }

        binding.icHome.setOnClickListener {
            viewModel.openMenu()
        }
    }

    companion object {
        private const val SOLVED_EXAMPLES = "SOLVED_EXAMPLES"
        private const val UNSOLVED_EXAMPLES = "UNSOLVED_EXAMPLES"
        private const val TIME_SPENT = "TIME_SPENT"

        fun newInstance(solved: Int, unSolved: Int, timeSpent: Int) =
            GameResultFragment().apply {
                arguments = Bundle().apply {
                    putInt(SOLVED_EXAMPLES, solved)
                    putInt(UNSOLVED_EXAMPLES, unSolved)
                    putInt(TIME_SPENT, timeSpent)
                }
            }
    }
}