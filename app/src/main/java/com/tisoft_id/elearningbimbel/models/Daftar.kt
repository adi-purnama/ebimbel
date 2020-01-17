package com.tisoft_id.elearningbimbel.models

import com.google.gson.annotations.SerializedName

class Daftar {
    /*"jenis_user":"2",
        "nama_lengkap":"baru",
        "alamat":"alamat",
        "email":"email@eail.com",
        "no_hp":"0987654321",
        "username":"baru",
        "status":"0",
        "updated_at":"2019-05-01 05:51:45",
        "created_at":"2019-05-01 05:51:45",
        "id":21*/
    @SerializedName("jenis_user")
    var jenis: Int = 0

    @SerializedName("nama_lengkap")
    var namalengkap : String? = null

    @SerializedName("alamat")
    var alamat : String? = null

    @SerializedName("email")
    var xemail : String? = null

    @SerializedName("username")
    var username : String? = null

}