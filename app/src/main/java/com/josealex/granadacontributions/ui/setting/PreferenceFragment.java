package com.josealex.granadacontributions.ui.setting;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.ui.rview.MymercadosRecyclerViewAdapter;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.GlobalInformation;

import java.util.ArrayList;


public class PreferenceFragment extends Fragment {


    View root;
    ImageView foto;
    TextView name, mail;
    ImageButton addgestion;
    User user;
    private Dialog dialog;
    ArrayList<Mercado> listaGestion;
    private RecyclerView viewRCWGestion;
    private MymercadosRecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_preferences, container, false);
        user = GlobalInformation.SIGN_IN_USER;
        viewRCWGestion = root.findViewById(R.id.gestionpreference);
        foto = root.findViewById(R.id.imageViewphotochat);
        name = root.findViewById(R.id.username_field);
        mail = root.findViewById(R.id.mail_field);
        Object imageURL;
        imageURL = user.getFotoURL();
        Glide.with(getContext()).load(imageURL).into(foto);
        name.setText(user.getNombre());
        mail.setText(user.getCorreo());
        addgestion = root.findViewById(R.id.edit_phone_button);
        dialog = new Dialog(getContext());
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addgestion);
        EditText editTextNombrexp = dialog.findViewById(R.id.passtext);
        Button aceptar = dialog.findViewById(R.id.buaccept);
        Button cancel = dialog.findViewById(R.id.buttonCancel);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO (añadir mercado uid al usuario)
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        addgestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();

            }
        });
        cargarGestion();
        return root;
    }

    public void cargarGestion() {
        viewRCWGestion.setLayoutManager(new LinearLayoutManager(getContext()));
        listaGestion = Consulta.getMercadosWhere(new Consulta<Mercado>() {
            @Override
            public boolean comprueba(Mercado o) {

                for (String uid : user.getGestiona()) {
                    if (uid.equals(o.getUid())) {
                        return true;
                    }
                }
                return false;
            }
        });
        recyclerViewAdapter = new MymercadosRecyclerViewAdapter(
                listaGestion,
                getContext(),
                getActivity()
        );

        viewRCWGestion.setAdapter(recyclerViewAdapter);
    }

    public void update() {
        listaGestion = Consulta.getMercadosWhere(new Consulta<Mercado>() {
            @Override
            public boolean comprueba(Mercado o) {

                for (String uid : user.getGestiona()) {
                    if (uid.equals(o.getUid())) {
                        return true;
                    }
                }
                return false;
            }
        });
        recyclerViewAdapter.update(listaGestion);
    }
}