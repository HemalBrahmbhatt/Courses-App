package com.brain.courses_app.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.brain.courses_app.R
import com.brain.courses_app.model.Questions
import com.brain.courses_app.model.TestResult
import com.brain.courses_app.repository.api.QuestionApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestRepository
@Inject
constructor(
    private val questionApi: QuestionApi,
    @ApplicationContext
    private val context: Context
) {
    val isStoredMutableLiveData = MutableLiveData<Boolean>()
    private val user = FirebaseAuth.getInstance().currentUser!!.uid

    suspend fun getQuestions(category: Int): List<Questions> {
        return if (checkForInternet(context)) {
            questionApi.getQuestions(category = category).results
        } else {
            emptyList()
        }
    }

    fun storeResult(testResult: TestResult) {
        if (checkForInternet(context)) {
            FirebaseDatabase.getInstance()
                .getReference(context.getString(R.string.firebase_history_name))
                .child(user).push().setValue(testResult)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        isStoredMutableLiveData.value = true
                    } else {
                        Toast.makeText(
                            context,
                            it.exception?.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                        isStoredMutableLiveData.value = false
                    }
                }
        }
    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    fun getIsChecked(): Boolean {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.user_pref), 0)
        return sharedPref.getBoolean(user, true)
    }
}