package com.tisoft_id.elearningbimbel.models

import com.google.gson.annotations.SerializedName

class Latihan {
    @SerializedName("id")
    var idmapel: Int = 0

    @SerializedName("subjectid")
    var idsubject: String? = null

    @SerializedName("material")
    var namapel: String? = null

    @SerializedName("exercises")
    var exercises: List<Exercises>? = null
}