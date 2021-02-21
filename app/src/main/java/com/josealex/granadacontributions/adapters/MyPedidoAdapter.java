package com.josealex.granadacontributions.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.Pedido;

import java.util.List;


public class MyPedidoAdapter extends RecyclerView.Adapter<MyPedidoAdapter.ViewHolder> {

    private List<Pedido> mValues;

    public MyPedidoAdapter(List<Pedido> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_pedidos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.start(mValues.get(position), position);
    }

    public void update(List<Pedido> pedidos) {
        mValues = pedidos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mClienteView;
        public final TextView mDateView;
        private Pedido mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_id);
            mClienteView = (TextView) view.findViewById(R.id.item_client_name);
            mDateView = (TextView) view.findViewById(R.id.item_date);
        }

        public void start(Pedido mItem, int position) {
            this.mItem = mItem;
            mIdView.setText(position+"");
            mClienteView.setText(mItem.getNombreCliente());
            mDateView.setText(mItem.getDate()+"");

            mView.setOnClickListener(v -> {
                onViewClick(mView, mItem, position);
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mClienteView.getText() + "'";
        }
    }

    public void onViewClick(View view, Pedido mItem, int position) {}
}