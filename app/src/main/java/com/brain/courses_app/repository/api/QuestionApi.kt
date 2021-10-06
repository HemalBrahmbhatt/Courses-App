package com.brain.courses_app.repository.api

import com.brain.courses_app.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionApi {

    companion object {
        const val BASE_URL = "https://opentdb.com/"
    }

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 5,
        @Query("category") category: Int,
        @Query("difficulty") difficulty: String = "easy",
        @Query("type") type: String = "multiple"
    ): Response
}