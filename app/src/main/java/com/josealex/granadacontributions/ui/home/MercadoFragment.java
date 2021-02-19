package com.josealex.granadacontributions.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.ProductsRecyclerAdapter;
import com.josealex.granadacontributions.adapters.UsersRecyclerAdapter;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.GlobalInformation;

import java.util.ArrayList;


public class MercadoFragment extends Fragment {

    public static final String MARKET_TITLE_BUNDLE_ID = "title";
    public static final String MARKET_USER_BUNDLE_ID = "user";
    public static final String MARKET_BUNDLE_ID = "market";

    private ArrayList<User> usersList;
    private RecyclerView usersListRecyclerView;
    private UsersRecyclerAdapter usersAdapter;

    private RecyclerView productsRecyclerView;
    private ProductsRecyclerAdapter adapter;

    private View root;
    private User user;
    private Mercado market;
    private User marketAdmin;

    private ViewGroup optionsLinearMenu;
    private Button deleteMarketButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_mercado, container, false);

        Bundle arguments = getArguments();
        user = (User) arguments.getSerializable(MARKET_USER_BUNDLE_ID);
        market = (Mercado) getArguments().getSerializable(MARKET_BUNDLE_ID);
        searchAdminUser();

        ((TextView)root.findViewById(R.id.market_uid_field))
            .setText(market.getUid());

        ((TextView)root.findViewById(R.id.market_name_field))
            .setText(market.getNombre());

        ((TextView) root.findViewById(R.id.market_admin_name_field))
                .setText(marketAdmin.getNombre());

        ((TextView)root.findViewById(R.id.market_password_field))
                .setText(market.getPassword());

        optionsLinearMenu = root.findViewById(R.id.options_linear_menu);
        deleteMarketButton = root.findViewById(R.id.market_manager_remove_button);


        System.out.println("USER " + market.getGestores().size());
        for (String s : market.getGestores()) {
            System.out.println(s);
        }

        usersList = Consulta.getUsersWhere(new Consulta<User>() {
            @Override
            public boolean comprueba(User o) {
                return (o.hasGestionesWhere(market.getUid()));
            }
        });

        System.out.println(usersList.size());

        usersAdapter = new UsersRecyclerAdapter(usersList);

        System.out.println(usersAdapter.getItemCount());

        usersListRecyclerView = root.findViewById(R.id.market_managers_recyclerview);
        usersListRecyclerView.setAdapter(usersAdapter);

        adapter = new ProductsRecyclerAdapter(market.getProductos());
        productsRecyclerView =
                (RecyclerView) root.findViewById(R.id.market_products_list);
        productsRecyclerView.setAdapter(adapter);

        updateOwner();

        return root;
    }

    private void searchAdminUser() {

        ArrayList<User> admin = Consulta.getUsersWhere(new Consulta<User>() {
            @Override
            public boolean comprueba(User o) {
                return market.getUidOwner().equals(o.getUid());
            }
        });

        if (admin.size() > 0) {
            marketAdmin = admin.get(0);
        }
        else {
            marketAdmin = new User("NONE", "NONE", "NONE", "");
        }
    }

    private void initExpandableValues(User model, View convertView) {
        convertView.findViewById(R.id.delete_user_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeDeleteManagerDialog(model);
                    }
                });

        ((TextView) convertView.findViewById(R.id.item_user_name))
                .setText(model.getNombre());

        ((TextView) convertView.findViewById(R.id.item_user_correo))
                .setText(model.getCorreo());

        Glide.with(getContext())
                .load( model.getFotoURL() )
                .error(R.drawable.ic_launcher_foreground)
                .circleCrop()
                .into(
                        (ImageView) convertView.findViewById(R.id.item_user_image)
                );
    }


    private void makeDeleteManagerDialog(User user) {
        new AlertDialog.Builder(GlobalInformation.mainActivity)
                .setTitle(R.string.warnning)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(
                        R.string.delete_button_text,
                        (dialogD, id) -> {
                            removeUser(user);
                            dialogD.dismiss();
                        })
                .setNegativeButton(
                        R.string.cancel_button_text,
                        (dialogD, id) -> dialogD.dismiss()
                ).create()
                .show();
    }

    private void removeUser(User user) {
        usersList.remove(user);
        usersAdapter.update(usersList);

        user.removeGestiones(market.getUid());
        market.removeGestores(user.getUid());

        FirebaseDBManager.saveUserData(user);
        FirebaseDBManager.saveMercado(market);
    }

    public void update(Mercado market) {

        if(market == null) return;

        if(this.market.getUid().equals(market.getUid())) {this.market = market;}

        updateOwner();
        updateList();
    }

    private void updateList() {
        adapter.update(this.market.getProductos());
        if(usersList.size() != this.market.getGestores().size()) {
            usersList = Consulta.getUsersWhere(new Consulta<User>() {
                @Override
                public boolean comprueba(User o) {
                    return (o.hasGestionesWhere(market.getUid()));
                }
            });

            usersAdapter.update(usersList);
        }
    }

    private void updateOwner() {
        if (user.getUid().equals(this.market.getUidOwner())) {
            if(optionsLinearMenu.findViewById(R.id.market_manager_remove_button) == null) {
                optionsLinearMenu.addView(deleteMarketButton);
                //TODO(FALTAN LOS GESTORES SI ES ADMIN)
            }
        }
        else optionsLinearMenu.removeView(deleteMarketButton);
        //TODO(FALTAN LOS GESTORES SI ES ADMIN)
    }
}