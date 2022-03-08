package com.lexneoapps.motivodoroapp.ui.project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.data.cdtimer.CDTimerDao
import com.lexneoapps.motivodoroapp.databinding.FragmentProjectBinding
import com.lexneoapps.motivodoroapp.services.Countdown
import com.lexneoapps.motivodoroapp.services.CountdownService
import com.lexneoapps.motivodoroapp.services.SingletonProjectAttr
import com.lexneoapps.motivodoroapp.services.StopwatchService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint

class ProjectFragment : Fragment(R.layout.fragment_project) {

    private var _binding: FragmentProjectBinding? = null

//    val args: TimerFragmentArgs by navArgs()


    private val viewModel: ProjectViewModel by viewModels()

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var cdTimerDao: CDTimerDao

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
        subscribeToObservers()
        setUpBinding()
        if (StopwatchService.isTracking.value == true || StopwatchService.elapsedMilliSeconds.value!! > 1L) {
            findNavController().navigateUp()
        }

/*        binding.startTimerButton.setOnClickListener {
//            viewModel.updateUIState(SharedViewModel.UIStatesEnum.RECORDING)
            Timber.i("Clicked StartTimer")
            commandService(
                StopwatchService.SERVICESTATE.START_OR_RESUME,
              binding.title.text.toString()
            )

        }*/


    }


    private fun subscribeToObservers() {
        viewModel.cdTimers.observe(viewLifecycleOwner) {
            binding.apply {
                it.toList()
                Timber.d("CDTIMERS ${it}")
                countdownTimerTextView.text = it[0].name
                activityTimeTextView.text = it[0].countdown.toString()
                pauseTimeTextView.text = it[0].pause.toString()
                repeatTimeTextView.text = it[0].repeat.toString()

                countdownTimerTextView2.text = it[1].name
                activityTimeTextView2.text = it[1].countdown.toInt().toString()
                pauseTimeTextView2.text = it[1].pause.toInt().toString()
                repeatTimeTextView2.text = it[1].repeat.toString()

                countdownTimerTextView3.text = it[2].name
                activityTimeTextView3.text = it[2].countdown.toInt().toString()
                pauseTimeTextView3.text = it[2].pause.toInt().toString()
                repeatTimeTextView3.text = it[2].repeat.toString()
            }


        }

    }

    private fun setUpBinding() {
        binding.apply {
            startTimerButton.setOnClickListener {
                Timber.i("Clicked StartTimer")

                commandService(
                    StopwatchService.SERVICESTATE.START_OR_RESUME,
                    binding.title.text.toString()
                )
            }
            title.text = SingletonProjectAttr.projectName

        }
        binding.apply {
            playButton1.setOnClickListener {
                Countdown.setCountDown(
                    activityTimeTextView.text.toString().toInt(),
                    repeatTimeTextView.text.toString().toInt(),
                    pauseTimeTextView.text.toString().toInt()
                )

                startCountdownTimer()
            }
            playButton2.setOnClickListener {
                Countdown.setCountDown(
                    activityTimeTextView2.text.toString().toInt(),
                    repeatTimeTextView2.text.toString().toInt(),
                    pauseTimeTextView2.text.toString().toInt()
                )
                startCountdownTimer()
            }
            playButton3.setOnClickListener {
                Countdown.setCountDown(
                    activityTimeTextView3.text.toString().toInt(),
                    repeatTimeTextView3.text.toString().toInt(),
                    pauseTimeTextView3.text.toString().toInt()
                )
                startCountdownTimer()
            }
            timerCardView1.setOnClickListener {
                editTimer(0)
            }
            timerCardView2.setOnClickListener {
                editTimer(1)
            }
            timerCardView3.setOnClickListener {
                editTimer(2)
            }
        }

    }

    private fun editTimer(id: Int) {
        val action = ProjectFragmentDirections.actionProjectFragmentToEditTimerFragment(id)
        findNavController().navigate(action)
    }


    private fun startCountdownTimer() {
        commandCDService(CountdownService.CDSERVICESTATE.START_OR_RESUME)
        val action = ProjectFragmentDirections.actionProjectFragmentToCountDownFragment()
        findNavController().navigate(action)
    }

    private fun commandService(servicestate: StopwatchService.SERVICESTATE, string: String) {
        context?.let { context ->
            val intent = Intent(context, StopwatchService::class.java)
            intent.action = servicestate.name
            intent.putExtra("nameOfProject", string)
            context.startService(intent)
            val action = ProjectFragmentDirections.actionProjectFragmentToStopWatchFragment()
            findNavController().navigate(action)
        }
    }

    private fun commandCDService(servicestate: CountdownService.CDSERVICESTATE) {
        context?.let { context ->
            val intent = Intent(context, CountdownService::class.java)
            intent.action = servicestate.name
            context.startService(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}