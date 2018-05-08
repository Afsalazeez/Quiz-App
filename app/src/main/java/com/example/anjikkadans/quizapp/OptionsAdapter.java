package com.example.anjikkadans.quizapp;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Anjikkadan's on 5/8/2018.
 */

public class OptionsAdapter extends ArrayAdapter<String> {

    ChoiceClickCallback choiceClickCallback;


    public OptionsAdapter(Context context, int resource,String[] objects) {
        super(context, resource, objects);
        choiceClickCallback = (ChoiceClickCallback) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.quiz_options_view,parent,false);

        }
        final String choiceText = getItem(position);

        TextView choice = (TextView) convertView.findViewById(R.id.text_view_choice);
        View view = (View) convertView.findViewById(R.id.view_choice);
        view.setVisibility(View.INVISIBLE);

        choice.setText(choiceText);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (choiceClickCallback.choiceClicked(choiceText)) {
                   view.setBackgroundResource(R.color.colorAccent);
                   view.setVisibility(View.VISIBLE);
                }else{
                    view.setVisibility(View.VISIBLE);
                    view.setBackgroundResource(R.color.red);
                }



            }
        });
        return convertView;
    }


}
