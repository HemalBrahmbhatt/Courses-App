package com.brain.courses_app.model


import com.google.gson.annotations.SerializedName

data class Questions(
    @SerializedName("correct_answer")
    val correctAnswer: String,
    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String>,
    @SerializedName("question")
    val question: String
)