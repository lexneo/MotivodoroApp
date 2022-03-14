package com.lexneoapps.motivodoroapp.ui.stopwatch

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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


    private val viewModel: StopWatchViewModel by activityViewModels()


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

        setupObservers()
        setupStopwatchObservers()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        binding.stopImageButton.isEnabled =
            !StopwatchService.isTracking.value!! && StopwatchService.elapsedMilliSeconds.value!! != 0L
    }

    private fun setupObservers() {
        viewModel.currentQuote.observe(viewLifecycleOwner) {
            binding.quoteAuthorTextView.text = it.author
            binding.quoteTextView.text = it.theQuote
            binding.favoritedImageButton.isSelected = it.favorite
        }


    }

    private fun setupStopwatchObservers() {
        binding.projectTitle.text = SingletonProjectAttr.projectName
        StopwatchService.isTracking.observe(viewLifecycleOwner) { isTracking ->
            binding.apply {
                playImageButton.isEnabled = !isTracking
                pauseImageButton.isEnabled = isTracking
                stopImageButton.isEnabled =
                    !isTracking && StopwatchService.elapsedMilliSeconds.value!! != 0L
            }
        }
        StopwatchService.elapsedMilliSeconds.observe(viewLifecycleOwner) { elapsedMillis ->
            binding.stopwatchTextView.text = formatMillisToTimer(elapsedMillis)
            binding.stopImageButton.isEnabled =
                !StopwatchService.isTracking.value!! && elapsedMillis != 0L
        }
    }

    private fun setupListeners() {
        binding.apply {
            playImageButton.setOnClickListener {
                commandService(StopwatchService.SERVICESTATE.START_OR_RESUME)
            }
            pauseImageButton.setOnClickListener {
                viewModel.getRandomQuote()
                commandService(StopwatchService.SERVICESTATE.PAUSE)

            }
            stopImageButton.setOnClickListener {


                commandService(StopwatchService.SERVICESTATE.RESET)
                val action = StopWatchFragmentDirections.actionStopWatchFragmentToProjectFragment()
                findNavController().navigate(action)
            }
            favoritedImageButton.setOnClickListener {

                it.isSelected = !it.isSelected
                if (it.isSelected) {

                    Toast.makeText(requireContext(), "Added to favorites!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "Removed from favorites!", Toast.LENGTH_SHORT)
                        .show()
                }
                viewModel.setFavorite(it.isSelected)
            }
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