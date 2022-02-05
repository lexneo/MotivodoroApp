package com.lexneoapps.motivodoroapp.ui.startandtimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.databinding.FragmentProjectBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class ProjectFragment : Fragment(R.layout.fragment_project) {

    private var _binding: FragmentProjectBinding? = null

//    val args: TimerFragmentArgs by navArgs()


    private val viewModel: SharedViewModel by viewModels()

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startTimerButton.setOnClickListener {
            viewModel.updateUIState(SharedViewModel.UIStatesEnum.RECORDING)
        }



    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}