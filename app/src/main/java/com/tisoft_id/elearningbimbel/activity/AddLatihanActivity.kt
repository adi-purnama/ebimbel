package com.tisoft_id.elearningbimbel.activity

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.tisoft_id.elearningbimbel.R
import com.tisoft_id.elearningbimbel.core.APIretrofit
import com.tisoft_id.elearningbimbel.core.Alert
import com.tisoft_id.elearningbimbel.core.Preferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_addmateri.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddLatihanActivity : AppCompatActivity() {
    /*activity*/
    private val kodebacafile: Int = 42
    private var typeadd:Int=0
    private var materialid:Int=0
    private var chapterid:Int=0
    private var materialname:String?=null
    private var judul:String?=null
    private var nmfile:File?=null
    private var urifile:Uri?=null
    private var guruid:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addmateri)
        typeadd=intent.getIntExtra("typeadd",0)
        materialid=intent.getIntExtra("materialid",0)
        chapterid=intent.getIntExtra("chapterid",0)
        materialname="Exercises "+intent.getStringExtra("materialname")
        deskripsitext.visibility=View.GONE
        val preferences= Preferences(this)
        guruid=preferences.getInt("iduser")
        juduladdmateri.text=materialname
        btnaddfile.setOnClickListener {
            performFileSearch()
        }
        btnsimpanadd.setOnClickListener {
            if (typeadd==1) {
                tambah()
            }else{
                edit()
            }
        }
        btnbataladd.setOnClickListener {
            finish()
        }
        jenisfile.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId==R.id.materifile){
                addlinkwidget.visibility=View.GONE
                btnaddfile.visibility=View.VISIBLE
            }else{
                addlinkwidget.visibility=View.VISIBLE
                btnaddfile.visibility=View.GONE
            }
        }
    }

    private fun tambah(){
        if (addjudultext.length()==0){
            Alert.dialog(this,"Judul Materi Harus Di Isi")
            return
        }else{
            judul=addjudultext.text.toString()
        }
        if (nmfile == null) {
            Alert.dialog(this, "File Tidak Boleh Kosong")
            return
        }
        val xguruid = RequestBody.create(MediaType.parse("text/plain"), guruid.toString())
        val xmaterialid = RequestBody.create(MediaType.parse("text/plain"), materialid.toString())
        val xjudul = RequestBody.create(MediaType.parse("text/plain"), judul!!)
        val reqfile=RequestBody.create(MediaType.parse(contentResolver.getType(urifile!!)!!), nmfile!!)
        val xfile= MultipartBody.Part.createFormData("file", nmfile!!.name, reqfile)
        progressnya.visibility= View.VISIBLE
        btnsimpanadd.visibility=View.GONE
        val ambilmapel= APIretrofit.create().exerciseadd(xguruid,xmaterialid,xjudul,xfile)
        ambilmapel.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { mapel ->
                    if (mapel.status)
                        Toast.makeText(this,"Data Berhasil Ditambah",Toast.LENGTH_LONG).show()
                        finish()
                },
                { error ->
                    progressnya.visibility= View.GONE
                    btnsimpanadd.visibility=View.VISIBLE
                    Alert.dialog(this,"Gagal Simpan")
                    Log.e("RESULT1 ERROR", error.toString())
                }
            )
    }
    private fun edit(){
        if (addjudultext.length()==0){
            Alert.dialog(this,"Judul Materi Harus Di Isi")
            return
        }else{
            judul=addjudultext.text.toString()
        }
        val xguruid = RequestBody.create(MediaType.parse("text/plain"), guruid.toString())
        val xmaterialid = RequestBody.create(MediaType.parse("text/plain"), materialid.toString())
        val xjudul = RequestBody.create(MediaType.parse("text/plain"), judul!!)
        val xchapterid = RequestBody.create(MediaType.parse("text/plain"), chapterid.toString())
        val xfile= if (nmfile != null) {
            val reqfile=RequestBody.create(MediaType.parse(contentResolver.getType(urifile!!)!!), nmfile!!)
            MultipartBody.Part.createFormData("file", nmfile!!.name, reqfile)
        }else{
            val reqfile=RequestBody.create(MediaType.parse("text/plain"), "")
            MultipartBody.Part.createFormData("file1", "", reqfile)
        }
        progressnya.visibility= View.VISIBLE
        btnsimpanadd.visibility=View.GONE
        val ambilmapel= APIretrofit.create().exerciseEdit(xguruid,xmaterialid,xjudul,xchapterid,xfile)
        ambilmapel.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { mapel ->
                    if (mapel.status)
                        Toast.makeText(this,"Data Berhasil Diedit",Toast.LENGTH_LONG).show()
                    finish()
                },
                { error ->
                    progressnya.visibility= View.GONE
                    btnsimpanadd.visibility=View.VISIBLE
                    progressnya.visibility= View.GONE
                    Log.e("RESULT1 ERROR", error.toString())
                }
            )
    }
    private fun performFileSearch() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, kodebacafile)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == kodebacafile && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { uri ->
                urifile=uri
                val xcontentresolve=getPath(this,uri)
                nmfile=File(xcontentresolve)
                namafile.text=nmfile!!.name
            }
        }
    }

    private fun getPath(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    val xenv=Environment.getExternalStorageDirectory().toString()
                    return xenv+"/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        }
        else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
        }
        else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}
