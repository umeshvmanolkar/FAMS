package com.jbit.fams;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Ramakrishna on 07-Jun-21.
 */

public class AttendancePresentAdapter extends BaseAdapter {

    String[] arrayLists;
    Context context;
    // invoke the suitable constructor of the ArrayAdapter class
    public AttendancePresentAdapter(@NonNull Context context, String[] arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        this.arrayLists = arrayList;
        this.context=context;

    }

    @Override
    public int getCount() {
        return arrayLists.length;
    }

    @Override
    public Object getItem(int position) {
        return arrayLists[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(context).inflate(R.layout.listitems_layout, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        String date = (String)getItem(position);

        TextView textView1 = currentItemView.findViewById(R.id.tvDates);
        textView1.setText(date);
        TextView  tvPresent = currentItemView.findViewById(R.id.tvPresent);
        tvPresent.setText("Present");


        return currentItemView;
    }

    public void updateAdapter(String[] arrayList){

        this.arrayLists = arrayList;
        notifyDataSetChanged();
        notifyDataSetInvalidated();
    }
}