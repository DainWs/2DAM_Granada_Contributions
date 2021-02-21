package com.josealex.granadacontributions.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.josealex.granadacontributions.R;

public class ModelsSpinnerAdapter implements SpinnerAdapter, ListAdapter {

    protected static final int EXTRA_ITEMS = 1;
    protected SpinnerAdapter adapter;
    protected LayoutInflater layoutInflater;
    protected String nothingText = "";

    public ModelsSpinnerAdapter(@NonNull SpinnerAdapter spinnerAdapter, @NonNull Context context, String firstText) {

        if(firstText.isEmpty()){
            nothingText = context.getString(R.string.spinner_nothing_selected);
        }else{
            nothingText = firstText;
        }

        this.adapter = spinnerAdapter;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public final View getView(int pos, View convertView, ViewGroup group) {
        /*if (pos == 0) {
            View view = layoutInflater.inflate(R.layout.single_spinner_item, group, false);
            TextView tv = view.findViewById(R.id.spinner_item_text);
            tv.setText(nothingText);
            return view;
        }*/
        View view = adapter.getView(pos, null, group);

        return view;
    }

    @Override
    public View getDropDownView(int pos, View convertView, ViewGroup group) {
        /*if (pos == 0) {
            View view = layoutInflater.inflate(R.layout.single_spinner_item, group, false);
            TextView tv = view.findViewById(R.id.spinner_item_text);
            tv.setText(nothingText);
            return view;
        }*/
        return adapter.getDropDownView(pos, null, group);
    }

    @Override
    public int getCount() {
        int count = adapter.getCount();
        return count;
    }

    @Override
    public Object getItem(int pos) {
        return adapter.getItem(pos);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return adapter.getItemId(position);
    }

    @Override
    public boolean hasStableIds() {
        return adapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return adapter.isEmpty();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        adapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        adapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

}

