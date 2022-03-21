package com.lexneoapps.motivodoroapp.ui.editproject

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.data.project.Project
import com.lexneoapps.motivodoroapp.data.project.ProjectDao
import com.lexneoapps.motivodoroapp.databinding.FragmentEditProjectBinding
import com.lexneoapps.motivodoroapp.other.Constants
import com.lexneoapps.motivodoroapp.services.SingletonProjectAttr
import com.lexneoapps.motivodoroapp.ui.ColorPickerDialogFragment
import com.lexneoapps.motivodoroapp.ui.createproject.CreateProjectFragmentDirections
import com.lexneoapps.motivodoroapp.ui.createproject.FRAGMENT_TAG
import com.lexneoapps.motivodoroapp.ui.start.StartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint

class EditProjectFragment : Fragment(R.layout.fragment_edit_project) {

    private var _binding: FragmentEditProjectBinding? = null

    private val viewModel: StartViewModel by viewModels()
    private var curNoteColor = Constants.DEFAULT_PROJECT_COLOR

    private lateinit var currentProject: Project


    private var myColor: Int = SingletonProjectAttr.projectColor

    @Inject
    lateinit var projectDao: ProjectDao


    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProjectBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitial()
        setUpListeners()


    }

    private fun setUpListeners() {
        binding.saveChangesButton.setOnClickListener {

            val name = binding.editTextTextPersonName.text.toString()

            lifecycleScope.launch {
                    val updatedProject =
                        currentProject.copy(name = name, color = myColor)
                    projectDao.updateProject(updatedProject)
                    SingletonProjectAttr.projectColor = myColor
                    SingletonProjectAttr.projectName = name
                    Toast.makeText(requireContext(), "Project has been updated!", Toast.LENGTH_SHORT).show()
                }

            val action = EditProjectFragmentDirections.actionEditProjectFragmentToStartFragment()
            findNavController().navigate(action)
        }
        binding.viewProjectColor.setOnClickListener {
            ColorPickerDialogFragment().apply {
                setPositiveListener {
                    changeProjectColor(it)
                }
            }.show(parentFragmentManager, FRAGMENT_TAG)
        }

        binding.deleteProjectButton.setOnClickListener {
            lifecycleScope.launch {
                projectDao.deleteProject(currentProject)
                val action =
                    EditProjectFragmentDirections.actionEditProjectFragmentToStartFragment()
                findNavController().navigate(action)
            }
        }

    }

    private fun setUpInitial() {
        lifecycleScope.launchWhenCreated {
            currentProject = projectDao.getProjectById(SingletonProjectAttr.projectId)
        }
        binding.apply {
            editTextTextPersonName.setText(SingletonProjectAttr.projectName)
            materialLayout.setBackgroundColor(SingletonProjectAttr.projectColor)
        }
    }


    private fun changeProjectColor(colorString: String) {
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.circle_shape, null)
        drawable?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            val color = Color.parseColor("#$colorString")
            DrawableCompat.setTint(wrappedDrawable, color)
            binding.viewProjectColor.background = wrappedDrawable
            binding.materialLayout.setBackgroundColor(color)
            myColor = color
            curNoteColor = colorString
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}