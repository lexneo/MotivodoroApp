package com.lexneoapps.motivodoroapp.ui.quotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.databinding.FragmentQuotesBinding
import com.lexneoapps.motivodoroapp.ui.viewmodels.StatsAndQuotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class QuotesFragment : Fragment(R.layout.fragment_quotes) {

    private var _binding: FragmentQuotesBinding? = null

    private val viewModel: StatsAndQuotesViewModel by viewModels()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}