package com.tisoft_id.elearningbimbel.activity

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.tisoft_id.elearningbimbel.R
import com.tisoft_id.elearningbimbel.core.APIretrofit
import com.tisoft_id.elearningbimbel.core.Preferences
import com.tisoft_id.elearningbimbel.models.Chats
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DiskusiFragment : Fragment() {

    private lateinit var progressnya: ProgressBar
    private lateinit var fragmentContext: Context
    private lateinit var recyclerView: RecyclerView
    private lateinit var xtextchat: EditText
    private lateinit var xbtnsendchat:Button
    private lateinit var items: List<Chats>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var xiduser:Int=0
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentContext = context!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (!isAdded){return null;}
        val view = inflater.inflate(R.layout.fragment_diskusi, container, false)
        recyclerView = view.findViewById(R.id.recycler_diskusi) as RecyclerView
        xtextchat = view.findViewById(R.id.textchating) as EditText
        xbtnsendchat= view.findViewById(R.id.btnsendchat) as Button
        layoutManager = LinearLayoutManager(fragmentContext)
        xiduser=Preferences(fragmentContext).getInt("iduser")
        xbtnsendchat.setOnClickListener { sendchat() }
        sinkron()
        return view
    }
    private fun sinkron(){
        APIretrofit.create().chating()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { chating ->
                    items=chating
                    if (items.isNotEmpty())
                        setui()
                },
                { error ->
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
    private fun sendchat(){
        if (xtextchat.length()<=0) return

        val strchat=xtextchat.text.toString()
        xtextchat.setText("")
        APIretrofit.create().newchat(xiduser,strchat)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { chating ->
                    items=chating
                    if (items.isNotEmpty())
                        setui()
                    /*if (chating.status)
                        sinkron()*/
                },
                { error ->
                    Log.e("RESULT1 ERROR", error.toString())
                }
            )
    }
    internal inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_diskusi, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            if (xiduser==item.userid){
                holder.chatright.visibility=View.GONE
                holder.chatleft.visibility=View.INVISIBLE
            }else{
                holder.chatleft.visibility=View.GONE
                holder.chatright.visibility=View.INVISIBLE
            }
            holder.textmapel.text = item.username
            holder.textchat.text=item.chat
            holder.textjam.text=item.createdat
        }

        override fun getItemCount(): Int {
            return items.size
        }

        internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var chatleft:TextView=view.findViewById(R.id.chatleft)
            var chatright:TextView=view.findViewById(R.id.chatrigth)
            var textmapel: TextView = view.findViewById(R.id.textjudul)
            var textchat:TextView=view.findViewById(R.id.textchat)
            var textjam:TextView=view.findViewById(R.id.textjam)
        }
    }
}