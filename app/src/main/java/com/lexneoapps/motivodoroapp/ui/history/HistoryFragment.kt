package com.lexneoapps.motivodoroapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.databinding.FragmentHistoryBinding
import com.lexneoapps.motivodoroapp.ui.adapters.HistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint

class HistoryFragment : Fragment(R.layout.fragment_history) {



    lateinit var historyAdapter: HistoryAdapter

    private var _binding: FragmentHistoryBinding? = null

    private val viewModel: HistoryViewModel by viewModels()

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRV()

        viewModel.getRecords.observe(viewLifecycleOwner) {
            historyAdapter.submitList(it)
        }


    }

    private fun setUpRV() = binding.recyclerView.apply {
        historyAdapter = HistoryAdapter(this@HistoryFragment.requireContext())
        adapter = historyAdapter
        layoutManager = LinearLayoutManager(this@HistoryFragment.requireContext())
        setHasFixedSize(true)
        Timber.d("adapter set")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}