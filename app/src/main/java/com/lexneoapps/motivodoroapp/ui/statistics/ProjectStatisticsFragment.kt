package com.lexneoapps.motivodoroapp.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.databinding.FragmentProjectStatisticsBinding
import com.lexneoapps.motivodoroapp.ui.quotes.QuotesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint

class ProjectStatisticsFragment : Fragment(R.layout.fragment_project_statistics) {

    private var _binding: FragmentProjectStatisticsBinding? = null

    private val viewModel: QuotesViewModel by viewModels()

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectStatisticsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}