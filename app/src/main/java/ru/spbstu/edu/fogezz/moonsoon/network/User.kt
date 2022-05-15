package ru.spbstu.edu.fogezz.moonsoon.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val nickname: String): Parcelable
