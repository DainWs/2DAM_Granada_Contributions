package com.josealex.granadacontributions.ui.rview;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.Productos;

import java.util.List;


public class MyproductosRecyclerViewAdapter extends RecyclerView.Adapter<MyproductosRecyclerViewAdapter.ViewHolder> {

    private final List<Productos> mValuesproductos;

    public MyproductosRecyclerViewAdapter(List<Productos> items) {
        mValuesproductos = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_itemproductos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        System.out.println(holder.mContentView);
        holder.mContentView.setText(mValuesproductos.get(position).getNombre());
        holder.mIdView.setText(mValuesproductos.get(position).getCantidad()+"");

    }

    @Override
    public int getItemCount() {
        return mValuesproductos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number2);
            mContentView = view.findViewById(R.id.content2);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}