package com.brain.courses_app.model

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("results")
    val results: List<Questions>
)