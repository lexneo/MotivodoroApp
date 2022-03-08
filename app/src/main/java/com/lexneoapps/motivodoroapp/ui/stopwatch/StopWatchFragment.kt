package com.lexneoapps.motivodoroapp.ui.stopwatch

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.databinding.FragmentStopwatchBinding
import com.lexneoapps.motivodoroapp.services.SingletonProjectAttr
import com.lexneoapps.motivodoroapp.services.StopwatchService
import com.lexneoapps.motivodoroapp.util.formatMillisToTimer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StopWatchFragment : Fragment(R.layout.fragment_stopwatch) {

    private var _binding: FragmentStopwatchBinding? = null

//    val args: TimerFragmentArgs by navArgs()


    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupViews()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        binding.stopImageButton.isEnabled =
            !StopwatchService.isTracking.value!! && StopwatchService.elapsedMilliSeconds.value!! != 0L
    }

    private fun setupViews() {
        binding.projectTitle.text = SingletonProjectAttr.projectName
        StopwatchService.isTracking.observe(viewLifecycleOwner) { isTracking ->
            binding.playImageButton.isEnabled = !isTracking
            binding.pauseImageButton.isEnabled = isTracking
            binding.stopImageButton.isEnabled =
                !isTracking && StopwatchService.elapsedMilliSeconds.value!! != 0L
        }
        StopwatchService.elapsedMilliSeconds.observe(viewLifecycleOwner) { elapsedMillis ->
            binding.stopwatchTextView.text = formatMillisToTimer(elapsedMillis)
            binding.stopImageButton.isEnabled =
                !StopwatchService.isTracking.value!! && elapsedMillis != 0L
        }
    }

    private fun setupListeners() {
        binding.playImageButton.setOnClickListener {
            commandService(StopwatchService.SERVICESTATE.START_OR_RESUME)
        }
        binding.pauseImageButton.setOnClickListener {
            commandService(StopwatchService.SERVICESTATE.PAUSE)
        }
        binding.stopImageButton.setOnClickListener {


            commandService(StopwatchService.SERVICESTATE.RESET)
            val action = StopWatchFragmentDirections.actionStopWatchFragmentToProjectFragment()
            findNavController().navigate(action)
        }
    }

    private fun commandService(servicestate: StopwatchService.SERVICESTATE) {
        context?.let { context ->
            val intent = Intent(context, StopwatchService::class.java)
            intent.action = servicestate.name
            context.startService(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}