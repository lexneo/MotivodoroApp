package com.lexneoapps.motivodoroapp.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
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
import com.lexneoapps.motivodoroapp.ui.viewmodels.SortOrder
import com.lexneoapps.motivodoroapp.util.onQueryTextChanged
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

        projectAdapter = ProjectAdapter()
        setAdapter()

        viewModel.projects.observe(viewLifecycleOwner) {
            projectAdapter.list = it
        }

        binding.floatingActionButton.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToCreateProjectFragment()
            findNavController().navigate(action)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_start, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            // update search query
            viewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_recently_tracked -> {
                viewModel.sortOrder.value = SortOrder.BY_RECENT
                true
            }


            R.id.action_total_time -> {
                viewModel.sortOrder.value = SortOrder.BY_TOTAL
                true
            }
            R.id.action_sort_by_name -> {
                viewModel.sortOrder.value = SortOrder.BY_NAME
                true
            }

            R.id.action_dark_mode -> {
                item.isChecked = !item.isChecked
              /*  if(item.isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }*/
                true
            }

            R.id.action_settings ->{
                TODO()

            }
            R.id.action_about ->{
                TODO()
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setAdapter() = binding.recyclerView.apply {
        adapter = projectAdapter
        layoutManager = LinearLayoutManager(this@StartFragment.requireContext())
        setHasFixedSize(true)
        Timber.d("adapter set")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}