package com.lexneoapps.motivodoroapp.ui.countdown

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.data.quote.Quote
import com.lexneoapps.motivodoroapp.databinding.FragmentCountdownTimerBinding
import com.lexneoapps.motivodoroapp.services.Countdown
import com.lexneoapps.motivodoroapp.services.CountdownService
import com.lexneoapps.motivodoroapp.services.SingletonProjectAttr
import com.lexneoapps.motivodoroapp.util.formatMillisToTimer
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CountDownFragment : Fragment(R.layout.fragment_countdown_timer) {

    private var _binding: FragmentCountdownTimerBinding? = null

    //    private lateinit var allQuotes: List<Quote>

    private val viewModel: CountDownViewModel by activityViewModels()

    private  var currentQuote : Quote? = null

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








        setupCountdownViews()
        setupListeners()
        setUpObservers()
//        onFirstRun()
    }



    override fun onResume() {
        super.onResume()
        binding.stopImageButton.isEnabled =
            !CountdownService.isTrackingCD.value!! && CountdownService.elapsedMilliSecondsCD.value!! != 0L
    }



    private fun setUpObservers() {
        CountdownService.timerReachedZero.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.getRandomQuote()
            }
        }

        viewModel.currentQuote.observe(viewLifecycleOwner){
            currentQuote = it
            binding.quoteAuthorTextView.text = it.author
            binding.quoteTextView.text = it.theQuote
            binding.favoritedImageButton.isSelected = it.favorite


        }

    }

    private fun setupCountdownViews() {

        binding.projectTitle.text = SingletonProjectAttr.projectName
        CountdownService.isTrackingCD.observe(viewLifecycleOwner) { isTracking ->
            binding.playImageButton.isEnabled = !isTracking
            binding.pauseImageButton.isEnabled = isTracking
            binding.stopImageButton.isEnabled = !isTracking
        }
        CountdownService.timeLeftMilliSecondsCD.observe(viewLifecycleOwner) { timeLeft ->
            binding.timerTextView.text = formatMillisToTimer(timeLeft)
            binding.stopImageButton.isEnabled = !CountdownService.isTrackingCD.value!!

        }

        CountdownService.elapsedMilliSecondsCD.observe(viewLifecycleOwner) {
            binding.linearProgressIndicator.progress = it.toInt()
        }
        CountdownService.breakProgress.observe(viewLifecycleOwner) {
            if (it) {
                binding.linearProgressIndicator.max = Countdown.shortBreak * 1000 * 60 + 1000
                binding.linearProgressIndicator.setIndicatorColor(Color.RED)
            } else {

                binding.linearProgressIndicator.max = Countdown.pomodoro * 1000 * 60 + 1000
                binding.linearProgressIndicator.setIndicatorColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.teal_200
                    )
                )
            }


        }

        CountdownService.roundsLeft.observe(viewLifecycleOwner) {
            binding.roundsLeftNumberTextView.text = it.toString()
        }
        CountdownService.isOver.observe(viewLifecycleOwner) {
            Timber.i("isOver is $it")
            if (it == true) {
                Toast.makeText(requireContext(), "Session finished!", Toast.LENGTH_SHORT).show()
                viewModel.setToFirstOpen(true)
                findNavController().navigateUp()
            }
        }


    }

    private fun setupListeners() {
        binding.apply {


            playImageButton.setOnClickListener {
                commandService(CountdownService.CDSERVICESTATE.START_OR_RESUME)
            }
            pauseImageButton.setOnClickListener {
                commandService(CountdownService.CDSERVICESTATE.PAUSE)
            }
            stopImageButton.setOnClickListener {


                commandService(CountdownService.CDSERVICESTATE.RESET)
                Toast.makeText(requireContext(), "Session finished!", Toast.LENGTH_SHORT).show()
                viewModel.setToFirstOpen(true)
                findNavController().navigateUp()

            }
            favoritedImageButton.setOnClickListener {

                    it.isSelected = !it.isSelected
                    if (it.isSelected) {

                        Toast.makeText(requireContext(), "Added to favorites!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Removed from favorites!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    viewModel.setFavorite( it.isSelected)
            }

        }
    }


    private fun commandService(servicestate: CountdownService.CDSERVICESTATE) {
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