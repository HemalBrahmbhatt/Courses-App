package com.brain.courses_app.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.brain.courses_app.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LoginRepository
@Inject
constructor(@ApplicationContext private val context: Context) {
    val isSuccessfulMutableLiveData = MutableLiveData<Boolean>()
    private val auth = FirebaseAuth.getInstance()

    fun loginUser(email: String, password: String, remember: Boolean) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                if (remember) {
                    val editPref =
                        context.getSharedPreferences(context.getString(R.string.user_pref), 0)
                            .edit()
                    editPref.putBoolean(context.getString(R.string.is_logged_in), true)
                    editPref.apply()
                }
                isSuccessfulMutableLiveData.value = true
            } else {
                Toast.makeText(
                    context,
                    it.exception?.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
                isSuccessfulMutableLiveData.value = false
            }
        }
    }
}