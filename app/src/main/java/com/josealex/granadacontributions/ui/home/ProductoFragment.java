package com.josealex.granadacontributions.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Productos;

public class ProductoFragment extends Fragment {

    public static final String PRODUCT_TITLE_BUNDLE_ID = "title";
    public static final String PRODUCT_MARKET_BUNDLE_ID = "market";
    public static final String PRODUCT_BUNDLE_ID = "product";

    private View root;

    private Mercado market;
    private Productos product;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_producto, container, false);

        Bundle arguments = getArguments();
        market = (Mercado) arguments.getSerializable(PRODUCT_MARKET_BUNDLE_ID);
        product = (Productos) arguments.getSerializable(PRODUCT_BUNDLE_ID);

        return root;
    }
}