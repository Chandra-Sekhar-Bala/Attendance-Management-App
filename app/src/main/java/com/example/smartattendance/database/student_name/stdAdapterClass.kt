package com.example.smartattendance.database.student_name

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.activities.AddName
import com.example.smartattendance.model.CardModel


class stdAdapterClass (private val userArrayList: ArrayList<CardModel>, private val listener:AddName):RecyclerView.Adapter<stdAdapterClass.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): stdAdapterClass.MyViewHolder {
    val view= LayoutInflater.from(parent.context).inflate(R.layout.item_view_std,parent,false)
    val viewHolder = stdAdapterClass.MyViewHolder(view)
        view.setOnClickListener{
            listener.onItemCLickedStd(userArrayList[viewHolder.adapterPosition].roll!!)
        }
    return viewHolder
}

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=userArrayList[position]
        holder.rollD.text=currentItem.roll
        holder.nameD.text=currentItem.name
        Log.e("LAWRA",currentItem.present.toString())
        holder.pd.text= currentItem.present.toString()


    }

    override fun getItemCount(): Int {
        return userArrayList.size
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val nameD: TextView = itemView.findViewById(R.id.stdName)
        val rollD:TextView=itemView.findViewById(R.id.stdRoll)
        val pd:TextView=itemView.findViewById(R.id.stdPre)

    }
    private fun removeItem(position: Int){
        userArrayList.removeAt(position)

    }
    interface stdItemCLicked {
        fun onItemCLickedStd(itemCLicked: String?)
    }
}