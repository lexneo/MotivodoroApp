package com.lexneoapps.motivodoroapp.ui.startandtimer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.databinding.FragmentCountdownTimerBinding
import com.lexneoapps.motivodoroapp.databinding.FragmentStopwatchBinding
import com.lexneoapps.motivodoroapp.services.StopwatchService
import com.lexneoapps.motivodoroapp.util.formatMillisToTimer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountDownFragment : Fragment(R.layout.fragment_countdown_timer) {

    private var _binding: FragmentCountdownTimerBinding? = null

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
        _binding = FragmentCountdownTimerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}