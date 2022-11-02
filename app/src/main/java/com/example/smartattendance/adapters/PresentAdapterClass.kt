package com.example.smartattendance.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.ui.StdPresentActivity
import com.example.smartattendance.model.presentDataClass


class presentAdapterClass(private var userList: ArrayList<presentDataClass>, private val listener: StdPresentActivity): RecyclerView.Adapter<presentAdapterClass.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.pre_item_view,parent,false)
        val viewHolder = MyViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=userList[position]
        holder.DateD.text=currentItem.date
        holder.PreD.text=currentItem.att
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val DateD: TextView = itemView.findViewById(R.id.dateP)
        val PreD: TextView =itemView.findViewById(R.id.stdPre)
    }
}