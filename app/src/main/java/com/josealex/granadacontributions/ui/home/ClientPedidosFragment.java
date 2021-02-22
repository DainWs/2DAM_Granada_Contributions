package com.josealex.granadacontributions.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.MyLineaspedidoAdapter;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Pedido;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.DialogsFactory;
import com.josealex.granadacontributions.utils.GlobalInformation;

import java.util.ArrayList;

public class ClientPedidosFragment extends Fragment {
    public static final String PEDIDOS_BUNDLE_ID = "pedido";
    public static final String PEDIDOS_TITLE_BUNDLE_ID = "title";

    private View root;
    private Pedido pedido;

    private MyLineaspedidoAdapter adapter;
    private RecyclerView rcwLines;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_pedido_managers, container, false);

        pedido = (Pedido) getArguments().getSerializable(PEDIDOS_BUNDLE_ID);

        ((TextView)root.findViewById(R.id.textViewTOTAL)).setText(pedido.getTotal()+"");
        rcwLines = root.findViewById(R.id.include22);
        adapter = new MyLineaspedidoAdapter(pedido.getLineas(), R.layout.list_item_linea_pedidos_manages);
        rcwLines.setAdapter(adapter);

        Button cancelButton = root.findViewById(R.id.refuse_button);
        cancelButton.setText(R.string.cancel_order_button);
        cancelButton.setOnClickListener(v -> {
            DialogsFactory.makeAreYouSureDialog(
                    R.string.cancel_order_warning,
                    R.string.cannot_undo_this_action,
                    R.string.yes,
                    R.string.no,
                    (dialog, which) -> {

                        ArrayList<User> users = Consulta.getUsersWhere(new Consulta<User>() {
                            @Override
                            public boolean comprueba(User o) {
                                return o.getUid().equals(pedido.getUidCliente());
                            }
                        });

                        if(users.size() > 0) {
                            User user = users.get(0);

                            ArrayList<Mercado> mercados = Consulta.getMercadosWhere(new Consulta<Mercado>() {
                                @Override
                                public boolean comprueba(Mercado o) {
                                    return o.getUid().equals(pedido.getUidMercado());
                                }
                            });

                            Mercado mercado = null;
                            if(mercados.size() > 0) {
                                mercado = mercados.get(0);
                            }
                            else {
                                user.removePedidosWhere(new Consulta<Pedido>() {
                                    @Override
                                    public boolean comprueba(Pedido o) {
                                        return o.getUidMercado().equals(pedido.getUidMercado());
                                    }
                                });
                            }

                            if(mercado != null) {
                                mercado.removePedido(pedido);
                                FirebaseDBManager.saveMercado(mercado);
                            }

                            user.removePedidos(pedido);
                            FirebaseDBManager.saveUserData(user);
                            GlobalInformation.mainActivity.onBackPressed();
                        }
                    }
                );
            }
        );

        View acceptButton = root.findViewById(R.id.accept_button);
        acceptButton.setVisibility(View.INVISIBLE);
        acceptButton.setEnabled(false);

        return root;
    }
}
