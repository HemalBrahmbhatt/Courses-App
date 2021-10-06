package com.brain.courses_app.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.brain.courses_app.R
import com.brain.courses_app.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RegisterRepository
@Inject
constructor(@ApplicationContext private val context: Context) {
    val isRegisteredMutableLiveData = MutableLiveData<Boolean>()
    private val mAuth: FirebaseAuth = Firebase.auth

    fun registerUser(user: User) {
        mAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FirebaseDatabase.getInstance()
                        .getReference(context.getString(R.string.firebase_table_name))
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                isRegisteredMutableLiveData.value = true
                            } else {
                                Toast.makeText(
                                    context,
                                    it.exception?.localizedMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                                isRegisteredMutableLiveData.value = false
                            }
                        }
                } else {
                    Toast.makeText(context, task.exception?.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                    isRegisteredMutableLiveData.value = false
                }
            }
    }
}