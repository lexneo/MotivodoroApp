package com.lexneoapps.motivodoroapp.ui.createproject

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.data.project.Project
import com.lexneoapps.motivodoroapp.data.project.ProjectDao
import com.lexneoapps.motivodoroapp.databinding.FragmentCreateProjectBinding
import com.lexneoapps.motivodoroapp.other.Constants.DEFAULT_PROJECT_COLOR
import com.lexneoapps.motivodoroapp.ui.ColorPickerDialogFragment
import com.lexneoapps.motivodoroapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

const val FRAGMENT_TAG = "CreateProjectFragment"


@AndroidEntryPoint

class CreateProjectFragment : Fragment(R.layout.fragment_create_project) {

    private var _binding: FragmentCreateProjectBinding? = null

    private val viewModel: MainViewModel by viewModels()
    private var curNoteColor = DEFAULT_PROJECT_COLOR


    private var myColor : Int? = null

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
        _binding = FragmentCreateProjectBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createBtn.setOnClickListener{

            val name = binding.editTextTextPersonName.text.toString()
            val project = myColor?.let {
                    Project(name,it)
            }
            lifecycleScope.launch {
                if (project != null){
                    projectDao.insertProject(project)
                }
            }
            val action = CreateProjectFragmentDirections.actionCreateProjectFragmentToStartFragment()
            findNavController().navigate(action)
        }
        binding.viewProjectColor.setOnClickListener {
            ColorPickerDialogFragment().apply {
                setPositiveListener {
                    changeProjectColor(it)
                }
            }.show(parentFragmentManager, FRAGMENT_TAG)
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