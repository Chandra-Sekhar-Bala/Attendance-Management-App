package com.example.smartattendance.database.pre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.activities.presentStd
import com.example.smartattendance.database.student_name.stdAdapterClass


class presentAdapterClass(private var userList: ArrayList<presentDataClass>, private val listener: presentStd): RecyclerView.Adapter<presentAdapterClass.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): presentAdapterClass.MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.pre_item_view,parent,false)
        val viewHolder = presentAdapterClass.MyViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: presentAdapterClass.MyViewHolder, position: Int) {
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