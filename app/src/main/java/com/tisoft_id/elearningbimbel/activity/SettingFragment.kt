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
import android.widget.TextView
import com.tisoft_id.elearningbimbel.R
import com.tisoft_id.elearningbimbel.core.APIretrofit
import com.tisoft_id.elearningbimbel.models.MataPelajaran
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SettingFragment : Fragment() {

    private lateinit var fragmentContext: Context
    private lateinit var recyclerView: RecyclerView
    lateinit var items: List<MataPelajaran>
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentContext = context!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (!isAdded){return null;}
        val view = inflater.inflate(R.layout.fragment_pelajaran, container, false)
        recyclerView = view.findViewById(R.id.recycler_mapel) as RecyclerView
        layoutManager = LinearLayoutManager(fragmentContext)
        val ambilmapel=APIretrofit.create().getPelajaran("subjects")
        ambilmapel.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { mapel ->
                    /*val idmapel=menu.idmapel
                    val nmmapel=menu.namapel*/
                    Log.d("RESULT1",mapel[0].chapter.toString())
                    items=mapel
                    if (items.isNotEmpty())
                        setui()
                },
                { error ->
                    Log.e("RESULT1 ERROR", error.toString())
                }
            )
        return view
    }

    private fun setui(){
        recyclerView.adapter = RecyclerViewAdapter()
        recyclerView.layoutManager = layoutManager
        recyclerView.isFocusable = false
        recyclerView.isNestedScrollingEnabled = false
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
        }

        override fun getItemCount(): Int {
            return items.size
        }

        internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var textmapel: TextView = view.findViewById(R.id.textjudul)
            var recyclerView: RecyclerView = view.findViewById(R.id.recycler_chapter)
        }
    }

}