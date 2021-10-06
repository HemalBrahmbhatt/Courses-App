package com.brain.courses_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brain.courses_app.repository.DetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
@Inject
constructor(private val detailRepository: DetailRepository) : ViewModel() {
    val dateMutableLiveData = detailRepository.dateMutableLiveData

    fun getLastDate(sId: Int) {
        viewModelScope.launch {
            detailRepository.getLastDate(sId)
        }
    }
}