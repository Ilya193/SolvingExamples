package ru.kraz.feature_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.kraz.common.BaseFragment
import ru.kraz.feature_menu.databinding.FragmentMenuBinding

class MenuFragment : BaseFragment<FragmentMenuBinding>() {

    private val viewModel: MenuViewModel by viewModel()

    private var passedLevelId: Int = -1

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentMenuBinding =
        FragmentMenuBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        passedLevelId = requireArguments().getInt(PASSED_LEVEL_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (passedLevelId != -1) viewModel.levelPassed(passedLevelId)

        val adapter = LevelsAdapter(
            expand = viewModel::expand,
            start = viewModel::start,
            settingTimer = viewModel::settingTimer
        )
        binding.levels.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    println("s149 $passedLevelId")
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.coup()
    }

    companion object {
        private const val PASSED_LEVEL_ID = "PASSED_LEVEL_ID"

        fun newInstance(passedLevelId: Int = -1) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putInt(PASSED_LEVEL_ID, passedLevelId)
                }
            }
    }
}