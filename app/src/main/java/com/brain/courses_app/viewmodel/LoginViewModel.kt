package com.brain.courses_app.viewmodel

import androidx.lifecycle.ViewModel
import com.brain.courses_app.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(private val loginRepository: LoginRepository) : ViewModel() {
    val isSuccessfulMutableLiveData = loginRepository.isSuccessfulMutableLiveData
    fun loginUser(email: String, password: String, remember: Boolean) {
        loginRepository.loginUser(email, password, remember)
    }
}