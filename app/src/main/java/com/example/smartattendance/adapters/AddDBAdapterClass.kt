package com.example.smartattendance.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.ui.AddDBFragment
import com.example.smartattendance.model.addDBDataClass

class AddDBAdapterClass(private var userList: ArrayList<addDBDataClass>, private val listener: AddDBFragment): RecyclerView.Adapter<AddDBAdapterClass.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        val viewHolder = MyViewHolder(view)
        view.setOnClickListener{
            listener.onItemCLicked(userList[viewHolder.adapterPosition].StreamName!!)
        }
        return viewHolder
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=userList[position]
        holder.streamD.text=currentItem.StreamName
        holder.deleteButton.setOnClickListener {
            listener.onDeleteClicked(currentItem.StreamName, position)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val streamD: TextView = itemView.findViewById(R.id.item)
        val deleteButton: ImageView =itemView.findViewById(R.id.delete_item)

    }
    private fun removeItem(position: Int){
        userList.removeAt(position)
        // notifyItemRemoved(position)
    }
    interface StreamItemCLicked {
        fun onItemCLicked(item:String)
        fun onDeleteClicked(itemCLicked: String?, position: Int)
    }
}