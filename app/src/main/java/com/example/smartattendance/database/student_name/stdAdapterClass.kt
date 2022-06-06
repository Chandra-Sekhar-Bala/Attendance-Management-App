package com.example.smartattendance.database.student_name

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.R
import com.example.smartattendance.activities.AddName
import com.example.smartattendance.database.Sem.semAdapterClass

class stdAdapterClass (private val userArrayList: ArrayList<stdDataClass>, private val listener:AddName):RecyclerView.Adapter<stdAdapterClass.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): stdAdapterClass.MyViewHolder {
    val view= LayoutInflater.from(parent.context).inflate(R.layout.item_view_std,parent,false)
    val viewHolder = stdAdapterClass.MyViewHolder(view)
    return viewHolder
}

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=userArrayList[position]
        holder.rollD.text=currentItem.roll
        holder.nameD.text=currentItem.name
        holder.PresentD.text=currentItem.Present
        holder.deleteButton.setOnClickListener {
            listener.onDeleteClicked(currentItem.roll)
            removeItem(position)
        }
    }

    override fun getItemCount(): Int {
        return userArrayList.size
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val nameD: TextView = itemView.findViewById(R.id.stdName)
        val rollD:TextView=itemView.findViewById(R.id.stdRoll)
        val PresentD:TextView=itemView.findViewById(R.id.stdPre)
        val deleteButton:ImageButton =itemView.findViewById(R.id.delete_item_std)
    }
    private fun removeItem(position: Int){
        userArrayList.removeAt(position)

    }
    interface stdItemCLicked {
        fun onDeleteClicked(itemCLicked: String?)
    }
}