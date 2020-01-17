package com.tisoft_id.elearningbimbel.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.tisoft_id.elearningbimbel.R
import com.tisoft_id.elearningbimbel.core.APIretrofit
import com.tisoft_id.elearningbimbel.core.Preferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_main.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)
        button_login.setOnClickListener { login() }
        button_daftar.setOnClickListener {
            val inten=Intent(this@LoginActivity,DaftarActivity::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            startActivity(inten)
        }
    }

    private fun login() {
        val xuser=useredittext.text.toString()
        val xpass=passedittext.text.toString()
        /*Log.d("PASS",xuser+xpass)*/
        progressnya.visibility= View.VISIBLE
        val apilogin=APIretrofit.create().postLogin(xuser,xpass)
            apilogin.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { dtlogin ->
                    progressnya.visibility= View.GONE
                    val preferences=Preferences(this)
                    if (dtlogin.isNotEmpty()) {
                        preferences["login"] = true
                        val xjnuser=dtlogin[0].jnsuser
                        preferences["jenisuser"]=xjnuser
                        val xiduser=dtlogin[0].idlogin
                        preferences["iduser"]=xiduser
                        /*Log.d("DATA LOGIN", dtlogin[0].nama)*/
                        if (xjnuser==0)
                            startActivity(Intent(this@LoginActivity,AdminActivity::class.java)
                                .apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                })
                        else
                            startActivity(Intent(this@LoginActivity,MainActivity::class.java)
                                .apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                })
                        finish()
                    }else{
                        gagallogin()
                    }
                },
                { error ->
                    progressnya.visibility= View.GONE
                    println(error)
                    /*var xmessage="Error"
                    if (error!=null) {
                        if (error is HttpException) {
                            val errorJsonString = error.response()
                                .errorBody()?.string()
                            xmessage = JsonParser().parse(errorJsonString)
                                .asJsonObject["message"]
                                .asString
                            Log.e("RESULT1 ERROR", xmessage)
                        } else {
                            xmessage = error.message ?: error.message.toString()
                            Log.e("RESULT2 ERROR", xmessage)
                        }
                    }*/
                    gagallogin()
                }
            )
    }
    private fun gagallogin(){
        Toast.makeText(this, "Login Gagal", Toast.LENGTH_LONG).show()
    }
    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}