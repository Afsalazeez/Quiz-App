package com.example.anjikkadans.quizapp;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anjikkadan's on 4/24/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.OptionsViewHolder> {

    ArrayList<CategoryDet> optionsList;
    public CategoryAdapter(ArrayList<CategoryDet> optionsList) {

        super();
        this.optionsList = optionsList;
    }

    @Override
    public OptionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_layout,parent,false);

        return new OptionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder( OptionsViewHolder holder, int position) {
        CategoryDet currentCategoryDet = optionsList.get(position);

        holder.topicImageView.setImageResource(currentCategoryDet.getImageUrl());
        holder.topic.setText(currentCategoryDet.getButtonText());
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }
    class OptionsViewHolder extends RecyclerView.ViewHolder {
        TextView topic;
        ImageView topicImageView;

        public OptionsViewHolder(View itemView) {
            super(itemView);

            topic = (TextView) itemView.findViewById(R.id.card_text_view);
            topicImageView = (ImageView) itemView.findViewById(R.id.button_choice);

        }
    }
}
