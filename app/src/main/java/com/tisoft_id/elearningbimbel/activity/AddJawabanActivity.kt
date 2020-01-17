package com.tisoft_id.elearningbimbel.activity

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
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
import kotlinx.android.synthetic.main.activity_addjawaban.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddJawabanActivity : AppCompatActivity() {
    /*activity*/
    private val kodebacafile: Int = 42
    private var materialid:Int=0
    private var chapterid:Int=0
    private var materialname:String?=null
    private var judul:String?=null
    private var deskripsi:String?=null
    private var nmfile:File?=null
    private var urifile:Uri?=null
    private var guruid:Int=0
    private var permissionfile:Int=212
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addjawaban)
        chapterid=intent.getIntExtra("chapterid",0)
        materialname=intent.getStringExtra("materialname")
        val preferences= Preferences(this)
        guruid=preferences.getInt("iduser")
        btnaddfile.setOnClickListener {
            performFileSearch()
        }
        val xjudul="Upload Jawaban $materialname"
        juduladdmateri.text=xjudul
        btnsimpanadd.setOnClickListener {
            tambah()
        }
        btnbataladd.setOnClickListener {
            finish()
        }
    }

    private fun tambah(){
        if (nmfile == null) {
            Alert.dialog(this, "File Tidak Boleh Kosong")
            return
        }
        progressnya.visibility= View.VISIBLE
        btnsimpanadd.visibility=View.GONE
        val xguruid = RequestBody.create(MediaType.parse("text/plain"), guruid.toString())
        val xchapterid = RequestBody.create(MediaType.parse("text/plain"), chapterid.toString())
        val reqfile=RequestBody.create(MediaType.parse(contentResolver.getType(urifile!!)!!), nmfile!!)
        val xfile= MultipartBody.Part.createFormData("file", nmfile!!.name, reqfile)
        val ambilmapel= APIretrofit.create().postJawaban(xguruid,xchapterid,xfile)
        ambilmapel.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { mapel ->
                    if (mapel.status)
                        Toast.makeText(this,"Data Berhasil Disimpan",Toast.LENGTH_LONG).show()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                },
                { error ->
                    progressnya.visibility= View.GONE
                    btnsimpanadd.visibility=View.VISIBLE
                    Alert.dialog(this,"Gagal Tambah Jawaban")
                    Log.e("RESULT1 ERROR", error.toString())
                }
            )
    }

    private fun performFileSearch() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this as Activity, permissions, permissionfile)
        }else {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, kodebacafile)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            performFileSearch()
        else
            Alert.dialog(this, "Maaf dengan tidak mengijinkan akses penyimpanan, " +
                    "tahapan penambahan file tidak dapat dilanjutkan.")
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