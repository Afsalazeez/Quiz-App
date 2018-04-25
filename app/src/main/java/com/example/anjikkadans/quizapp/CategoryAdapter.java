package com.example.anjikkadans.quizapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anjikkadan's on 4/24/2018.
 */

public class CategoryAdapter extends ArrayAdapter<CategoryDet> {

    public CategoryAdapter(Context context, int resource, List<CategoryDet> objects) {

        super(context, resource, objects);
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.button_layout,parent,false);

        }
        CategoryDet currentCategoryDet = getItem(position);

        ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.button_choice);
        TextView textView = (TextView) convertView.findViewById(R.id.text_view_choice);

        imageButton.setImageResource(currentCategoryDet.getImageUrl());
        textView.setText(currentCategoryDet.getButtonText());

        return convertView;
    }
}
