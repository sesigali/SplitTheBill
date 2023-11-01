package br.edu.scl.ifsp.ads.splitthebill.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Person(
    val id: Int,
    var name: String,
    var spent1: String,
    var spent2: String,
    var spent3: String,
    var debt: String,
    var description1: String,
    var description2: String,
    var description3: String,
): Parcelable