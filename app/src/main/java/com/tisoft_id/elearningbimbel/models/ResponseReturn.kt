package com.tisoft_id.elearningbimbel.models

import com.google.gson.annotations.SerializedName

class ResponseReturn {
    @SerializedName("status")
    var status: Boolean = false

    @SerializedName("message")
    var message : String? = null

}