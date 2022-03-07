package com.lexneoapps.motivodoroapp.ui.adapters
/*

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lexneoapps.motivodoroapp.data.cdtimer.CDTimer
import com.lexneoapps.motivodoroapp.data.project.Project
import com.lexneoapps.motivodoroapp.databinding.CdtimerItemBinding
import com.lexneoapps.motivodoroapp.databinding.ProjectItemBinding


class ProjectAdapter() :
    ListAdapter<CDTimer,ProjectAdapter.MyViewHolder>(ProjectDiffCallback()) {

    class MyViewHolder(val binding: CdtimerItemBinding) : RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            CdtimerItemBinding.inflate
                (LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCDTimer = getItem(position)

        holder.binding.apply {
            cdtimerLayout.setBackgroundColor(currentCDTimer.color)
            countdownTimerTextView.text = currentCDTimer.name
            activityTimeTextView.text = currentCDTimer.countdown.toInt().toString()
            pauseTimeTextView.text = currentCDTimer.pause.toInt().toString()
            repeatTimeTextView.text = currentCDTimer.repeat.toString()
            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(currentCDTimer)
                }
            }
        }
    }


    private var onItemClickListener: ((CDTimer) -> Unit)? = null

    fun setOnItemClickListener(onItemClick: (CDTimer) -> Unit) {
        this.onItemClickListener = onItemClick
    }
}
private class ProjectDiffCallback : DiffUtil.ItemCallback<CDTimer>() {
    override fun areItemsTheSame(oldItem: CDTimer, newItem: CDTimer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CDTimer, newItem: CDTimer): Boolean {
        return oldItem == newItem
    }
}

*/
