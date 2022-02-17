package com.example.smartattendance;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

import com.example.smartattendance.model.CardModel;

public class CardStackCallback extends DiffUtil.Callback {

    private List<CardModel> old, latest;

    public CardStackCallback(List<CardModel> old, List<CardModel> latest) {
        this.old = old;
        this.latest = latest;
    }

    @Override
    public int getOldListSize() {
        return old.size();
    }

    @Override
    public int getNewListSize() {
        return latest.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition).getImage() == latest.get(newItemPosition).getImage();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition) == latest.get(newItemPosition);
    }
}