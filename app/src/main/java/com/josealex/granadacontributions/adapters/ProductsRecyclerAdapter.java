package com.josealex.granadacontributions.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.Productos;

import java.util.ArrayList;
import java.util.List;


public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder> {

    private List<Productos> mValues;
    private boolean isPrincipal = false;
    private boolean isAdmin = false;

    public ProductsRecyclerAdapter(List<Productos> items) {
        mValues = items;
        isPrincipal = true;
    }

    public ProductsRecyclerAdapter(List<Productos> items, boolean isAdmin) {
        mValues = items;
        this.isAdmin = isAdmin;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(mValues.get(position)!=null) holder.start(mValues.get(position), position);
    }

    public ArrayList<Productos> getList() {
        return new ArrayList<>(mValues);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void update(ArrayList<Productos> list) {
        mValues = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        private Productos mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_product_price);
            mContentView = view.findViewById(R.id.item_product_name);
        }

        public void start(Productos mItem, int position) {
            this.mItem = mItem;
            mIdView.setText(mItem.getPrecio()+"€");
            mContentView.setText(mItem.getNombre());

            if(isPrincipal) {
                mView.setOnClickListener( v -> onViewClick(mView, mItem, position) );
            }
            else {
                if (isAdmin) {
                    mView.setOnClickListener(v -> onViewClick(mView, mItem, position));
                }
            }
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    protected void onViewClick(View v, Productos producto, int position) {

    }
}