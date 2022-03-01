package com.lexneoapps.motivodoroapp.ui.startandtimer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.databinding.FragmentProjectBinding
import com.lexneoapps.motivodoroapp.services.SingletonProjectAttr
import com.lexneoapps.motivodoroapp.services.StopwatchService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint

class ProjectFragment : Fragment(R.layout.fragment_project) {

    private var _binding: FragmentProjectBinding? = null

//    val args: TimerFragmentArgs by navArgs()


    private val viewModel: SharedViewModel by activityViewModels()

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
        subscribeToObservers()
        setUpBinding()
         if (StopwatchService.isTracking.value == true || StopwatchService.elapsedMilliSeconds.value!! > 1L){
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

    }

    private fun commandService(servicestate: StopwatchService.SERVICESTATE, string: String) {
        context?.let { context ->
            val intent = Intent(context, StopwatchService::class.java)
            intent.action = servicestate.name
            intent.putExtra("nameOfProject", string)
            context.startService(intent)
            SingletonProjectAttr.setName(string)
            val action = ProjectFragmentDirections.actionProjectFragmentToStopWatchFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}