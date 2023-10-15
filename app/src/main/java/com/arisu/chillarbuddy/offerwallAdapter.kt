package com.arisu.chillarbuddy

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog

class offerwallAdapter (private val line:ArrayList<offerwallData>): RecyclerView.Adapter<offerwallAdapter.LineViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.offerwall_design, parent, false)
        return LineViewHolder(view)
    }

    override fun getItemCount(): Int {

        return line.size
    }

    @SuppressLint("MissingInflatedId")
    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val currentposition=line[position]

        holder.title.text= currentposition.title
        holder.coin.text=currentposition.amount
        val desc=currentposition.des
        val link=currentposition.link



        Glide.with(holder.itemView.context)
            .load(currentposition.image)
            .into(holder.icon)

        val context = holder.itemView.context
        val url =link
        holder.link.setOnClickListener {

            val bottomSheetDialog = BottomSheetDialog(context)
            val view = LayoutInflater.from(context).inflate(R.layout.offerwall_bottom, null)


            val shareButton = view.findViewById<LinearLayout>(R.id.apply)
            val otitle=view.findViewById<EditText>(R.id.t1)
            val odes=view.findViewById<EditText>(R.id.t2)
            val oam=view.findViewById<EditText>(R.id.t3)
            otitle.setText(currentposition.title)
            odes.setText(currentposition.des)
            oam.setText(currentposition.amount)

            shareButton.setOnClickListener {
                // Handle the share action here
                bottomSheetDialog.dismiss()
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url as String?)))

            }

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }

    }

    fun updateTrans(updateTrans: ArrayList<offerwallData>) {
        line.clear()
        line.addAll(updateTrans)

        notifyDataSetChanged()

    }

class LineViewHolder(line: View) : RecyclerView.ViewHolder(line){
    val title: TextView =line.findViewById(R.id.offerwall_title)
    val icon: ImageView =line.findViewById(R.id.offer_logo)
    val coin: TextView =line.findViewById(R.id.coin)
    val link: LinearLayout =line.findViewById(R.id.buy_btn)
}
}