package com.brain.courses_app.repository

import android.content.Context
import com.brain.courses_app.R
import com.brain.courses_app.model.Subject
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HomeRepository
@Inject constructor(@ApplicationContext private val context: Context) {
    fun getSubjects(): List<Subject> {
        val gk = Subject(
            9,
            context.getString(R.string.gk_title),
            context.getString(R.string.gk_logo),
            context.getString(R.string.gk_description)
        )
        val literature = Subject(
            10,
            context.getString(R.string.literature_title),
            context.getString(R.string.literature_logo),
            context.getString(R.string.literature_description)
        )
        val science = Subject(
            17,
            context.getString(R.string.science_title),
            context.getString(R.string.science_logo),
            context.getString(R.string.science_description)
        )
        val computer = Subject(
            18,
            context.getString(R.string.computers_title),
            context.getString(R.string.computer_logo),
            context.getString(R.string.computer_description)
        )
        val math = Subject(
            19,
            context.getString(R.string.math_title),
            context.getString(R.string.math_logo),
            context.getString(R.string.math_description)
        )
        val history = Subject(
            23,
            context.getString(R.string.history_title),
            context.getString(R.string.history_logo),
            context.getString(R.string.history_des)
        )
        return listOf(gk, literature, science, computer, math, history)
    }
}