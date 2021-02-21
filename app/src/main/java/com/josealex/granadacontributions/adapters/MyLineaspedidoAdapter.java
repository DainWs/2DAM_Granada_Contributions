package com.josealex.granadacontributions.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.LineaPedido;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Pedido;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.ui.home.ShoppingCardFragment;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.GlobalInformation;
import com.josealex.granadacontributions.utils.PedidosFactory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;


public class MyLineaspedidoAdapter extends RecyclerView.Adapter<MyLineaspedidoAdapter.ViewHolder> {

    private List<LineaPedido> mValues;
    DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();

    public MyLineaspedidoAdapter(List<LineaPedido> items ) {
        mValues = items;
        separadoresPersonalizados.setDecimalSeparator('.');

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
        private final TextView precioTotalLinea;
        private final Button mMinusButton;
        private final Button mPlusButton;
        public LineaPedido lineaPedido;
        public Productos producto;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_id_linea);
            mContentView = (TextView) view.findViewById(R.id.item_nombre_linea);
            mCantidadView = (TextView) view.findViewById(R.id.item_cantidad);
            mMinusButton = (Button) view.findViewById(R.id.item_minus_button);
            mPlusButton = (Button) view.findViewById(R.id.item_plus_button);
            precioTotalLinea = view.findViewById(R.id.textPtlinea);

        }

        public void start(LineaPedido lineaPedido, int position) {
            this.lineaPedido = lineaPedido;
            producto = PedidosFactory.getProducto(lineaPedido.getUidProducto());
            precioTotalLinea.setText(new DecimalFormat("#.00", separadoresPersonalizados ).format(lineaPedido.getPrecio())+"€");
            mContentView.setText(lineaPedido.getNombreProducto());
            mCantidadView.setText(lineaPedido.getCantidad()+"");
            mPlusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lineaPedido.getCantidad() < producto.getCantidad()) {
                        lineaPedido.addCantidad(1);
                        GlobalInformation.userShopping.update();
                        notifyDataSetChanged();
                    }
                }
            });

            mMinusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(lineaPedido.getCantidad()>1) {
                        lineaPedido.removeCantidad(1);
                        GlobalInformation.userShopping.update();
                        notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}