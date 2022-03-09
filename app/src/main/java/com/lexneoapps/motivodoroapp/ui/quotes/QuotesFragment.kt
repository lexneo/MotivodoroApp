package com.lexneoapps.motivodoroapp.ui.quotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.data.quote.Quote
import com.lexneoapps.motivodoroapp.databinding.FragmentQuotesBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.random.Random

@AndroidEntryPoint

class QuotesFragment : Fragment(R.layout.fragment_quotes) {

    private var _binding: FragmentQuotesBinding? = null

    private val viewModel: QuotesViewModel by viewModels()

    private lateinit var unlockedQuotes: List<Quote>
    private lateinit var favoriteQuotes: List<Quote>
    private lateinit var currentQuote: Quote
    private var firstOpeningOfFragment = 1

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuotesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setListeners()


    }

    private fun setObservers() {
        viewModel.unlockedQuotes.observe(viewLifecycleOwner) {
            unlockedQuotes = it
            currentQuote = unlockedQuotes.first()
            if (firstOpeningOfFragment == 1) {
                randomUnlockedQuote()
                firstOpeningOfFragment++
            }

            Timber.i("Unlocked $it")
        }

        viewModel.favoriteQuotes.observe(viewLifecycleOwner) {
            favoriteQuotes = it
            Timber.i("Unlocked $it")
        }
    }

    private fun setListeners() {
        binding.favoritedImageButton.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {

                Toast.makeText(requireContext(), "Added to favorites!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Removed from favorites!", Toast.LENGTH_SHORT)
                    .show()
            }
            viewModel.setFavorite(currentQuote, it.isSelected)

            Timber.i("isSelected ${it.isSelected}")
        }
        binding.getFavoritesButton.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                randomFavoriteQuote()
                Toast.makeText(requireContext(), "Switched to favorite quotes!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                randomUnlockedQuote()
                Toast.makeText(
                    requireContext(),
                    "Switched to all unlocked quotes!",
                    Toast.LENGTH_SHORT
                ).show()

            }
            Timber.i("itView ${it.isSelected}")
        }
        binding.randomButton.setOnClickListener {
            getQuote()
        }
    }

    private fun getQuote() {
        if (binding.getFavoritesButton.isSelected) {
            randomFavoriteQuote()
        } else {
            randomUnlockedQuote()
        }
    }


    private fun randomUnlockedQuote() {
        val random = Random.nextInt(unlockedQuotes.size)
        val quote = unlockedQuotes[random]
        currentQuote = quote
        binding.apply {
            quoteTextView.text = quote.theQuote
            quoteAuthorTextView.text = quote.author
            favoritedImageButton.isSelected = quote.favorite

        }
    }

    private fun randomFavoriteQuote() {
        if (favoriteQuotes.isNullOrEmpty()) {
            randomUnlockedQuote()
            Toast.makeText(
                requireContext(),
                "No favorite quotes, switched to all unlocked quotes!",
                Toast.LENGTH_SHORT
            ).show()
            binding.getFavoritesButton.isSelected = false
        } else {
            val random = Random.nextInt(favoriteQuotes.size)
            val quote = favoriteQuotes[random]
            currentQuote = quote
            binding.apply {
                quoteTextView.text = quote.theQuote
                quoteAuthorTextView.text = quote.author
                favoritedImageButton.isSelected = quote.favorite

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}