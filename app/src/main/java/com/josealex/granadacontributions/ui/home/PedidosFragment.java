package com.josealex.granadacontributions.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.MyLineaspedidoAdapter;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Pedido;
import com.josealex.granadacontributions.utils.PedidosFactory;

public class PedidosFragment extends Fragment {

    public static final String PEDIDOS_BUNDLE_ID = "pedido";
    public static final String PEDIDOS_TITLE_BUNDLE_ID = "title";

    private View root;
    private Pedido pedido;

    private MyLineaspedidoAdapter adapter;
    private RecyclerView rcwlineas;

    public TextView totalpedido;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_pedido_managers, container, false);

        pedido = (Pedido) getArguments().getSerializable(PEDIDOS_BUNDLE_ID);

        totalpedido = root.findViewById(R.id.textViewTOTAL);
        rcwlineas = root.findViewById(R.id.include22);
        adapter = new MyLineaspedidoAdapter(pedido.getLineas());
        root.findViewById(R.id.refuse_button);
        root.findViewById(R.id.accept_button);


        return root;
    }
}
