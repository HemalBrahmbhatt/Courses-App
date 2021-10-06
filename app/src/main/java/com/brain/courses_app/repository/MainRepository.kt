package com.brain.courses_app.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.brain.courses_app.R
import com.brain.courses_app.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MainRepository
@Inject
constructor(@ApplicationContext private val context: Context) {
    val userMutableLiveData = MutableLiveData<User?>()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference(context.getString(R.string.firebase_table_name))

    fun getUser() {
        if (checkForInternet(context)) {
            databaseReference.child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val cUser = snapshot.getValue(User::class.java)
                        if (cUser != null) {
                            userMutableLiveData.value = cUser
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                    }
                })
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