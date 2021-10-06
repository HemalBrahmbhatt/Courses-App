package com.brain.courses_app.model

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Subject(
    var id: Int = -1,
    var title: String = "",
    var logo: String = "",
    var description: String = ""
) : Parcelable
