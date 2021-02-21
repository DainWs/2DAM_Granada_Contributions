package com.josealex.granadacontributions.ui.home;

import android.content.Context;
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
import com.josealex.granadacontributions.modules.Pedido;
import com.josealex.granadacontributions.utils.GlobalInformation;
import com.josealex.granadacontributions.utils.PedidosFactory;

public class ShoppingCardFragment extends Fragment {

    private View root;

    private MyLineaspedidoAdapter adapter;
    private RecyclerView rcwlineas;
    private TextView totalpedido;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_pedido, container, false);
        GlobalInformation.mainActivity.setShoppingItemState(false);
        rcwlineas = root.findViewById(R.id.include2);
        totalpedido = root.findViewById(R.id.textViewTOTAL);
        adapter = new MyLineaspedidoAdapter(PedidosFactory.get().getLineas());
        update();
        return root;
    }
    public void update(){
         //  totalpedido.setText(PedidosFactory.get().);
            adapter.update(PedidosFactory.get().getLineas());
            rcwlineas.setAdapter(adapter);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        GlobalInformation.mainActivity.setShoppingItemState(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        GlobalInformation.mainActivity.setShoppingItemState(true);
    }
}
