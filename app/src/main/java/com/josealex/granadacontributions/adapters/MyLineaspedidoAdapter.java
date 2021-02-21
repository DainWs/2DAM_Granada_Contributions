package com.josealex.granadacontributions.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.LineaPedido;

import java.util.List;


public class MyLineaspedidoAdapter extends RecyclerView.Adapter<MyLineaspedidoAdapter.ViewHolder> {

    private List<LineaPedido> mValues;

    public MyLineaspedidoAdapter(List<LineaPedido> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_linea_pedidos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.start(mValues.get(position), position);
    }

    public void update(List<LineaPedido> pedidos) {
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
        public final TextView mContentView;
        private final TextView mCantidadView;
        private final Button mMinusButton;
        private final Button mPlusButton;
        public LineaPedido lineaPedido;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_id_linea);
            mContentView = (TextView) view.findViewById(R.id.item_nombre_linea);
            mCantidadView = (TextView) view.findViewById(R.id.item_cantidad);
            mMinusButton = (Button) view.findViewById(R.id.item_minus_button);
            mPlusButton = (Button) view.findViewById(R.id.item_plus_button);
        }

        public void start(LineaPedido lineaPedido, int position) {
            this.lineaPedido = lineaPedido;
            mIdView.setText(position);
            mContentView.setText(lineaPedido.getNombreProducto());
            mCantidadView.setText(lineaPedido.getCantidad());
            mPlusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lineaPedido.addCantidad(1);

                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}