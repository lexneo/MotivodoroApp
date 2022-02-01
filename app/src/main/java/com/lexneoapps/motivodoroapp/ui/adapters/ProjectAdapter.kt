package com.lexneoapps.motivodoroapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lexneoapps.motivodoroapp.data.project.Project
import com.lexneoapps.motivodoroapp.databinding.ProjectItemBinding


class ProjectAdapter() :
    RecyclerView.Adapter<ProjectAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ProjectItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Project>() {
        override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)
    var list: List<Project>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ProjectItemBinding.inflate
                (LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentProject = list[position]

        holder.binding.linearLayout.setBackgroundColor(currentProject.color)
        holder.binding.projectNameTextView.text = currentProject.name


        holder.binding.root.setOnClickListener {
            onItemClickListener?.let { click ->
                click(currentProject)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private var onItemClickListener: ((Project) -> Unit)? = null

    fun setOnItemClickListener(onItemClick: (Project) -> Unit) {
        this.onItemClickListener = onItemClick
    }
}

