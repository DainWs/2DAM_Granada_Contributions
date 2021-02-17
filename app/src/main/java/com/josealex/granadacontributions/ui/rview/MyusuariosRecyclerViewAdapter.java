package com.josealex.granadacontributions.ui.rview;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.User;


import java.util.List;


public class MyusuariosRecyclerViewAdapter extends RecyclerView.Adapter<MyusuariosRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValuesusuarios;

    public MyusuariosRecyclerViewAdapter(List<User> items) {
        mValuesusuarios = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_itemusuario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mIdView.setText(mValuesusuarios.get(position).getNombre());
        holder.mContentView.setText(mValuesusuarios.get(position).getCorreo());
    }

    @Override
    public int getItemCount() {
        return mValuesusuarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView imagefotoURL;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_name);
            mContentView = (TextView) view.findViewById(R.id.contentcorreo);
            imagefotoURL = view.findViewById(R.id.imagefotoURL);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}