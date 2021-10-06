package com.brain.courses_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brain.courses_app.model.Questions
import com.brain.courses_app.model.Subject
import com.brain.courses_app.model.TestResult
import com.brain.courses_app.model.UserAnswer
import com.brain.courses_app.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel
@Inject
constructor(private val testRepository: TestRepository) : ViewModel() {
    val isStoredMutableLiveData = testRepository.isStoredMutableLiveData
    val isNotificationCheckedMutableLiveData = MutableLiveData<Boolean>()

    private val _questionsMutableLiveData = MutableLiveData<List<Questions>>()
    val questionsMutableLiveData: LiveData<List<Questions>> get() = _questionsMutableLiveData

    fun getQuestions(category: Int) {
        viewModelScope.launch {
            _questionsMutableLiveData.value = testRepository.getQuestions(category)
        }
    }

    fun storeResults(date: String, subject: Subject, userAnswers: List<UserAnswer>) {
        viewModelScope.launch {
            val testResult = TestResult(
                date = date,
                subject = subject,
                userAnswers = userAnswers
            )
            testRepository.storeResult(testResult)
        }
    }

    fun getIsChecked(){
        isNotificationCheckedMutableLiveData.value = testRepository.getIsChecked()
    }
}