package com.tisoft_id.elearningbimbel.core

import android.content.Context
import android.support.v7.app.AlertDialog


object Alert {

    fun dialog(context: Context, error: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(error)
        builder.setPositiveButton("OK") { _, _ ->  }
        val alert = builder.create()
        alert.show()
    }
}
