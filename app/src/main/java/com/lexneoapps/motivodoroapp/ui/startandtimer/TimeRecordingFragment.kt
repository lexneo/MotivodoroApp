package com.lexneoapps.motivodoroapp.ui.startandtimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.databinding.FragmentRecordingTimeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class TimeRecordingFragment : Fragment(R.layout.fragment_recording_time) {

    private var _binding: FragmentRecordingTimeBinding? = null

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
        _binding = FragmentRecordingTimeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




//        var id = args.projectId
//        binding.title =
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}