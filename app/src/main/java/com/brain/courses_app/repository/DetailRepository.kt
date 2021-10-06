package com.brain.courses_app.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import com.brain.courses_app.R
import com.brain.courses_app.model.TestResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DetailRepository
@Inject
constructor(
    @ApplicationContext
    private val context: Context
) {
    val dateMutableLiveData = MutableLiveData<String>()
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance()
            .getReference(context.getString(R.string.firebase_history_name))
    private val user = FirebaseAuth.getInstance().currentUser!!.uid

    suspend fun getLastDate(sId: Int) {
        if (checkForInternet(context)) {
            val snapshot = databaseReference.child(user).get().await()
            snapshot.children.forEach {
                if (it.getValue<TestResult>()!!.subject.id == sId) {
                    dateMutableLiveData.value = it.getValue<TestResult>()!!.date
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
}
