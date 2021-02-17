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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.ui.rview.MymercadosRecyclerViewAdapter;
import com.josealex.granadacontributions.utils.GlobalInformation;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public static final String USER_BUNDLE_ID = "User";
    private View root;
    private View viewrcwmercados;

    private FirebaseDBManager dbManager;
    private  ArrayList<Mercado> mercadoslista;
    private User loggedUser;
    private Button button;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalInformation.home.update();
        GlobalInformation.home = this;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        viewrcwmercados = root.findViewById(R.id.rcw);

        Bundle b = getActivity().getIntent().getExtras();

        button = root.findViewById(R.id.button);
        if(b != null) {
            loggedUser = (User) b.getSerializable(USER_BUNDLE_ID);
        }

        mercadoslista = GlobalInformation.MERCADOS;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargorcwMercado();
            }
        });

        CargorcwMercado();
        return root;
    }
    public void CargorcwMercado(){
        RecyclerView recyclerView = (RecyclerView) viewrcwmercados;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(new MymercadosRecyclerViewAdapter(mercadoslista,getContext(),getActivity()));
    }
    public void update() {

        for (User user: GlobalInformation.USERS) {
            System.out.println("------------------<Correo "+user.getCorreo());
        }

        for (Mercado m : GlobalInformation.MERCADOS) {
            System.out.println("------------------Mercado "+m.getNombre());
            for (Productos p : m.getProductos()) {
                System.out.println("---------producto "+p.getNombre());
            }
        }
    }
}