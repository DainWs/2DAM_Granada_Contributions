package com.josealex.granadacontributions.ui.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.MarketsRecyclerAdapter;
import com.josealex.granadacontributions.adapters.ModelsSpinnerAdapter;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.GlobalInformation;
import com.josealex.granadacontributions.utils.ResourceManager;

import java.util.ArrayList;


public class PreferenceFragment extends Fragment {


    View root;
    ImageButton addgestion;
    User user;
    private Dialog dialog;
    ArrayList<Mercado> listaGestion;
    private RecyclerView userManagesRecyclerView;
    private MarketsRecyclerAdapter recyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_preferences, container, false);
        user = GlobalInformation.SIGN_IN_USER;

        userManagesRecyclerView = root.findViewById(R.id.gestionpreference);

        ((TextView)root.findViewById(R.id.username_field)).setText(user.getNombre());
        ((TextView)root.findViewById(R.id.mail_field)).setText(user.getCorreo());

        Glide.with( getContext() )
             .load( user.getFotoURL() )
             .into(
                  (ImageView) root.findViewById(R.id.user_profile_image)
             );

        addgestion = root.findViewById(R.id.edit_phone_button);

        // Boton para aplicar a gestiones del mercado
        root.findViewById(R.id.edit_phone_button)
            .setOnClickListener(new View.OnClickListener() {
                private Spinner spinnerField;
                private ModelsSpinnerAdapter adapter;
                private EditText passField;

                @Override
                public void onClick(View v) {
                    makeApplyDialog();
                    dialog.show();
                }

                private void makeApplyDialog() {
                    View dialogView =
                            inflater.inflate(R.layout.dialog_join_to_management, null);

                    spinnerField = dialogView.findViewById(R.id.market_selector_spinner);
                    passField = dialogView.findViewById(R.id.market_apply_password_field);

                    Context context = PreferenceFragment.this.getContext();

                    SpinnerAdapter simpleSpinnerAdapter = new ArrayAdapter<Mercado>(
                            context,
                            android.R.layout.simple_spinner_item,
                            GlobalInformation.MERCADOS);

                    adapter = new ModelsSpinnerAdapter(
                            simpleSpinnerAdapter,
                            context,
                            ResourceManager.getString(R.string.spinner_select_market)
                    );

                    spinnerField.setAdapter(adapter);

                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setView(dialogView)
                         // poniendo el listener positivo a nulo e iniciandolo mas tarde,
                         // nos aseguramos de que da igual cuanto pulse el boton,
                         // ni nosotros no queremos, el dialog no se cerrara
                        .setPositiveButton(R.string.apply_button_text, null)
                        .setNegativeButton(
                                R.string.cancel_button_text,
                                (dialog12, id) -> dialog12.dismiss()
                        ).create();

                    //Aqui agregaremos el listener del positiveButton
                    dialog.setOnShowListener(it -> {
                        ((AlertDialog)it).getButton(AlertDialog.BUTTON_POSITIVE)
                            .setOnClickListener(view -> {
                                if(userApplyToMarket()) it.dismiss();
                            });
                    });
                    dialog.show();
                }

                public boolean userApplyToMarket() {
                    String password = passField.getText().toString();
                    if(!password.isEmpty()) {
                        Mercado market = (Mercado) spinnerField.getSelectedItem();
                        String marketPassword = market.getPassword();

                        if(password.equals(marketPassword)) {
                            user.addGestiones(market.getUid());
                            market.addGestor(user.getUid());

                            FirebaseDBManager.saveUserData(user);
                            FirebaseDBManager.saveMercado(market);
                            return true;
                        }
                        else {
                            Toast.makeText(
                                    PreferenceFragment.this.getContext(),
                                    R.string.incorrect_market_password,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                    else {
                        Toast.makeText(
                                PreferenceFragment.this.getContext(),
                                R.string.password_is_empty,
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    return false;
                }
            });
        cargarGestion();
        return root;
    }

    public void cargarGestion() {
        userManagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        recyclerViewAdapter = new MarketsRecyclerAdapter(listaGestion);

        userManagesRecyclerView.setAdapter(recyclerViewAdapter);
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