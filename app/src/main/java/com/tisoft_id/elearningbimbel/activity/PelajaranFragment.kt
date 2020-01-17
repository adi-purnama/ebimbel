package com.tisoft_id.elearningbimbel.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.tisoft_id.elearningbimbel.BuildConfig
import com.tisoft_id.elearningbimbel.R
import com.tisoft_id.elearningbimbel.core.APIretrofit
import com.tisoft_id.elearningbimbel.core.Alert
import com.tisoft_id.elearningbimbel.core.Preferences
import com.tisoft_id.elearningbimbel.models.Chapter
import com.tisoft_id.elearningbimbel.models.MataPelajaran
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.net.URI
import java.text.DecimalFormat

class PelajaranFragment : Fragment() {

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private var savedPhoto: File = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOCUMENTS+"/.bimbel")
    private var namafile:String?=null

    private lateinit var fragmentContext: Context
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressnya:ProgressBar
    lateinit var items: List<MataPelajaran>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var typeuser:Int=1
    private var guruid:Int=0
    private var resultok:Int=212

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentContext = context!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (!isAdded){return null;}
        val view = inflater.inflate(R.layout.fragment_pelajaran, container, false)
        recyclerView = view.findViewById(R.id.recycler_mapel) as RecyclerView
        progressnya=view.findViewById(R.id.progressnya) as ProgressBar
        layoutManager = LinearLayoutManager(fragmentContext)
        val preferences= Preferences(fragmentContext)
        typeuser=preferences.getInt("jenisuser")
        guruid=preferences.getInt("iduser")
        sinkron()
        return view
    }
    private fun sinkron(){
        progressnya.visibility=View.VISIBLE
        val ambilmapel=APIretrofit.create().getPelajaran("subjects")
        ambilmapel.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { mapel ->
                    progressnya.visibility=View.GONE
                    /*val idmapel=menu.idmapel
                    val nmmapel=menu.namapel
                    Log.d("RESULT1",mapel[0].chapter.toString())*/
                    items=mapel
                    if (items.isNotEmpty())
                        setui()
                },
                { error ->
                    progressnya.visibility=View.GONE
                    Log.e("RESULT1 ERROR", error.toString())
                }
            )
    }
    private fun setui(){
        recyclerView.adapter = RecyclerViewAdapter()
        recyclerView.layoutManager = layoutManager
        recyclerView.isFocusable = false
        recyclerView.isNestedScrollingEnabled = false
    }
    private fun hapus(xidmateri:Int){
        progressnya.visibility=View.VISIBLE
        val ambilmapel=APIretrofit.create().hapusmateri(xidmateri)
        ambilmapel.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { hapusmapel ->
                    progressnya.visibility=View.GONE
                    if (hapusmapel.status) {
                        Alert.dialog(fragmentContext, "Data Berhasil dihapus")
                        sinkron()
                    }
                },
                { error ->
                    Alert.dialog(fragmentContext,"Gagal Dihapus")
                    progressnya.visibility=View.GONE
                    Log.e("RESULT1 ERROR", error.toString())
                }
            )
    }

    private fun simpan(xurl:String?){
        if (xurl.isNullOrEmpty()){
            Alert.dialog(fragmentContext,"Gagal Simpan")
            return
        }
        namafile=xurl
        progressnya.visibility=View.VISIBLE
        val ambilmapel=APIretrofit.view().simpan(xurl)
        ambilmapel.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                /*progBaritenerary.visibility=View.GONE*/
                Alert.dialog(fragmentContext,"Koneksi Gagal")
                progressnya.visibility=View.GONE
                t.printStackTrace()
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressnya.visibility=View.GONE
                if (response.isSuccessful) {
                    if (saveToDisk(response.body())){
                        Log.d("ITENERARY RSP","TRUE")
                    }else{
                        /*Alert.dialog(fragmentContext,"Gagal Simpan")*/
                        Log.d("ITENERARY RSP","FALSE")
                    }
                }else{
                    /*Alert.dialog(fragmentContext,"Gagal Simpan")*/
                    Log.d("ITENERARY Error",response.errorBody().toString())
                }
            }
        })
    }

    private fun bukafile(file:File){
        try {
            //val url=Uri.fromFile(file)
            //FileProvider.getUriForFile
            val url=FileProvider.getUriForFile(fragmentContext, BuildConfig.APPLICATION_ID + ".provider",file)
            val mimetype=if (url.toString().contains(".doc") || url.toString().contains(".docx")){
                "application/msword"
            }else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                 "application/vnd.ms-powerpoint"
            }else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                "application/vnd.ms-excel"
            } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
                "application/zip"
            } else if(url.toString().contains(".rtf")) {
                 "application/rtf"
            } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                "audio/x-wav"
            } else if(url.toString().contains(".gif")) {
                 "image/gif"
            } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                 "image/jpeg"
            } else if(url.toString().contains(".txt")) {
                "text/plain"
            }else if(url.toString().contains(".pdf")){
                "application/pdf"
            }else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")){
                "video/*"
            }else{
                "*/*"
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(url, mimetype)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Alert.dialog(fragmentContext,"File tidak dapat di buka")
        }
    }

    private fun saveToDisk(body: ResponseBody?):Boolean {
        if (body == null) {
            return false
        }
        try {
            if (!savedPhoto.isDirectory) {
                savedPhoto.mkdirs()
            }
            val futureStudioIconFile =File(savedPhoto,namafile)
            try {
                val fileReader = ByteArray(4096)
                /*val fileSize = body.contentLength()*/
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)
                while (true) {
                    val read = inputStream!!.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    (outputStream as FileOutputStream).write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    /*Log.d("ITENERARY SAVE", "file download: $fileSizeDownloaded of $fileSize")*/
                }
                (outputStream as FileOutputStream).flush()
                return true
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            } finally {
                inputStream?.close()
                outputStream?.close()
                val alertDialog = AlertDialog.Builder(fragmentContext)
                alertDialog.setMessage("Materi berhasil disimpan")
                alertDialog.setNegativeButton("OK", null)
                alertDialog.show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == resultok && resultCode == Activity.RESULT_OK) {
            sinkron()
        }
    }
    internal inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_matapelajaran, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.textmapel.text = item.namapel
            holder.textmapel.setOnClickListener {
                if (holder.recyclerView.visibility==View.VISIBLE){
                    holder.recyclerView.visibility=View.GONE
                }else{
                    holder.recyclerView.visibility=View.VISIBLE
                }
            }
            if (!item.chapter.isNullOrEmpty()) {
                /*Log.d("CHAPTER", item.chapter.toString())*/
                val detailadapter=item.chapter
                holder.recyclerView.adapter = ChildRecyclerViewAdapter(detailadapter!!)
                holder.recyclerView.layoutManager = LinearLayoutManager(fragmentContext)
                holder.recyclerView.isFocusable = false
            }
            if (typeuser==2){
                holder.btnadd.visibility=View.INVISIBLE
            }else{
                holder.btnadd.setOnClickListener {
                    val intent = Intent(fragmentContext, AddMateriActivity::class.java)
                    intent.putExtra("materialid", item.idmapel)
                    intent.putExtra("materialname", item.namapel)
                    intent.putExtra("typeadd", 1)
                    startActivityForResult(intent,resultok)
                }
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var textmapel: TextView = view.findViewById(R.id.textjudul)
            var recyclerView: RecyclerView = view.findViewById(R.id.recycler_chapter)
            var btnadd:Button=view.findViewById(R.id.btntambahchapter)
        }
    }

    internal inner class ChildRecyclerViewAdapter(private val detailchapter: List<Chapter>) : RecyclerView.Adapter<ChildRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.recycle_chapterpelajaran, parent, false))
        }

        @SuppressLint("ResourceAsColor", "PrivateResource")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val detail = detailchapter[position]
            holder.titleTextView.text = detail.chapter
            if (!detail.url.isNullOrEmpty()) {
                namafile = detail.url!!.substring(detail.url!!.lastIndexOf("/") + 1)
            }
            if (!detail.deskripsi.isNullOrEmpty()){
                holder.deskripsiText.text=detail.deskripsi
                holder.deskripsiText.visibility=View.VISIBLE
            }
            if (detail.ukuran>0){
                val xukuran:Double=(detail.ukuran.toDouble()/1024)/1024
                val ukurantext="Ukuran File : ${DecimalFormat("##0.000").format(xukuran)} MB"
                holder.ukuranText.text=ukurantext
                holder.ukuranText.visibility=View.VISIBLE
            }
            if (typeuser==2){
                holder.btnhapus.visibility=View.GONE
                holder.btnedit.visibility=View.GONE
            }else {
                if (detail.guruid==guruid) {
                    holder.btnhapus.setOnClickListener {
                        val alertDialog = AlertDialog.Builder(fragmentContext)
                        alertDialog.setMessage("Yakin akan hapus Materi?")
                        alertDialog.setPositiveButton("Ya") { _, _ ->
                            hapus(detail.id)
                            /*Toast.makeText(fragmentContext, "Hapus Berhasil", Toast.LENGTH_SHORT).show()*/
                        }
                        alertDialog.setNegativeButton("Tidak", null)
                        alertDialog.show()
                    }
                    holder.btnedit.setOnClickListener {
                        /*edit materi*/
                        val intent = Intent(fragmentContext, AddMateriActivity::class.java)
                        intent.putExtra("materialid", detail.materialid)
                        intent.putExtra("materialname", detail.chapter)
                        intent.putExtra("chapterid", detail.id)
                        intent.putExtra("deskripsi", detail.deskripsi)
                        intent.putExtra("typeadd", 2)
                        startActivityForResult(intent,resultok)
                    }
                }else{
                    holder.btnedit.backgroundTintList= ColorStateList.valueOf(R.color.material_grey_600)
                    holder.btnhapus.backgroundTintList= ColorStateList.valueOf(R.color.material_grey_600)
                    holder.btnedit.isEnabled=false
                    holder.btnhapus.isEnabled=false
                }
            }
            holder.btnsave.setOnClickListener {
                val uri = URI(detail.url)
                val path = uri.path
                val xurl = path.substring(path.lastIndexOf('/') + 1)
                val xcekfile=File(savedPhoto,xurl)
                Log.d("EXT FILE",xurl)
                if (xcekfile.exists()){
                    bukafile(xcekfile)
                }else {
                    simpan(xurl)
                }
            }
        }

        override fun getItemCount(): Int {
            return detailchapter.size
        }

        internal inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            var titleTextView: TextView = view.findViewById(R.id.textjudul)
            var deskripsiText: TextView = view.findViewById(R.id.textdeskripsi)
            var ukuranText: TextView = view.findViewById(R.id.textukuranfile)
            var btnhapus: Button = view.findViewById(R.id.cmdhapuschapter)
            var btnedit: Button = view.findViewById(R.id.cmdeditchapter)
            var btnsave: Button = view.findViewById(R.id.cmddownloadchapter)
        }
    }
}