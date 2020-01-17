package com.tisoft_id.elearningbimbel.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.tisoft_id.elearningbimbel.R
import com.tisoft_id.elearningbimbel.core.Preferences


class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val preferences=Preferences(this)
        Handler().postDelayed(object : Thread() {
            override fun run() {
                val userlogin=preferences.getBoolean("login")
                val mainMenu = if (!userlogin)
                        Intent(this@SplashActivity, LoginActivity::class.java)
                    else {
                        if (preferences.getInt("jenisuser")>0)
                            Intent(this@SplashActivity, MainActivity::class.java)
                        else
                            Intent(this@SplashActivity, AdminActivity::class.java)
                    }
                this@SplashActivity.startActivity(mainMenu)
                this@SplashActivity.finish()
                overridePendingTransition(R.layout.fadein, R.layout.fadeout)
            }
        }, 3000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}