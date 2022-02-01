package com.lexneoapps.motivodoroapp.ui.startandtimer

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.data.project.Project
import com.lexneoapps.motivodoroapp.data.project.ProjectDao
import com.lexneoapps.motivodoroapp.data.SortOrder
import com.lexneoapps.motivodoroapp.databinding.FragmentStartBinding
import com.lexneoapps.motivodoroapp.ui.adapters.ProjectAdapter
import com.lexneoapps.motivodoroapp.ui.viewmodels.MainViewModel
import com.lexneoapps.motivodoroapp.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
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

        setUpRV()

        viewModel.projects.observe(viewLifecycleOwner) {
            projectAdapter.list = it
        }

        binding.floatingActionButton.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToCreateProjectFragment()
            findNavController().navigate(action)
        }

        projectAdapter.setOnItemClickListener {
            findNavController().navigate(
                StartFragmentDirections.actionStartFragmentToTimerFragment(it.id)
            )
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

        // Set the item state
        viewLifecycleOwner.lifecycleScope.launch {
            val isChecked = viewModel.getUIMode.first()
            val item = menu.findItem(R.id.action_dark_mode)
            item.isChecked = isChecked
            setUIMode(isChecked)
            Timber.d("onCreateOptionsMenu:  $isChecked")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_recently_tracked -> {
                viewModel.onSortOrderSelected(SortOrder.BY_RECENT)
                true
            }


            R.id.action_total_time -> {
                viewModel.onSortOrderSelected(SortOrder.BY_TOTAL)
                true
            }
            R.id.action_sort_by_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }

            R.id.action_dark_mode -> {
                item.isChecked = !item.isChecked
                setUIMode(item.isChecked)

                true
            }

            R.id.action_settings -> {
                TODO()

            }
            R.id.action_about -> {
                TODO()
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpRV() = binding.recyclerView.apply {
        projectAdapter = ProjectAdapter()
        adapter = projectAdapter
        layoutManager = LinearLayoutManager(this@StartFragment.requireContext())
        setHasFixedSize(true)
        Timber.d("adapter set")

    }

    //set the dark(er:)) mode and save it via datastore
    private fun setUIMode(boolean: Boolean) {
        if (boolean) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            viewModel.saveToDataStore(true)

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            viewModel.saveToDataStore(false)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}