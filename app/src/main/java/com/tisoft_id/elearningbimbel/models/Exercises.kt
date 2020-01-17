package com.tisoft_id.elearningbimbel.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Exercises {
    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("guruid")
    @Expose
    var guruid: Int? = null

    @SerializedName("materialid")
    @Expose
    var materialid: Int? = null

    @SerializedName("exercise")
    @Expose
    var exercise: String? = null

    @SerializedName("description")
    @Expose
    var deskripsi: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("ukuran")
    @Expose
    var ukuran: Int = 0

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("results")
    @Expose
    var jawaban:List<Result>?=null
}