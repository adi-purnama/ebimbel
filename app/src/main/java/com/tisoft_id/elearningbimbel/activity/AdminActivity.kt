package com.tisoft_id.elearningbimbel.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.tisoft_id.elearningbimbel.R
import com.tisoft_id.elearningbimbel.core.APIretrofit
import com.tisoft_id.elearningbimbel.core.Preferences
import com.tisoft_id.elearningbimbel.models.UserList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {

    lateinit var items: List<UserList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        logoutOnClick.setOnClickListener { logout() }
        val ambilmapel=APIretrofit.create().getUser()
        ambilmapel.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { userlist ->
                    items=userlist
                    if (items.isNotEmpty())
                        setui()
                },
                { error ->
                    Log.e("RESULT1 ERROR", error.toString())
                }
            )
    }
    private fun activasi(xactivasi:Int){
        val alertDialog = AlertDialog.Builder(this)
        if (xactivasi>0){
            val reqactivasi=APIretrofit.create().getActivasi(xactivasi)
            reqactivasi.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { sukses ->
                        /*val idmapel=menu.idmapel
                        val nmmapel=menu.namapel*/
                        Log.d("RESULT1",sukses.toString())
                        alertDialog.setMessage("Activasi berhasil")
                        alertDialog.setNegativeButton("OK", null)
                        alertDialog.show()
                    },
                    { error ->
                        alertDialog.setMessage("Koneksi Error")
                        alertDialog.setNegativeButton("OK", null)
                        alertDialog.show()
                        Log.e("RESULT1 ERROR", error.toString())
                    }
                )
        }
    }
    private fun logout(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage("Yakin akan Logout?")
        alertDialog.setPositiveButton("Ya") { _, _ ->
            val preferences=Preferences(this)
            preferences.delete("login")
            preferences.delete("jenisuser")
            val inten=Intent(this@AdminActivity,LoginActivity::class.java)
            startActivity(inten)
            finish()
        }
        alertDialog.setNegativeButton("Tidak", null)
        alertDialog.show()
    }
    private fun setui(){
        recycler_mapel.adapter=RecyclerViewAdapter()
        recycler_mapel.layoutManager= LinearLayoutManager(this)
        recycler_mapel.isFocusable=false
    }
    internal inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_listuser, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]

            holder.textmapel.text = item.nama
            holder.textalamat.text=item.alamat
            holder.textemail.text=item.emailnya
            holder.textnotelp.text=item.notelp
            holder.switstatus.isChecked= item.statususer != 0
            if (item.statususer>0){
                holder.switstatus.text="Active"
                holder.switstatus.isEnabled=false
            }else{
                holder.switstatus.isEnabled=true
                holder.switstatus.text="New"
                holder.switstatus.setOnClickListener {
                    Log.d("click",item.idlogin.toString())
                    activasi(item.idlogin)
                    holder.switstatus.isEnabled=false
                }
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var textmapel: TextView = view.findViewById(R.id.textjudul)
            var textalamat: TextView = view.findViewById(R.id.textalamat)
            var textemail: TextView = view.findViewById(R.id.textemail)
            var textnotelp: TextView = view.findViewById(R.id.textnotelp)
            var switstatus:Switch=view.findViewById(R.id.switch1)
        }
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
