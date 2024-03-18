package ru.kraz.feature_setting_timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.kraz.feature_setting_timer.databinding.FragmentSettingTimerBinding

class SettingTimerFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSettingTimerBinding? = null
    private val binding get() = _binding!!

    private var exampleId = 0

    private val viewModel: SettingTimerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exampleId = requireArguments().getInt(EXAMPLE_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val min = 10
        binding.seconds.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress < min) binding.seconds.progress = 10
                else binding.tvSeconds.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.btnStart.setOnClickListener {
            val sec = binding.tvSeconds.text.toString().toInt()
            viewModel.openGame(exampleId, sec)
            dismiss()
        }
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    companion object {
        private const val EXAMPLE_ID = "EXAMPLE_ID"
        fun newInstance(exampleId: Int) =
            SettingTimerFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXAMPLE_ID, exampleId)
                }
            }
    }
}