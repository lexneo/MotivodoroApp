package com.lexneoapps.motivodoroapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.data.Project
import com.lexneoapps.motivodoroapp.data.ProjectDao
import com.lexneoapps.motivodoroapp.databinding.FragmentStartBinding
import com.lexneoapps.motivodoroapp.ui.adapters.ProjectAdapter
import com.lexneoapps.motivodoroapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {

    private var _binding: FragmentStartBinding? = null

    @Inject
    lateinit var projectDao: ProjectDao

    lateinit var projectList: List<Project>

    lateinit var projectAdapter: ProjectAdapter

    private val viewModel: MainViewModel by viewModels()
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        lifecycleScope.launch() {
            projectList = projectDao.listProjects()
            projectAdapter = ProjectAdapter()
            binding.recyclerView.adapter = projectAdapter
            projectAdapter.list = projectList

            Timber.d("onViewCreated: $projectList")
        }
/*
        viewModel.projects.observe(viewLifecycleOwner){
            projectAdapter.list = it
        }*/


        binding.recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.setHasFixedSize(true)

        binding.floatingActionButton.setOnClickListener{
            val action = StartFragmentDirections.actionStartFragmentToCreateProjectFragment()
            findNavController().navigate(action)
        }





    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}