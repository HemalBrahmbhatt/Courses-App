package com.brain.courses_app.repository

import android.content.Context
import android.content.SharedPreferences
import com.brain.courses_app.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationRepository
@Inject
constructor(@ApplicationContext private val context: Context) {
    private val user = FirebaseAuth.getInstance().currentUser!!.uid

    fun setShowNotification(isChecked: Boolean) {
        val editPref =
            context.getSharedPreferences(context.getString(R.string.user_pref), 0)
                .edit()
        editPref.putBoolean(user, isChecked)
        editPref.apply()
    }

    fun getIsChecked(): Boolean {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.user_pref), 0)
        return sharedPref.getBoolean(user, true)
    }
}