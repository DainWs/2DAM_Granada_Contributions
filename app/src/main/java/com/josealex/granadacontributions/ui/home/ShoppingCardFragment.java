package com.josealex.granadacontributions.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.Pedido;
import com.josealex.granadacontributions.utils.GlobalInformation;
import com.josealex.granadacontributions.utils.PedidosFactory;

public class ShoppingCardFragment extends Fragment {

    private View root;
    private Pedido pedido;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_pedido, container, false);
        pedido = PedidosFactory.get();

        return root;
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
