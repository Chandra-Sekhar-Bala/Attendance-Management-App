package com.example.smartattendance.database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.database.Sem.semDataClass
import com.example.smartattendance.fragment.AddDbFragment

class streamAdapterClass(private val userList: ArrayList<streamDataClass>, private val context: AddDbFragment): RecyclerView.Adapter<streamAdapterClass.MyViewHolder>() {

    lateinit var listener: onItemClickListener
    lateinit var name:String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(parent.context).
        inflate(R.layout.item_view,parent,false)

        return MyViewHolder(itemView)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        this.listener=listener
    }

    override fun onBindViewHolder(holder: streamAdapterClass.MyViewHolder, position: Int) {
        val currentItem=userList[position]
        holder.streamD.text=currentItem.StreamName
        holder.itemView.setOnClickListener(){
            listener.onClicked(currentItem.StreamName.toString())
        }
    }


    override fun getItemCount(): Int {
        return userList.size
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val streamD: TextView = itemView.findViewById(R.id.item)
    }
    interface onItemClickListener{
        public fun onClicked(DataName:String)
    }

}