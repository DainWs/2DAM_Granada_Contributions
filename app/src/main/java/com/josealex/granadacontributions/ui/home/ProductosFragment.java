package com.josealex.granadacontributions.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.MarketsRecyclerAdapter;
import com.josealex.granadacontributions.adapters.ProductsRecyclerAdapter;
import com.josealex.granadacontributions.modules.Mercado;

import java.util.ArrayList;


public class ProductosFragment extends Fragment {

    private View root;
    private View viewRCWProductos;
    private Mercado mercado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_productos, container, false);
        mercado = (Mercado) getArguments().getSerializable(MarketsRecyclerAdapter.BUNDLE_MERCADO_ID);

        viewRCWProductos = root.findViewById(R.id.include);

        verProductos(mercado.getProductos());
        return  root;
    }

    public void verProductos(ArrayList Productos){
        RecyclerView recyclerView = (RecyclerView) viewRCWProductos;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ProductsRecyclerAdapter(Productos));
    }
}