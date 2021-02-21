package com.josealex.granadacontributions.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.MyPedidoAdapter;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Pedido;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.utils.GlobalInformation;

public class ClientPendingOrdersFragment extends Fragment {

    public static final String PEDIDO_USER_BUNDLE_ID = "user";

    private View root;
    private RecyclerView recycler;
    private MyPedidoAdapter adapter;

    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_list_pedidos, container, false);

        user = GlobalInformation.SIGN_IN_USER;

        recycler = root.findViewById(R.id.include);

        adapter = new MyPedidoAdapter(user.getPedidosPendientes()) {
            @Override
            public void onViewClick(View view, Pedido mItem, int position) {

            }
        };

        recycler.setAdapter(adapter);

        return root;
    }

    public void update() {

    }

}
