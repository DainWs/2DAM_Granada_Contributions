package com.josealex.granadacontributions.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.MarketsRecyclerAdapter;
import com.josealex.granadacontributions.adapters.ModelsSpinnerAdapter;
import com.josealex.granadacontributions.adapters.ProductsRecyclerAdapter;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.GlobalInformation;
import com.josealex.granadacontributions.utils.NavigationManager;
import com.josealex.granadacontributions.utils.ResourceManager;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public static final String USER_BUNDLE_ID = "User";

    private boolean hasStarted = false;

    private View root;

    private FirebaseDBManager dbManager;
    private User loggedUser;

    private Consulta<Mercado> mercadosDelUsuario = new Consulta<Mercado>() {
        @Override
        public boolean comprueba(Mercado o) {
            Consulta<String> esMiGestor = new Consulta<String>() {
                @Override
                public boolean comprueba(String o) {
                    return (o.equals(loggedUser.getUid()));
                }
            };

            return (o.getGestoresWhere(esMiGestor).size() > 0);
        }
    };

    private ViewGroup filterMenuLinearlayout;

    private RecyclerView viewRCWMercados;
    private MarketsRecyclerAdapter recyclerViewMarketsAdapter;
    private ProductsRecyclerAdapter recyclerViewProductsAdapter;
    private ArrayList<Mercado> mercadosList;
    private ArrayList<Productos> productosList;

    private Spinner mercadosSpinner;
    private Spinner categoriasSpinner;

    private SpinnerAdapter mercadosSpinnerAdapter;
    private SpinnerAdapter categoriasSpinnerAdapter;
    private ArrayList listaproducto;
    private Button addMercadoBtn;
    private Button filterBtn;
    private Button exploreBtn;

    private boolean inMode = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalInformation.home.update();
        GlobalInformation.home = this;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        mercadosSpinner = root.findViewById(R.id.mercado_spinner);
        categoriasSpinner = root.findViewById(R.id.categorias_spinner);

        addMercadoBtn = root.findViewById(R.id.add_mercado_btn);
        filterBtn = root.findViewById(R.id.filter_button);
        exploreBtn = root.findViewById(R.id.explore_btn);

        viewRCWMercados = root.findViewById(R.id.rcw);

        filterMenuLinearlayout = root.findViewById(R.id.filter_menu_linearlayout);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeFilter();
            }
        });
        Bundle b = getActivity().getIntent().getExtras();

        if (b != null) {
            loggedUser = (User) b.getSerializable(USER_BUNDLE_ID);
        }

        startHome();

        viewRCWMercados.setLayoutManager(new LinearLayoutManager(getContext()));
        cargarMercado();

        cargarSpinner();
        hasStarted = true;
        return root;
    }

    private void startHome() {
        mercadosList = GlobalInformation.MERCADOS;

        productosList = new ArrayList<>();
        for (Mercado mercado : GlobalInformation.MERCADOS) {
            productosList.addAll(mercado.getProductos());
        }

        addMercadoBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(HomeFragment.USER_BUNDLE_ID, loggedUser);

            NavigationManager.navigateTo(
                    R.id.action_from_home_to_makeMarketFragment,
                    bundle
            );
        });

        filterBtn.setOnClickListener(v -> makeFilter());
        exploreBtn.setOnClickListener(v ->
        {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ProductosListFragment.PRODUCTS_LIST_USER_BUNDLE_ID, loggedUser);
            bundle.putSerializable(ProductosListFragment.PRODUCTS_LIST_TITLE_BUNDLE_ID, ResourceManager.getString(R.string.products));
            if (mercadosSpinner.getSelectedItem()==null) {
                mercadosSpinner.setSelection(1);
            }
            bundle.putSerializable(ProductosListFragment.PRODUCTS_LIST_MARKET_BUNDLE_ID, (Mercado) mercadosSpinner.getSelectedItem());
            bundle.putSerializable(ProductosListFragment.PRODUCTS_LIST_MODE_BUNDLE_ID, ProductosListFragment.ALL_PRODUCTS_FROM_ONE_MARKET);

            NavigationManager.navigateTo(
                    R.id.action_from_home_to_productosFragment,
                    bundle
            );
        });

        filterMenuLinearlayout.removeView(
                filterMenuLinearlayout.findViewById(R.id.add_mercado_btn)
        );

        inMode = GlobalInformation.ON_MANAGER_MODE;
        updateMode();
    }

    public void cargarMercado() {
        recyclerViewMarketsAdapter = new MarketsRecyclerAdapter(mercadosList) {
            @Override
            public void onItemClick(Mercado mItem) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MercadoFragment.MARKET_TITLE_BUNDLE_ID, mItem.getNombre());
                bundle.putSerializable(MercadoFragment.MARKET_BUNDLE_ID, mItem);
                bundle.putSerializable(MercadoFragment.MARKET_USER_BUNDLE_ID, loggedUser);

                NavigationManager.navigateTo(
                        R.id.action_from_home_to_mercadoFragment,
                        bundle
                );
            }
        };

        productosList = new ArrayList<>();
        for (Mercado mercado : GlobalInformation.MERCADOS) {
            productosList.addAll(mercado.getProductos());
        }
        recyclerViewProductsAdapter = new ProductsRecyclerAdapter(productosList);

        if (inMode) viewRCWMercados.setAdapter(recyclerViewMarketsAdapter);
        else viewRCWMercados.setAdapter(recyclerViewProductsAdapter);

    }

    public void cargarSpinner() {


        SpinnerAdapter simpleSpinnerAdapter = new ArrayAdapter<Mercado>(
                getContext(),
                android.R.layout.simple_spinner_item,
                GlobalInformation.MERCADOS);

        mercadosSpinnerAdapter = new ModelsSpinnerAdapter(
                simpleSpinnerAdapter,
                getContext(),
                ResourceManager.getString(R.string.spinner_select_market)
        );


        SpinnerAdapter simpleSpinnerAdapter2 = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_spinner_item,
                ResourceManager.getArray(R.array.categorias));

        categoriasSpinnerAdapter = new ModelsSpinnerAdapter(
                simpleSpinnerAdapter2,
                getContext(),
                ""
        );

        mercadosSpinner.setAdapter(mercadosSpinnerAdapter);
        categoriasSpinner.setAdapter(categoriasSpinnerAdapter);
    }

    public void update() {
        if (hasStarted) {
            if (inMode) {
                mercadosList = Consulta.getMercadosWhere(mercadosDelUsuario);
                recyclerViewMarketsAdapter.update(mercadosList);
                viewRCWMercados.setAdapter(recyclerViewMarketsAdapter);

            } else {
                productosList = new ArrayList<>();
                for (Mercado mercado : GlobalInformation.MERCADOS) {
                    productosList.addAll(mercado.getProductos());
                }
                recyclerViewProductsAdapter.update(productosList);
                viewRCWMercados.setAdapter(recyclerViewProductsAdapter);
            }
            cargarSpinner();
        }

    }

    public void makeFilter() {
        Mercado markselect = (Mercado) mercadosSpinner.getSelectedItem();
        String catselect = categoriasSpinner.getSelectedItem().toString();
        mercadosList = Consulta.getMercadosWhere(new Consulta<Mercado>() {
            @Override
            public boolean comprueba(Mercado o) {
                return o.getUid().equals(markselect.getUid());
            }

        });

        if (mercadosList.size() > 0) {
            listaproducto = mercadosList.get(0).getProductosWhere(new Consulta<Productos>() {
                @Override
                public boolean comprueba(Productos o) {
                    return o.getCategoria().equals(catselect);
                }
            });
        }
        recyclerViewProductsAdapter.update(listaproducto);
        viewRCWMercados.setAdapter(recyclerViewProductsAdapter);
    }

    // si el Switch Button esta ON es true
    // si el Switch Button esta OFF es false
    public void changeMode(boolean checked) {
        if (inMode != checked) {
            inMode = checked;

            update();
            updateMode();
        }
    }

    private void updateMode() {
        if (inMode) {
            filterMenuLinearlayout.addView(addMercadoBtn, 0);

            //TODO(FALTAN COSAS POR ACTUALIZAR)
        } else {
            filterMenuLinearlayout.removeView(
                    filterMenuLinearlayout.findViewById(R.id.add_mercado_btn)
            );

            Consulta.getMercadosWhere(new Consulta<Mercado>() {
                @Override
                public boolean comprueba(Mercado o) {
                    return false;
                }
            });

            //TODO(FALTAN COSAS POR ACTUALIZAR)
        }
    }
}