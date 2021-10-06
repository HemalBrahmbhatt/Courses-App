package com.brain.courses_app.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.brain.courses_app.R
import com.brain.courses_app.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepository
@Inject
constructor(@ApplicationContext private val context: Context) {
    private val user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference(context.getString(R.string.firebase_table_name))
    private val userId: String = user.uid

    suspend fun getUser(): User {
        return if (checkForInternet(context)) {
            databaseReference.child(userId).get().await().getValue<User>()!!
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT)
                .show()
            User(context.getString(R.string.name), 0, context.getString(R.string.email_address), "")
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        val editPref = context.getSharedPreferences(context.getString(R.string.user_pref), 0).edit()
        editPref.putBoolean(context.getString(R.string.is_logged_in), false)
        editPref.apply()
        NotificationManagerCompat.from(context).cancelAll()
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


