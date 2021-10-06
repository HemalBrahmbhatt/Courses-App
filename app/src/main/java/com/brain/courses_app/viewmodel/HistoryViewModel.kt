package com.brain.courses_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brain.courses_app.model.TestResult
import com.brain.courses_app.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel
@Inject
constructor(private val historyRepository: HistoryRepository) : ViewModel() {
    val results: MutableLiveData<ArrayList<TestResult>> = historyRepository.resultList
    fun getResult() {
        viewModelScope.launch {
            historyRepository.getResult()
        }
    }
}