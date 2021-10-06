package com.brain.courses_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brain.courses_app.model.Subject
import com.brain.courses_app.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(private val homeRepository: HomeRepository) : ViewModel() {
    private val _subjects = MutableLiveData<List<Subject>>()
    val subjects: LiveData<List<Subject>> get() = _subjects
    fun getSubjectList() {
        _subjects.value = homeRepository.getSubjects()
    }
}