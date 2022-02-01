package com.lexneoapps.motivodoroapp.ui.startandtimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.databinding.FragmentTimerBinding
import com.lexneoapps.motivodoroapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class TimerFragment : Fragment(R.layout.fragment_timer) {

    private var _binding: FragmentTimerBinding? = null

    val args: TimerFragmentArgs by navArgs()


    private val viewModel: MainViewModel by viewModels()
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var id = args.projectId
//        binding.title =
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}