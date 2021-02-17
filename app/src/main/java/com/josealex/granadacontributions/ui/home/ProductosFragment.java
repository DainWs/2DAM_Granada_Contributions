package com.josealex.granadacontributions.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.ui.rview.MyproductosRecyclerViewAdapter;

import java.util.ArrayList;


public class ProductosFragment extends Fragment {

    private View root;
    private View viewrcwproductos;
    private  ArrayList<Productos> productoslista;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_productos, container, false);
        productoslista = (ArrayList<Productos>) getArguments().getSerializable("listaproductos");
        viewrcwproductos = root.findViewById(R.id.include);

        verProductos(productoslista);
        return  root;
    }

    public void verProductos(ArrayList Productos){
        RecyclerView recyclerView = (RecyclerView) viewrcwproductos;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyproductosRecyclerViewAdapter(Productos));
    }
}