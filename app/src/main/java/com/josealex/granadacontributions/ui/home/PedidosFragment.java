package com.josealex.granadacontributions.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Pedido;

public class PedidosFragment extends Fragment {

    public static final String PEDIDOS_BUNDLE_ID = "pedido";
    public static final String PEDIDOS_TITLE_BUNDLE_ID = "title";

    private View root;
    private Pedido pedido;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_pedido, container, false);

        pedido = (Pedido) getArguments().getSerializable(PEDIDOS_BUNDLE_ID);

        return root;
    }
}
