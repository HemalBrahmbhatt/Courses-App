package com.brain.courses_app.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brain.courses_app.R
import com.brain.courses_app.databinding.ResultItemBinding
import com.brain.courses_app.model.UserAnswer

class ResultAdapter(
    private val context: Context,
    private val itemList: List<UserAnswer>
) : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    override fun getItemCount(): Int = itemList.size

    inner class ResultViewHolder(private val binding: ResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userAnswer: UserAnswer, position: Int) {
            binding.apply {
                txtResultQuestionItem.text =
                    context.getString(R.string.question_r).plus("${position + 1}. ")
                        .plus(userAnswer.question)
                txtResultCorrectItem.text =
                    context.getString(R.string.correct_ans).plus(userAnswer.correct)
                txtResultUaItem.text = context.getString(R.string.your_ans).plus(userAnswer.answer)
            }
        }
    }
}