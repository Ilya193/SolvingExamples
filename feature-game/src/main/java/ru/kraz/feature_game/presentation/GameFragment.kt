package ru.kraz.feature_game.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.kraz.common.BaseFragment
import ru.kraz.feature_game.databinding.FragmentGameBinding

class GameFragment : BaseFragment<FragmentGameBinding>() {

    private val viewModel: GameViewModel by viewModel()

    private var id = -1
    private var mode = false

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentGameBinding =
        FragmentGameBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = requireArguments().getInt(KEY_ID)
        mode = requireArguments().getBoolean(KEY_MODE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) viewModel.comeback()
        else {
            val examplesAdapter = ExamplesAdapter()
            binding.examples.adapter = examplesAdapter
            binding.examples.isUserInputEnabled = false

            val solutionsAdapter = SolutionAdapter(select = {
                viewModel.select(page = binding.examples.currentItem, it)
            })
            binding.solutionOptions.adapter = solutionsAdapter

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect {
                        binding.loading.visibility = if (it is GameUiState.Loading) View.VISIBLE else View.GONE
                        binding.btnAnswer.visibility = if (it is GameUiState.Success) View.VISIBLE else View.GONE
                        binding.examples.visibility = if (it is GameUiState.Success) View.VISIBLE else View.GONE
                        binding.solutionOptions.visibility = if (it is GameUiState.Success) View.VISIBLE else View.GONE
                        binding.containerError.visibility = if (it is GameUiState.Error) View.VISIBLE else View.GONE

                        if (it is GameUiState.Success) {
                            examplesAdapter.submitList(it.examples)
                            solutionsAdapter.submitList(it.solutions)
                        }
                    }
                }
            }

            /*viewModel.uiState.observe(viewLifecycleOwner) {
                binding.loading.visibility = if (it is GameUiState.Loading) View.VISIBLE else View.GONE
                binding.btnAnswer.visibility = if (it is GameUiState.Success) View.VISIBLE else View.GONE
                binding.examples.visibility = if (it is GameUiState.Success) View.VISIBLE else View.GONE
                binding.solutionOptions.visibility = if (it is GameUiState.Success) View.VISIBLE else View.GONE
                binding.containerError.visibility = if (it is GameUiState.Error) View.VISIBLE else View.GONE

                if (it is GameUiState.Success) {
                    examplesAdapter.submitList(it.examples)
                    solutionsAdapter.submitList(it.testSolutions)
                }
            }*/

            viewModel.init(id)

            binding.btnAnswer.setOnClickListener {
                val newItem = binding.examples.currentItem + 1
                binding.examples.currentItem = newItem
                viewModel.answer(newItem)
            }
        }
    }

    companion object {
        private const val KEY_ID = "KEY_ID"
        private const val KEY_MODE = "KEY_MODE"

        fun newInstance(id: Int, mode: Boolean) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_ID, id)
                    putBoolean(KEY_MODE, mode)
                }
            }
    }
}