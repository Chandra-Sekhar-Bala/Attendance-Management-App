package com.example.smartattendance.database.pre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.activities.stdDate


class preAdapterClass(private var userList: ArrayList<preDataClass>, private val listener: stdDate): RecyclerView.Adapter<preAdapterClass.MyViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): preAdapterClass.MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        val viewHolder = preAdapterClass.MyViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: preAdapterClass.MyViewHolder, position: Int) {
        val currentItem=userList[position]
        holder.DateD.text=currentItem.datep
        holder.AOrP.text=currentItem.AOrP
    }


    override fun getItemCount(): Int {
        return userList.size
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val DateD: TextView = itemView.findViewById(R.id.dateP)
        val AOrP: TextView =itemView.findViewById(R.id.stdPre)

    }
}