package com.tisoft_id.elearningbimbel.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Chapter {
    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("guruid")
    @Expose
    var guruid: Int? = null
    @SerializedName("materialid")
    @Expose
    var materialid: Int? = null
    @SerializedName("chapter")
    @Expose
    var chapter: String? = null
    @SerializedName("deskripsi")
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
}