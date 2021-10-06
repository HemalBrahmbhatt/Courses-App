package com.brain.courses_app.viewmodel

import androidx.lifecycle.ViewModel
import com.brain.courses_app.repository.ForgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgetViewModel
@Inject
constructor(private val forgetRepository: ForgetRepository) : ViewModel() {
    val isSuccessfulMutableLiveData = forgetRepository.isSuccessfulMutableLiveData
    fun forgetPassword(email: String) {
        forgetRepository.forgetPassword(email)
    }
}