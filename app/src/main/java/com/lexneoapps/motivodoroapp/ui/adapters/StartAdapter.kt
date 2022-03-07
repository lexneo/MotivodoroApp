package com.lexneoapps.motivodoroapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lexneoapps.motivodoroapp.data.project.Project
import com.lexneoapps.motivodoroapp.databinding.ProjectItemBinding


class StartAdapter() :
    ListAdapter<Project,StartAdapter.MyViewHolder>(StartDiffCallback()) {

    class MyViewHolder(val binding: ProjectItemBinding) : RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ProjectItemBinding.inflate
                (LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentProject = getItem(position)

        holder.binding.apply {
            linearLayout.setBackgroundColor(currentProject.color)
            projectNameTextView.text = currentProject.name
            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(currentProject)
                }
            }
        }
    }


    private var onItemClickListener: ((Project) -> Unit)? = null

    fun setOnItemClickListener(onItemClick: (Project) -> Unit) {
        this.onItemClickListener = onItemClick
    }
}
private class StartDiffCallback : DiffUtil.ItemCallback<Project>() {
    override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
        return oldItem == newItem
    }
}

