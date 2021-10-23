package com.lexneoapps.motivodoroapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.databinding.FragmentEditHistoryRecordBinding
import com.lexneoapps.motivodoroapp.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint


//@AndroidEntryPoint

class EditHistoryRecordFragment : Fragment(R.layout.fragment_edit_history_record) {

    private var _binding: FragmentEditHistoryRecordBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditHistoryRecordBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}