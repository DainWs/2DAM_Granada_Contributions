package com.josealex.granadacontributions.ui.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.MarketsRecyclerAdapter;
import com.josealex.granadacontributions.adapters.ModelsSpinnerAdapter;
import com.josealex.granadacontributions.adapters.UsersRecyclerAdapter;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.ui.home.HomeFragment;
import com.josealex.granadacontributions.ui.home.MercadoFragment;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.GlobalInformation;
import com.josealex.granadacontributions.utils.NavigationManager;
import com.josealex.granadacontributions.utils.ResourceManager;

import java.util.ArrayList;


public class PreferenceFragment extends Fragment {

    private boolean hasStarted = false;

    View root;
    User user;
    ArrayList<Mercado> listaGestion;
    Button addMoney;
    private RecyclerView userManagesRecyclerView;
    private MarketsRecyclerAdapter recyclerViewAdapter;
    View dialogViewAddMoney;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalInformation.preferences = this;
        hasStarted = true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_preferences, container, false);
        user = GlobalInformation.SIGN_IN_USER;

        userManagesRecyclerView = root.findViewById(R.id.gestionpreference);

        root.findViewById(R.id.market_addmoney).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HomeFragment.USER_BUNDLE_ID, user);

                NavigationManager.navigateTo(
                        R.id.action_nav_settings_to_userFragment,
                        bundle
                );
            }
        });
        ((TextView)root.findViewById(R.id.username_field)).setText(user.getNombre());
        ((TextView)root.findViewById(R.id.mail_field)).setText(user.getCorreo());

        Glide.with( getContext() )
             .load( user.getFotoURL() )
             .error(R.drawable.ic_launcher_foreground)
             .into(
                  (ImageView) root.findViewById(R.id.user_profile_image)
             );

        // Boton para aplicar a gestiones del mercado
        root.findViewById(R.id.preferences_apply_for_market_button)
            .setOnClickListener(new View.OnClickListener() {
                private Spinner spinnerField;
                private ModelsSpinnerAdapter adapter;
                private EditText passField;

                @Override
                public void onClick(View v) {
                    makeApplyDialog();
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
                            ArrayList<String> gestiones = user.getGestiona();
                            gestiones.add(market.getUid());
                            user.setGestiona(gestiones);
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

        // Boton para aplicar a gestiones del mercado
        root.findViewById(R.id.market_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(HomeFragment.USER_BUNDLE_ID, user);

                        NavigationManager.navigateTo(
                                R.id.action_from_settings_to_makeMarketFragment,
                                bundle
                        );
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
        recyclerViewAdapter = new MarketsRecyclerAdapter(listaGestion) {
            @Override
            public void onItemClick(Mercado mItem) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MercadoFragment.MARKET_TITLE_BUNDLE_ID, mItem.getNombre());
                bundle.putSerializable(MercadoFragment.MARKET_BUNDLE_ID, mItem);
                bundle.putSerializable(MercadoFragment.MARKET_USER_BUNDLE_ID, user);

                NavigationManager.navigateTo(
                        R.id.action_from_settings_to_mercadoFragment,
                        bundle
                );
            }
        };

        userManagesRecyclerView.setAdapter(recyclerViewAdapter);
    }

    public void update() {
        if(hasStarted) {
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
}