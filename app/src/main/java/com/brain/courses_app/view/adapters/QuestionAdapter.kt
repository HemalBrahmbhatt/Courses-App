package com.brain.courses_app.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.brain.courses_app.R
import com.brain.courses_app.databinding.QuestionItemBinding
import com.brain.courses_app.model.Questions
import com.brain.courses_app.model.UserAnswer
import com.google.android.material.button.MaterialButton

class QuestionAdapter(
    private val context: Context,
    private val itemList: List<Questions>,
    private val onButtonClickListener: OnButtonClickListener
) :
    RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    private val userInputSet = mutableSetOf<UserAnswer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding =
            QuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }


    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    override fun getItemCount(): Int = itemList.size

    inner class QuestionViewHolder(private val binding: QuestionItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.btnPreviousItem.setOnClickListener(this)
            binding.btnNextItem.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(question: Questions, position: Int) {
            val questionR = replaceString(question.question)
            val answerR = replaceString(question.correctAnswer)
            val optionAR = replaceString(question.incorrectAnswers[0])
            val optionBR = replaceString(question.incorrectAnswers[1])
            val optionCR = replaceString(question.incorrectAnswers[2])

            binding.apply {
                if (position == 0) btnPreviousItem.visibility = View.INVISIBLE
                if (position == itemList.lastIndex) btnNextItem.text =
                    context.getString(R.string.submit)
                txtPageNo.text = (position + 1).toString()
                txtQuestionItem.text = questionR
                btnOptionA.text = optionAR
                btnOptionB.text = optionBR
                btnOptionC.text = answerR
                btnOptionD.text = optionCR
            }

        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (p0?.id == binding.btnNextItem.id) {
                    binding.apply {
                        if (toggleGrpItem.checkedButtonId != -1) {
                            val btn =
                                toggleGrpItem.findViewById<MaterialButton>(toggleGrpItem.checkedButtonId)!!
                            userInputSet.add(
                                UserAnswer(
                                    question = txtQuestionItem.text.toString(),
                                    answer = btn.text.toString(),
                                    correct = btnOptionC.text.toString()
                                )
                            )
                            btnOptionA.isClickable = false
                            btnOptionB.isClickable = false
                            btnOptionC.isClickable = false
                            btnOptionD.isClickable = false
                            if (toggleGrpItem.checkedButtonId != btnOptionC.id) {
                                btn.strokeColor = ColorStateList.valueOf(Color.RED)
                                btn.setTextColor(Color.RED)
                                btnOptionC.setTextColor(Color.GREEN)
                                btnOptionC.strokeColor = ColorStateList.valueOf(Color.GREEN)
                            } else {
                                btnOptionC.setTextColor(Color.GREEN)
                                btnOptionC.strokeColor = ColorStateList.valueOf(Color.GREEN)
                            }
                            onButtonClickListener.onNextClick(userInputSet, position)
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.select_ans),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else if (p0?.id == binding.btnPreviousItem.id) {
                    onButtonClickListener.onPreviousClick()
                }
            }
        }
    }

    fun replaceString(string: String): String {
        return string.replace("&quot;", "\"")
            .replace("&#039;", "'")
            .replace("", "")
    }

    interface OnButtonClickListener {
        fun onPreviousClick()
        fun onNextClick(userAnswerSet: MutableSet<UserAnswer>, position: Int)
    }
}