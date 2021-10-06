package com.brain.courses_app.viewmodel

import androidx.lifecycle.ViewModel
import com.brain.courses_app.model.User
import com.brain.courses_app.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject
constructor(private val registerRepository: RegisterRepository) : ViewModel() {
    val isRegisteredMutableLiveData = registerRepository.isRegisteredMutableLiveData
    fun registerUser(user: User) {
        registerRepository.registerUser(user)
    }
}