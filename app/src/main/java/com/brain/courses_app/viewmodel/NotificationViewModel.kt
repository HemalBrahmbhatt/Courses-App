package com.brain.courses_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brain.courses_app.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel
@Inject
constructor(private val notificationRepository: NotificationRepository):ViewModel() {
    val isCheckedMutableLiveData = MutableLiveData<Boolean>()

    fun setShowNotification(isChecked: Boolean){
        notificationRepository.setShowNotification(isChecked)
    }

    fun getIsChecked(){
        isCheckedMutableLiveData.value = notificationRepository.getIsChecked()
    }
}