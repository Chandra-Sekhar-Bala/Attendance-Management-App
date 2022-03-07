package com.example.smartattendance.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.smartattendance.R;
import com.example.smartattendance.model.CardModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private List<CardModel> items;
    Context context;

    public CardStackAdapter(List<CardModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        Glide.with(context)
//                .load(items.get(position).getImage().replace("http","https"))
//                .placeholder(R.drawable.default_profile)
//                .into(holder.image);

        Glide.with(context).load(items.get(position).getImage().replace("http", "https")).
                placeholder(R.drawable.default_profile).disallowHardwareConfig().into(holder.image);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.e("Imag","IMG: "+items.get(position).getImage());

        holder.name.setText(items.get(position).getName());
        holder.stream.setText(items.get(position).getMyClass());
        holder.roll.setText("Roll: "+ items.get(position).getRoll());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, stream, roll;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            name = itemView.findViewById(R.id.item_name);
            stream = itemView.findViewById(R.id.item_stream);
            roll = itemView.findViewById(R.id.item_roll);
        }

        @SuppressLint("SetTextI18n")
        void setData(CardModel data) {


        }
    }

    public List<CardModel> getItems() {
        return items;
    }

    public void setItems(List<CardModel> items) {
        this.items = items;
    }
}
