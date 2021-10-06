package com.brain.courses_app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAnswer(
    var question: String = "",
    var answer: String = "",
    var correct: String = ""
) : Parcelable