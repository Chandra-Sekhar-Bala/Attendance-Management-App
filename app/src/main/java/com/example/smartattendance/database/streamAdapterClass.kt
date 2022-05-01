package com.example.smartattendance.database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.fragment.AddDbFragment
import com.google.firebase.database.ValueEventListener

class streamAdapterClass(private val userList: ArrayList<streamDataClass>, private val listener: StreamItemCLicked): RecyclerView.Adapter<streamAdapterClass.MyViewHolder>() {

    lateinit var name:String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        val viewHolder = MyViewHolder(view)
        view.setOnClickListener{
            listener.onItemCLicked(userList[viewHolder.adapterPosition].StreamName!!)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=userList[position]
        holder.streamD.text=currentItem.StreamName

    }


    override fun getItemCount(): Int {
        return userList.size
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val streamD: TextView = itemView.findViewById(R.id.item)

    }
    interface StreamItemCLicked {
        fun onItemCLicked(item:String)
    }

}