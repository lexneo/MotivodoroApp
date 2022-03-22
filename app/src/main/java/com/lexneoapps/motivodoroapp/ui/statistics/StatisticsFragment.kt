package com.lexneoapps.motivodoroapp.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.databinding.FragmentStatisticsBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private var _binding: FragmentStatisticsBinding? = null

    private val viewModel: StatisticsViewModel by viewModels()

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        setupPieChart()

    }

    private fun setUpObservers() {


        viewModel.allProjects.observe(viewLifecycleOwner) {

            it?.let {
                val allProjects = it.indices.map { i -> PieEntry(it[i].totalTime.toFloat(),it[i].name) }
                Timber.i("PIE Total time : $allProjects")
                val colorList = it.indices.map { i -> it[i].color }
                Timber.i("PIE ColorList  : $colorList")

                val pieDataSet = PieDataSet(allProjects, "Total time").apply {
                    valueTextColor = Color.WHITE
                    valueTextSize = 12f
                    colors = colorList
                    valueFormatter = PercentFormatter(binding.totalPieChart)

                }
                binding.totalPieChart.apply {
                    data = PieData(pieDataSet)
                    invalidate()
                }

            }

        }
    }


    private fun setupPieChart() {
        binding.totalPieChart.apply {
            isDrawHoleEnabled = true
            setHoleColor(ContextCompat.getColor(requireContext(),R.color.primaryDarkColor))
            setUsePercentValues(true)
            setEntryLabelTextSize(12f)
            setEntryLabelColor(Color.WHITE)

            centerText = "Total time"
            setCenterTextColor(Color.WHITE)
            setCenterTextSize(24f)
            description.isEnabled = false
          legend.apply {
              verticalAlignment = Legend.LegendVerticalAlignment.TOP
              horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
              orientation = Legend.LegendOrientation.VERTICAL
              textColor = Color.WHITE
              setDrawInside(false)
              isEnabled = true
          }

            invalidate()
            animateY(1400, Easing.EaseInOutQuad)

        }

    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}