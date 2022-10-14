package com.example.testworknode.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val name:String ="",
    val count: Int = 0,
    val price: Int = 0,
    val imgUrl: String ="",
    val idFirebase:String = ""
) : Parcelable
