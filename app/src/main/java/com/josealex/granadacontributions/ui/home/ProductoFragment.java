package com.josealex.granadacontributions.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josealex.granadacontributions.R;

public class ProductoFragment extends Fragment {

    public static final String PRODUCT_TITLE_BUNDLE_ID = "title";

    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_producto, container, false);

        Bundle arguments = getArguments();
        arguments.getString(PRODUCT_TITLE_BUNDLE_ID);
        arguments.getString(PRODUCT_TITLE_BUNDLE_ID);

        return root;
    }
}