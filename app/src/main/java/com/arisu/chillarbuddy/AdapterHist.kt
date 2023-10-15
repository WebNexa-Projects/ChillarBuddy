package com.arisu.chillarbuddy

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterHist(private val line: ArrayList<historyData>): RecyclerView.Adapter<AdapterHist.LineViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_design, parent, false)
        return LineViewHolder(view)
    }

    override fun getItemCount(): Int {

        return line.size
    }

    @SuppressLint("MissingInflatedId")
    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val currentposition=line[position]

        holder.title.text= currentposition.purpose
        holder.coin.text=currentposition.amount


    }

    fun updateTrans(updateTrans: ArrayList<historyData>) {
        line.clear()
        line.addAll(updateTrans)

        notifyDataSetChanged()

    }

class LineViewHolder(line: View) : RecyclerView.ViewHolder(line){
    val title: TextView =line.findViewById(R.id.offerwall_title)
    val icon: ImageView =line.findViewById(R.id.offer_logo)
    val coin: TextView =line.findViewById(R.id.coin)
}
}