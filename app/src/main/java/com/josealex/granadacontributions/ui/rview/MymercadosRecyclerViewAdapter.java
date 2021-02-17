package com.josealex.granadacontributions.ui.rview;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Productos;


import java.util.ArrayList;
import java.util.List;


public class MymercadosRecyclerViewAdapter extends RecyclerView.Adapter<MymercadosRecyclerViewAdapter.ViewHolder> {

    private final List<Mercado> mValuesMercados;
    private  ArrayList<Productos> mValuesPr = new ArrayList<>() ;
    private FragmentActivity activity;
    private Context context;


    public MymercadosRecyclerViewAdapter(ArrayList<Mercado> mercadoslista, Context context, FragmentActivity activity) {
        mValuesMercados = mercadoslista;
        this.context = context;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_itemmercado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mName.setText(mValuesMercados.get(position).getNombre());
        holder.mContentView.setText(mValuesMercados.get(position).getUid());
        holder.mProducts.setText("Mis Productos");
        holder.mProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   mValuesPr = mValuesMercados.get(position).getProductos();
                   Bundle bundle = new Bundle();
                   bundle.putSerializable("listaproductos",mValuesPr);

                  //Navigation.findNavController().navigate(, bundle);
                   NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
                   navController.navigate(R.id.action_nav_home_to_productosFragment,bundle);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValuesMercados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mProducts;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.item_name);
            mProducts = (TextView) view.findViewById(R.id.textView);
            mContentView = (TextView) view.findViewById(R.id.contentcorreo);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}