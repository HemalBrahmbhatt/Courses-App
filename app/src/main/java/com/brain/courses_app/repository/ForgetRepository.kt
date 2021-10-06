package com.brain.courses_app.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.brain.courses_app.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ForgetRepository
@Inject
constructor(
    @ApplicationContext
    private val context: Context
) {
    val isSuccessfulMutableLiveData = MutableLiveData<Boolean>()
    private var auth= FirebaseAuth.getInstance()
    fun forgetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.reset_success),
                        Toast.LENGTH_SHORT
                    ).show()
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