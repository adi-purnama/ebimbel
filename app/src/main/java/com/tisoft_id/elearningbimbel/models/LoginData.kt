package com.tisoft_id.elearningbimbel.models

import com.google.gson.annotations.SerializedName

class LoginData {
    @SerializedName("id")
    var idlogin: Int = 0

    @SerializedName("jenis_user")
    var jnsuser : Int = 3

    @SerializedName("nama_lengkap")
    var nama : String? = null

    @SerializedName("alamat")
    var alamat : String? = null

    @SerializedName("email")
    var xemail : String? = null

    @SerializedName("no_hp")
    var notelp : String? = null

    @SerializedName("status")
    var status : Int = 0
}