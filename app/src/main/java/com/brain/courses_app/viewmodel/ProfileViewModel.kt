package com.brain.courses_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brain.courses_app.model.User
import com.brain.courses_app.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(private val profileRepository: ProfileRepository) : ViewModel() {
    val userMutableLiveData = MutableLiveData<User>()

    fun getUser() {
        viewModelScope.launch {
            userMutableLiveData.value = profileRepository.getUser()
        }
    }

    fun logout() {
        profileRepository.logout()
    }
}