package com.josealex.granadacontributions.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.ProductsRecyclerAdapter;
import com.josealex.granadacontributions.adapters.UsersRecyclerAdapter;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.ui.makers.MakeProduct;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.DialogsFactory;
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
    private ProductsRecyclerAdapter productsAdapter;

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

        //ponemos el UID del mercado
        ((TextView)root.findViewById(R.id.market_uid_field))
            .setText(market.getUid());

        //ponemos el nombre del mercado
        ((TextView)root.findViewById(R.id.market_name_field))
            .setText(market.getNombre());

        //ponemos el nombre del admin del mercado
        ((TextView) root.findViewById(R.id.market_admin_name_field))
                .setText(marketAdmin.getNombre());

        //ponemos la password de acceso al mercado
        ((TextView)root.findViewById(R.id.market_password_field))
                .setText(market.getPassword());


        boolean isAdmin = (user.getUid().equals(this.market.getUidOwner())) ? true : false ;

        //Managers of market query
        usersList = Consulta.getUsersWhere(new Consulta<User>() {
            @Override
            public boolean comprueba(User o) {
                return (o.hasGestionesWhere(market.getUid()));
            }
        });

        //Managers of market adapter
        usersAdapter = new UsersRecyclerAdapter(usersList, isAdmin) {
            @Override
            public void onDeleteButtonClick(User mItem) {
                DialogsFactory.makeAreYouSureDialog(
                        (dialog, which) -> {
                            removeUser(user);
                            dialog.dismiss();
                        }
                );
            }
        };

        usersListRecyclerView = root.findViewById(R.id.market_managers_recyclerview);
        usersListRecyclerView.setAdapter(usersAdapter);

        productsAdapter = new ProductsRecyclerAdapter(market.getProductos());

        productsRecyclerView = root.findViewById(R.id.market_products_list);
        productsRecyclerView.setAdapter(productsAdapter);

        optionsLinearMenu = root.findViewById(R.id.options_linear_menu);
        deleteMarketButton = root.findViewById(R.id.market_manager_remove_button);

        //Add product button
        root.findViewById(R.id.add_products_buton).setOnClickListener(v -> {
            MakeProduct.makeProductWithAdapter(productsAdapter);
        });

        deleteMarketButton.setOnClickListener(v -> {
            DialogsFactory.makeAreYouSureDialog(
                    (dialog, which) -> {
                        //TODO(TAL VEZ SE DEBAN COMPROBAR LOS METODOS DE PAGO ANTES DE BORRAR)
                        Mercado.delete(market);
                        GlobalInformation.mainActivity.onBackPressed();
                    }
            );
        });

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

        if (admin.size() > 0) marketAdmin = admin.get(0);
        else marketAdmin = new User("NONE", "NONE", "NONE", "");
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
        productsAdapter.update(this.market.getProductos());
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