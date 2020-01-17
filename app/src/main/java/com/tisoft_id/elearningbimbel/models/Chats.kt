package com.tisoft_id.elearningbimbel.models

import com.google.gson.annotations.SerializedName

class Chats {
    @SerializedName("id")
    var idchat: Int = 0

    @SerializedName("userid")
    var userid: Int = 0

    @SerializedName("nama_lengkap")
    var username: String? = null

    @SerializedName("chat")
    var chat: String? = null

    @SerializedName("created_at")
    var createdat: String? = null

    @SerializedName("updated_at")
    var updateat: String? = null
}