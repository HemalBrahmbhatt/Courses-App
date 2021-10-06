package com.brain.courses_app.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brain.courses_app.databinding.HistoryItemBinding
import com.brain.courses_app.model.TestResult
import com.bumptech.glide.Glide

class HistoryAdapter(private val mListener: OnItemClickListener) :
    ListAdapter<TestResult, HistoryAdapter.HistoryViewHolder>(HistoryComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            HistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        if (getItem(position) != null) {
            holder.bind(getItem(position))
        }
    }

    inner class HistoryViewHolder(private val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        mListener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(testResult: TestResult) {
            var score = 0
            val userAnswers = testResult.userAnswers
            for (i in userAnswers) {
                if (i.answer == i.correct) {
                    score++
                }
            }
            val p = (score * 100) / 5
            binding.apply {
                Glide.with(itemView)
                    .load(testResult.subject.logo)
                    .into(imgSubItem)
                txtSubTitleItem.text = testResult.subject.title
                txtDateItem.text = testResult.date
                circularIndicatorItem.progress = p
                txtProgressItem.text = p.toString().plus("%")
            }
        }
    }

    class HistoryComparator : DiffUtil.ItemCallback<TestResult>() {
        override fun areItemsTheSame(oldItem: TestResult, newItem: TestResult): Boolean =
            oldItem.date == newItem.date

        override fun areContentsTheSame(oldItem: TestResult, newItem: TestResult): Boolean =
            oldItem == newItem
    }

    interface OnItemClickListener {
        fun onItemClick(testResult: TestResult)
    }
}
