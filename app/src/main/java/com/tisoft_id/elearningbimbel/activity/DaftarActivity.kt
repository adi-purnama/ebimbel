package com.tisoft_id.elearningbimbel.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.JsonParser
import com.tisoft_id.elearningbimbel.R
import com.tisoft_id.elearningbimbel.core.APIretrofit
import com.tisoft_id.elearningbimbel.core.Alert
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.daftar_main.*
import retrofit2.HttpException

class DaftarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daftar_main)
        button_daftar.setOnClickListener { daftar() }
        button_login.setOnClickListener {
            val inten=Intent(this@DaftarActivity,LoginActivity::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            startActivity(inten)
        }
    }

    private fun daftar() {
        /* cek field dan repass*/
        val xjenis=jenisradio.checkedRadioButtonId
        Log.d("Jenis User",xjenis.toString())
        val xnama=namatext.text.toString()
        val xalamat=alamat.text.toString()
        val xemail=emailtext.text.toString()
        val xnotelp=notelptext.text.toString()
        val xusername= usernametext.text.toString()
        val xpass=passtext.text.toString()
        val xrepass=repasstext.text.toString()
        if (xjenis<=0){
            Alert.dialog(this,"Silahkan pilih jenis pendaftaran")
            return
        }
        if (xnama.isEmpty()){
            Alert.dialog(this,"Silahkan isi Nama")
            namatext.requestFocus()
            return
        }
        if (xalamat.isEmpty()){
            Alert.dialog(this,"Silahkan isi alamat")
            alamat.requestFocus()
            return
        }
        if (xemail.isEmpty()){
            Alert.dialog(this,"Silahkan isi alamat Email")
            emailtext.requestFocus()
            return
        }
        if (xnotelp.isEmpty()){
            Alert.dialog(this,"Silahkan isi nomer telepon")
            notelptext.requestFocus()
            return
        }
        if (xusername.isEmpty()){
            Alert.dialog(this,"Silahkan isi Nama Pengguna")
            usernametext.requestFocus()
            return
        }
        if (xpass.isEmpty()){
            Alert.dialog(this,"Silahkan isi Password")
            passtext.requestFocus()
            return
        }
        if (xrepass.isEmpty()){
            Alert.dialog(this,"Silahkan isi Ulangi Password")
            repasstext.requestFocus()
            return
        }
        if (xrepass!=xpass){
            Alert.dialog(this,"Password tidak sama")
            repasstext.requestFocus()
            return
        }
        progressnya.visibility= View.VISIBLE
        val xstatus=0
        val apilogin=APIretrofit.create().postdaftar(xjenis,xnama,xalamat,xemail,xnotelp,xusername,xpass,xstatus)
            apilogin.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { dtlogin ->
                    progressnya.visibility= View.GONE
                    Log.d("DTLOGIN"," ")
                    if (dtlogin.status) {
                        /*Alert.dialog(this,"Pendaftaran Berhasil,Akun akan aktif setelah di verifikasi administrasi")*/
                        val alertDialog = AlertDialog.Builder(this)
                        alertDialog.setMessage("Pendaftaran Berhasil,Akun akan aktif setelah di verifikasi administrasi")
                        alertDialog.setPositiveButton("Ok") { _, _ ->
                            button_login.callOnClick()
                        }
                        alertDialog.show()
                    }else{
                        gagallogin()
                    }
                },
                { error ->
                    progressnya.visibility= View.GONE
                    var xmessage="Error"
                    if(error!=null) {
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
                    }
                    gagallogin()
                }
            )
    }
    private fun gagallogin(){
        Toast.makeText(this, "Pendaftaran Gagal", Toast.LENGTH_LONG).show()
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