package com.tisoft_id.elearningbimbel.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("siswaid")
    @Expose
    var siswaid: Int = 0


    @SerializedName("nama_siswa")
    @Expose
    var siswaname: String? = null

    @SerializedName("exerciseid")
    @Expose
    var exerciseid: String? = null

    @SerializedName("exercise")
    @Expose
    var exercise: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("ukuran")
    @Expose
    var ukuran: Int = 0

    @SerializedName("nilai")
    @Expose
    var nilai: Int = 0

    @SerializedName("keterangan")
    @Expose
    var keterangan: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}