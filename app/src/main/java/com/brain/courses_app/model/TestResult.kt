package com.brain.courses_app.model

data class TestResult(
    var date: String = "",
    var subject: Subject = Subject(-1, "", "", ""),
    var userAnswers: List<UserAnswer> = listOf(UserAnswer("", "", ""))
)