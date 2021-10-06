package com.brain.courses_app.viewmodel

import androidx.lifecycle.ViewModel
import com.brain.courses_app.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(private val mainRepository: MainRepository) : ViewModel() {
    val userMutableLiveData = mainRepository.userMutableLiveData
    fun getUser() {
        mainRepository.getUser()
    }
}