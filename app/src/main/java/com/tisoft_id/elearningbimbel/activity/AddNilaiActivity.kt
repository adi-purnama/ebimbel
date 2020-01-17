package com.tisoft_id.elearningbimbel.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.tisoft_id.elearningbimbel.R
import com.tisoft_id.elearningbimbel.core.APIretrofit
import com.tisoft_id.elearningbimbel.core.Alert
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_addnilai.*
import okhttp3.MediaType
import okhttp3.RequestBody

class AddNilaiActivity : AppCompatActivity() {
    /*activity*/
    private var jawabanid:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnilai)
        jawabanid=intent.getIntExtra("chapterid",0)
        btnsimpannilai.setOnClickListener {
            tambah()
        }
        btnbatalnilai.setOnClickListener {
            finish()
        }
    }

    private fun tambah(){
        var nilai =0
        if (addjudultext.length()==0){
            addjudultext.requestFocus()
            Alert.dialog(this,"Judul Materi Harus Di Isi")
            return
        }else{
            nilai=addjudultext.text.toString().toInt()
        }
        val keterangan=deskripsitext.text.toString()
        progressnya.visibility= View.VISIBLE
        btnsimpannilai.visibility=View.GONE
        val xjawabanid = RequestBody.create(MediaType.parse("text/plain"), jawabanid.toString())
        val xnilai = RequestBody.create(MediaType.parse("text/plain"), nilai.toString())
        val xketerangan = RequestBody.create(MediaType.parse("text/plain"), keterangan)
        val ambilmapel= APIretrofit.create().postNilai(xjawabanid,xnilai,xketerangan)
        ambilmapel.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { mapel ->
                    if (mapel.status)
                        Toast.makeText(this,"Nilai Berhasil Disimpan",Toast.LENGTH_LONG).show()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                },
                { error ->
                    progressnya.visibility= View.GONE
                    btnsimpannilai.visibility=View.VISIBLE
                    Alert.dialog(this,"Gagal Upload Nilai")
                    Log.e("RESULT1 ERROR", error.toString())
                }
            )
    }
}