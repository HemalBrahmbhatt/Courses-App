package com.brain.courses_app.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brain.courses_app.databinding.SubjectItemBinding

import com.brain.courses_app.model.Subject
import com.bumptech.glide.Glide

class SubjectAdapter(private val mListener: OnItemClickListener) :
    ListAdapter<Subject, SubjectAdapter.SubjectViewHolder>(SubjectComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder(
            SubjectItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        if (getItem(position) != null) {
            holder.bind(getItem(position))
        }
    }

    inner class SubjectViewHolder(private val binding: SubjectItemBinding) :
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

        fun bind(subject: Subject) {
            binding.apply {
                Glide.with(itemView)
                    .load(subject.logo)
                    .into(imgSubItem)
                txtTitleItem.text = subject.title
            }
        }
    }

    class SubjectComparator : DiffUtil.ItemCallback<Subject>() {
        override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean =
            oldItem == newItem
    }

    interface OnItemClickListener {
        fun onItemClick(subject: Subject)
    }
}
