package com.example.smartattendance.adapters

import com.example.smartattendance.model.CardModel
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.smartattendance.R
import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.Glide
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class CardStackAdapter(var items: List<CardModel>, var context: Context) :
    RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        Glide.with(context)
//                .load(items.get(position).getImage().replace("http","https"))
//                .placeholder(R.drawable.default_profile)
//                .into(holder.image);
        Glide.with(context).load(items[position].image!!.replace("http", "https"))
            .placeholder(R.drawable.default_profile).disallowHardwareConfig().into(holder.image)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Log.e("Imag", "IMG: " + items[position].image)
        holder.name.text = items[position].name
        holder.stream.text = items[position].sem
        holder.roll.text = "Roll: " + items[position].roll
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var name: TextView
        var stream: TextView
        var roll: TextView
        @SuppressLint("SetTextI18n")
        fun setData(data: CardModel?) {
        }

        init {
            image = itemView.findViewById(R.id.item_image)
            name = itemView.findViewById(R.id.item_name)
            stream = itemView.findViewById(R.id.item_stream)
            roll = itemView.findViewById(R.id.item_roll)
        }
    }
}