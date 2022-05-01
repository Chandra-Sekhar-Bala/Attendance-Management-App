package com.example.smartattendance.database.Sem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.database.streamAdapterClass
import com.example.smartattendance.fragment.AddSemFragment

class semAdapterClass(private val userList: ArrayList<semDataClass>, val context: AddSemFragment): RecyclerView.Adapter<semAdapterClass.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return MyViewHolder(view)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=userList[position]
        holder.semD.text=currentItem.sem
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val semD: TextView = itemView.findViewById(R.id.item)
    }
}