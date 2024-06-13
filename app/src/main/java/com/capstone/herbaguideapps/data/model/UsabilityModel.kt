package com.capstone.herbaguideapps.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Usability(
    var penyakit: String,
    var resep: List<String>,
    var isExpandable: Boolean = false
) : Parcelable