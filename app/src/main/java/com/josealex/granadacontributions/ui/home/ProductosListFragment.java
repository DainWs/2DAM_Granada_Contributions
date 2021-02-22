package com.josealex.granadacontributions.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.ProductsRecyclerAdapter;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.DialogsFactory;
import com.josealex.granadacontributions.utils.GlobalInformation;
import com.josealex.granadacontributions.utils.PedidosFactory;
import com.josealex.granadacontributions.utils.ResourceManager;

import java.util.ArrayList;


public class ProductosListFragment extends Fragment {

    public static final int ALL_PRODUCTS = 0;
    public static final int ALL_PRODUCTS_FROM_ONE_MARKET = 1;

    public static final String PRODUCTS_LIST_TITLE_BUNDLE_ID = "title";
    public static final String PRODUCTS_LIST_MODE_BUNDLE_ID = "mode";
    public static final String PRODUCTS_LIST_MARKET_BUNDLE_ID = "market";
    public static final String PRODUCTS_LIST_USER_BUNDLE_ID = "user";

    private boolean hasStarted = false;

    private View root;
    private View viewRCWProductos;
    private int mode = 0;
    private Mercado mercado;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_list_productos, container, false);

        hasStarted = true;
        GlobalInformation.productosListFragment = this;

        Bundle arguments = getArguments();
        int mode = arguments.getInt(PRODUCTS_LIST_MODE_BUNDLE_ID);
        mercado = (Mercado) arguments.getSerializable(PRODUCTS_LIST_MARKET_BUNDLE_ID);
        user = (User) arguments.getSerializable(PRODUCTS_LIST_USER_BUNDLE_ID);

        viewRCWProductos = root.findViewById(R.id.include);

        if(mode == ALL_PRODUCTS_FROM_ONE_MARKET) {
            verProductos(mercado.getProductos());
        }
        else {
            verProductos(new ArrayList<Productos>());
            /*ArrayList<Productos> productos = new ArrayList<>();
            for (Mercado mercado:GlobalInformation.MERCADOS) {
                productos.addAll(mercado.getProductos());
            }
            verProductos(productos);*/
        }
        return root;
    }

    public void verProductos(ArrayList Productos){
        RecyclerView recyclerView = (RecyclerView) viewRCWProductos;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ProductsRecyclerAdapter(Productos) {
            @Override
            protected void onViewClick(View v, Productos producto, int position) {
                DialogsFactory.makeAddToShoppingCartDialog((dialog, which) -> {
                    if(producto.getCantidad() >= 1) {
                        PedidosFactory.addLinea(mercado, producto);
                    }
                    else {
                        DialogsFactory.makeWarningDialog(
                                ResourceManager.getString(R.string.out_of_stock_title),
                                ResourceManager.getString(R.string.out_of_stock)
                        );
                    }
                    dialog.dismiss();
                });
            }
        });
    }

    public void update() {
        if(hasStarted) {
            RecyclerView recyclerView = (RecyclerView) viewRCWProductos;
            if (mode == ALL_PRODUCTS_FROM_ONE_MARKET) {
                ArrayList<Mercado> mercados = Consulta.getMercadosWhere(new Consulta<Mercado>() {
                    @Override
                    public boolean comprueba(Mercado o) {
                        return o.getUid().equals(mercado.getUid());
                    }
                });
                if(mercados.size() > 0) {
                    ((ProductsRecyclerAdapter) recyclerView.getAdapter())
                            .update(mercados.get(0).getProductos());
                }
            }
            else {
                ArrayList<Productos> productos = new ArrayList<>();
                for (Mercado mercado:GlobalInformation.MERCADOS) {
                    productos.addAll(mercado.getProductos());
                }

                ((ProductsRecyclerAdapter) recyclerView.getAdapter())
                        .update(productos);
            }
        }
    }
}