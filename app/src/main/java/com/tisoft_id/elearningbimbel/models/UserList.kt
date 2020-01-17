package com.tisoft_id.elearningbimbel.models

import com.google.gson.annotations.SerializedName

class UserList {
    @SerializedName("id")
    var idlogin: Int = 0
    @SerializedName("jenis_user")
    var jenisuser: Int=3
    @SerializedName("nama_lengkap")
    var nama:String?=null
    @SerializedName("alamat")
    var alamat:String?=null
    @SerializedName("email")
    var emailnya:String?=null
    @SerializedName("no_hp")
    var notelp:String?=null
    @SerializedName("status")
    var statususer:Int=0
    @SerializedName("username")
    var nmuser:String?=null
}