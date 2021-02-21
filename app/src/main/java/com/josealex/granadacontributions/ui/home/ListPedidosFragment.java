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
import com.josealex.granadacontributions.utils.NavigationManager;
import com.josealex.granadacontributions.utils.ResourceManager;

public class ListPedidosFragment extends Fragment {

    public static final String PEDIDO_TITLE_BUNDLE_ID = "title";
    public static final String PEDIDO_MARKET_BUNDLE_ID = "market";

    private View root;
    private RecyclerView recycler;
    private MyPedidoAdapter adapter;

    private Mercado market;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_list_pedidos, container, false);

        Bundle arguments = getArguments();
        market = (Mercado) arguments.getSerializable(PEDIDO_MARKET_BUNDLE_ID);

        recycler = root.findViewById(R.id.include);

        adapter = new MyPedidoAdapter(market.getPedidos()) {
            @Override
            public void onViewClick(View view, Pedido mItem, int position) {
                Bundle bundle = new Bundle();
                String title = ResourceManager.getString(R.string.orders_of) + market.getNombre();
                bundle.putString(PedidosFragment.PEDIDOS_TITLE_BUNDLE_ID, title);
                bundle.putSerializable(PedidosFragment.PEDIDOS_BUNDLE_ID, mItem);
                NavigationManager.navigateTo(R.id.action_list_pending_orders_to_pending_order, bundle);
            }
        };

        recycler.setAdapter(adapter);

        return root;
    }

    public void update() {

    }

}
