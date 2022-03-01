package com.lexneoapps.motivodoroapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lexneoapps.motivodoroapp.R
import com.lexneoapps.motivodoroapp.data.record.Record
import com.lexneoapps.motivodoroapp.databinding.HistoryItemBinding
import timber.log.Timber


class HistoryAdapter(val context: Context) :
    ListAdapter<Record, HistoryAdapter.HistoryViewHolder>(RecordDiffCallback()) {

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {

        super.onDetachedFromRecyclerView(recyclerView)
        Timber.d("onDetachedFromRecyclerView: called ")

    }

    class HistoryViewHolder(val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(record: Record) {
            binding.apply {
                projectNameTextView.text = record.projectName
//                viewProjectColor.background = record.projectColor.toDrawable()
                timeStartedTextView.text = record.startTimeFormattedTime
                timeEndedTextView.text = record.endTimeTimeFormattedTime
                totalTimeTextView.text = record.totalTimeFormatted
                dateTextView.text = record.startTimeFormattedDate

             /*   projectNameTextView.text = record.projectName
                startTimeTextView.text = record.startTimeFormattedTime
                endTimeTextView.text = record.endTimeTimeFormattedTime


                totalTimeTextView.text = record.totaltimeFormatedTry2
                dateTextView.text = record.startTimeFormattedDate*/


            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )


        return HistoryViewHolder(binding)


    }


    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentRecord = getItem(position)
        val drawable = ResourcesCompat.getDrawable(context.resources, R.drawable.circle_shape, null)
        drawable?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            val color = currentRecord.projectColor
            DrawableCompat.setTint(wrappedDrawable, color)
            holder.binding.viewProjectColor.background = wrappedDrawable
        }
        holder.bind(currentRecord)
    }
}

private class RecordDiffCallback : DiffUtil.ItemCallback<Record>() {
    override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem == newItem
    }
}
