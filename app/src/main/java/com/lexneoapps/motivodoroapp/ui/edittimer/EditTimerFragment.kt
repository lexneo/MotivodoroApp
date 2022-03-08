package com.lexneoapps.motivodoroapp.ui.edittimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.databinding.FragmentEditTimerBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint

class EditTimerFragment : Fragment(R.layout.fragment_edit_timer) {

    private var _binding: FragmentEditTimerBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private val args: EditTimerFragmentArgs by navArgs()
    private val viewModel: EditTimerViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditTimerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCDTimerById(args.id)
        subscribeToObservers()
        setUpListeners()
        Timber.i("CDTIMER ID ${args.id}")


    }

    private fun saveCDTimer() {
        binding.apply {
            var countdown: Int? = null
            var pause: Int? = null
            var repeat: Int? = null
            var name: String? = null

            if (activityDurationEdit.text.isNotEmpty()) {
                if (activityDurationEdit.text.toString().toInt() >= 5) {
                    countdown = activityDurationEdit.text.toString().toInt()
                } else {
                    countdown = null
                    Toast.makeText(
                        requireContext(),
                        "Countdown should be minimum 5 minutes long!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else Toast.makeText(
                requireContext(),
                "Countdown shouldn't be empty!",
                Toast.LENGTH_SHORT
            ).show()
            if (breakTimeDurationEdit.text.isNotEmpty()) {
                if (breakTimeDurationEdit.text.toString().toInt() >= 1) {
                    pause = breakTimeDurationEdit.text.toString().toInt()

                } else {
                    pause = null
                    Toast.makeText(
                        requireContext(),
                        "Pause should be minimum 1 minute long!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else Toast.makeText(
                requireContext(),
                "Pause shouldn't be empty!",
                Toast.LENGTH_SHORT
            ).show()
            if (repeatTimesEdit.text.isNotEmpty()) {
                repeat = repeatTimesEdit.text.toString().toInt()
            } else Toast.makeText(
                requireContext(),
                "Repeat shouldn't be empty!",
                Toast.LENGTH_SHORT
            ).show()
            if (timerNameTextView.text.isNotEmpty()) {
                name = timerNameTextView.text.toString()
            } else Toast.makeText(
                requireContext(),
                "Name shouldn't be empty",
                Toast.LENGTH_SHORT
            ).show()






            if (countdown != null && pause != null
                && repeat != null && name != null
            ) {
                viewModel.saveCDTimer(countdown, pause, repeat, name, args.id)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Invalid countdown!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun setUpListeners() {
        binding.apply {
            saveTimerButton.setOnClickListener {
                saveCDTimer()
                val action = EditTimerFragmentDirections.actionEditTimerFragmentToProjectFragment()
                findNavController().navigate(action)
            }
        }
    }


    private fun subscribeToObservers() {
        binding.apply {

            viewModel.name.observe(viewLifecycleOwner) {
                timerNameTextView.setText(it.toString())
            }

            viewModel.countdown.observe(viewLifecycleOwner) {
                activityDurationEdit.setText(it.toString())

            }
            viewModel.pause.observe(viewLifecycleOwner) {
                breakTimeDurationEdit.setText(it.toString())

            }
            viewModel.repeat.observe(viewLifecycleOwner) {
                repeatTimesEdit.setText(it.toString())

            }


        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}