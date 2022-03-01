package com.lexneoapps.motivodoroapp.ui.startandtimer

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.data.SortOrder
import com.lexneoapps.motivodoroapp.data.project.Project
import com.lexneoapps.motivodoroapp.databinding.FragmentStartBinding
import com.lexneoapps.motivodoroapp.services.SingletonProjectAttr
import com.lexneoapps.motivodoroapp.services.StopwatchService
import com.lexneoapps.motivodoroapp.ui.adapters.ProjectAdapter
import com.lexneoapps.motivodoroapp.util.formatMillisToTimer
import com.lexneoapps.motivodoroapp.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {

    private var _binding: FragmentStartBinding? = null


    lateinit var projectList: List<Project>

    lateinit var projectAdapter: ProjectAdapter

    private val viewModel: SharedViewModel by activityViewModels()

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
        setUpBindings()

        viewModel.projects.observe(viewLifecycleOwner) {
            projectAdapter.submitList(it)
        }


//        projectAdapter.setOnItemClickListener {
//            findNavController().navigate(
//                StartFragmentDirections.actionStartFragmentToTimerFragment(it.id)
//            )
//        }

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

    private fun setUpBindings() {
        binding.floatingActionButton.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToCreateProjectFragment()
            findNavController().navigate(action)
        }
        viewsVisibility()
    }

    private fun viewsVisibility() {
        if (StopwatchService.isTracking.value == true || StopwatchService.elapsedMilliSeconds.value!! > 0L) {
            binding.apply {
                startGroup.visibility = View.GONE
                trackingCardView.visibility = View.VISIBLE
                trackingCardView.setOnClickListener {
                    val action = StartFragmentDirections.actionStartFragmentToStopWatchFragment()
                    findNavController().navigate(action)
                }
                currentProjectLayout.setBackgroundColor(SingletonProjectAttr.projectColor)
                currentProjectNameTextView.text = SingletonProjectAttr.projectName
                StopwatchService.elapsedMilliSeconds.observe(viewLifecycleOwner) { elapsedMillis ->
                    currentTimeTextView.text = formatMillisToTimer(elapsedMillis)
                }

            }
        } else {
            binding.apply {
                trackingCardView.visibility = View.GONE
                startGroup.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpRV() = binding.recyclerView.apply {
        projectAdapter = ProjectAdapter()
        adapter = projectAdapter
        layoutManager = LinearLayoutManager(this@StartFragment.requireContext())
        setHasFixedSize(true)
        projectAdapter.setOnItemClickListener {
            SingletonProjectAttr.projectName = it.name
            SingletonProjectAttr.projectColor = it.color
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToProjectFragment())
        }

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