package com.brain.courses_app.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.brain.courses_app.R
import com.brain.courses_app.model.TestResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HistoryRepository
@Inject
constructor(
    @ApplicationContext
    private val context: Context
) {
    val resultList = MutableLiveData<ArrayList<TestResult>>()
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance()
            .getReference(context.getString(R.string.firebase_history_name))
    private val user = FirebaseAuth.getInstance().currentUser!!.uid
    suspend fun getResult() {
        if (checkForInternet(context)) {
            val snapshot = databaseReference.child(user).get().await()
            resultList.value = ArrayList()
            snapshot.children.forEach {
                it.getValue<TestResult>()?.let { it1 -> resultList.value!!.add(it1) }
            }
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT)
                .show()
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
}
