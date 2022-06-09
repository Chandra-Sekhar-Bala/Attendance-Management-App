package com.example.smartattendance.database.Sem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.activities.SemAdd
import com.example.smartattendance.adapters.semDataClass

class semAdapterClass(private val userList: ArrayList<semDataClass>, private val listener: SemAdd): RecyclerView.Adapter<semAdapterClass.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        val viewHolder = MyViewHolder(view)
        view.setOnClickListener{
            listener.onItemCLicked(userList[viewHolder.adapterPosition].sem!!)
        }
            return viewHolder
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=userList[position]
        holder.semD.text=currentItem.sem
        holder.deleteButton.setOnClickListener {
            listener.onDeleteClicked(currentItem.sem, position)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val semD: TextView = itemView.findViewById(R.id.item)
        val deleteButton: ImageView =itemView.findViewById(R.id.delete_item)
    }

    interface semItemCLicked {
        fun onItemCLicked(item:String)
        fun onDeleteClicked(itemCLicked: String?,position: Int)
    }
}