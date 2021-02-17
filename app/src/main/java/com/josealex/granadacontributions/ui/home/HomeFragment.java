package com.josealex.granadacontributions.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.ui.rview.MymercadosRecyclerViewAdapter;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.GlobalInformation;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public static final String USER_BUNDLE_ID = "User";
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
    private MymercadosRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Mercado> mercadosList;

    private Spinner mercadosSpinner;
    private Spinner categoriasSpinner;

    private SpinnerAdapter mercadosSpinnerAdapter;
    private SpinnerAdapter categoriasSpinnerAdapter;

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

        Bundle b = getActivity().getIntent().getExtras();

        if(b != null) {
            loggedUser = (User) b.getSerializable(USER_BUNDLE_ID);
        }

        startHome();

        cargarMercado();
        return root;
    }

    private void startHome() {
        mercadosList = GlobalInformation.MERCADOS;

        addMercadoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        exploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarMercado();
            }
        });

        filterMenuLinearlayout.removeView(
                filterMenuLinearlayout.findViewById(R.id.add_mercado_btn)
        );

        updateMode(false);
    }

    public void cargarMercado(){
        viewRCWMercados.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAdapter = new MymercadosRecyclerViewAdapter(
                mercadosList,
                getContext(),
                getActivity()
        );

        viewRCWMercados.setAdapter(recyclerViewAdapter);
    }

    public void update() {
        if(recyclerViewAdapter != null) {
            if (inMode) {
                mercadosList = Consulta.getMercadosWhere(mercadosDelUsuario);
                recyclerViewAdapter.update(mercadosList);
            } else {
                recyclerViewAdapter.update(GlobalInformation.MERCADOS);
            }
        }
    }

    // si el Switch Button esta ON es true
    // si el Switch Button esta OFF es false
    public void updateMode(boolean checked) {
        if(inMode != checked) {
            inMode = checked;

            update();

            if (inMode) {
                filterMenuLinearlayout.addView(addMercadoBtn, 0);

                //TODO(FALTAN COSAS POR ACTUALIZAR)
            }
            else {
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
}