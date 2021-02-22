package com.josealex.granadacontributions.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.ProductsRecyclerAdapter;
import com.josealex.granadacontributions.adapters.UsersRecyclerAdapter;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.ui.makers.MakeProduct;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.DialogsFactory;
import com.josealex.granadacontributions.utils.GlobalInformation;
import com.josealex.granadacontributions.utils.NavigationManager;
import com.josealex.granadacontributions.utils.ResourceManager;

import java.lang.reflect.Field;
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
    private Button pendingOrdersMarketButton;
    private ImageButton show_pass;
    private boolean generalExpanded = true;
    private View generalContent;
    private int oldHeightOfGenerals;
    private TextView showpasstext;
    private boolean showing;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_mercado, container, false);

        Bundle arguments = getArguments();
        user = (User) arguments.getSerializable(MARKET_USER_BUNDLE_ID);
        market = (Mercado) getArguments().getSerializable(MARKET_BUNDLE_ID);
        searchAdminUser();

        System.out.println(market.toDetailsString());

        //ponemos el UID del mercado
        ((TextView)root.findViewById(R.id.market_uid_field))
            .setText(market.getUid());

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
            public void onButtonClick(User mItem) {
                DialogsFactory.makeAreYouSureDialog(
                        (dialog, which) -> {
                            removeUser(mItem);
                            dialog.dismiss();
                        }
                );
            }
        };

        usersListRecyclerView = root.findViewById(R.id.market_managers_recyclerview);
        usersListRecyclerView.setAdapter(usersAdapter);

        productsAdapter = new ProductsRecyclerAdapter(market.getProductos(), true) {
            @Override
            public void onViewClick(View v, Productos producto, int position) {
                PopupMenu popup = new PopupMenu(getContext(), v);

                popup.setOnMenuItemClickListener(
                        (PopupMenu.OnMenuItemClickListener) item ->
                    {
                        switch (item.getItemId()) {
                            case R.id.popup_edit:
                                MakeProduct.editProductWithAdapter(producto, dialog -> {
                                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                                        Productos editedProducto = MakeProduct.userApplyToMarket();
                                        ArrayList<Productos> productosList = productsAdapter.getList();
                                        productosList.set(position, editedProducto);
                                        productsAdapter.update(productosList);

                                        market.setProductos(productosList);
                                        FirebaseDBManager.saveMercado(market);

                                        dialog.dismiss();
                                    });
                                });
                                return true;
                            case R.id.popup_remove:
                                DialogsFactory.makeAreYouSureDialog((dialog, which) -> {
                                    ArrayList<Productos> productos = productsAdapter.getList();
                                    productos.remove(producto);
                                    productsAdapter.update(productos);

                                    market.setProductos(productos);
                                    FirebaseDBManager.saveMercado(market);

                                    dialog.dismiss();
                                });
                                return true;
                        }
                        return false;
                    }
                );

                popup.inflate(R.menu.user_popup_menu);
                try {
                    Field mFieldPopup=popup.getClass().getDeclaredField("mPopup");
                    mFieldPopup.setAccessible(true);
                    MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popup);
                }
                catch (Exception e) {}
                popup.show();
            }
        };

        productsRecyclerView = root.findViewById(R.id.market_products_list);
        productsRecyclerView.setAdapter(productsAdapter);

        optionsLinearMenu = root.findViewById(R.id.options_linear_menu);
        deleteMarketButton = root.findViewById(R.id.market_manager_remove_button);
        pendingOrdersMarketButton = root.findViewById(R.id.market_order_button);

        //Add product button
        root.findViewById(R.id.add_products_buton).setOnClickListener(v -> {
            MakeProduct.makeProductWithAdapter(productsAdapter, market);
        });

        deleteMarketButton.setOnClickListener(v -> {
            DialogsFactory.makeAreYouSureDialog(
                    (dialog, which) -> {
                        if (market.getPedidos().size() <= 0) {
                            Mercado.delete(market);
                            GlobalInformation.mainActivity.onBackPressed();
                        }
                        else {
                            DialogsFactory.makeWarningDialog(
                                    ResourceManager.getString(R.string.you_cant_delete_market_if)
                            );
                        }
                    }
            );
        });

        pendingOrdersMarketButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            String title = ResourceManager.getString(R.string.orders_of) + market.getNombre();
            bundle.putString(ListPedidosFragment.PEDIDO_TITLE_BUNDLE_ID, title);
            bundle.putSerializable(ListPedidosFragment.PEDIDO_MARKET_BUNDLE_ID, market);
            NavigationManager.navigateTo(R.id.action_from_mercado_to_listPedidos, bundle);
        });

        generalContent = root.findViewById(R.id.general_content);

        root.findViewById(R.id.general_title).setOnClickListener(v -> {
            ViewGroup.LayoutParams layoutParams = generalContent.getLayoutParams();
            if(generalExpanded) {
                oldHeightOfGenerals = layoutParams.height;
                layoutParams.height = 0;
                generalContent.setLayoutParams(layoutParams);
                generalContent.setEnabled(false);
            }
            else {
                layoutParams.height = oldHeightOfGenerals;
                generalContent.setLayoutParams(layoutParams);
                generalContent.setEnabled(true);
            }
            generalExpanded = !generalExpanded;
        });

        updateOwner();
        show_pass = root.findViewById(R.id.show_password_button);
        showpasstext = root.findViewById(R.id.market_password_field);

        show_pass.setOnClickListener(v -> {
            if(showing){
                showpasstext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                showpasstext.setTransformationMethod( PasswordTransformationMethod.getInstance());
            }
            showing = !showing;

        });

        showing = false;

        GlobalInformation.mercadoFragment = this;
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

        if(market == null || this.market==null) return;

        if(this.market.getUid().equals(market.getUid())) {this.market = market;}

        update();
    }

    public void update() {
        if(root!=null) {
            //ponemos el UID del mercado
            ((TextView) root.findViewById(R.id.market_uid_field))
                    .setText(market.getUid());

            //ponemos el nombre del admin del mercado
            ((TextView) root.findViewById(R.id.market_admin_name_field))
                    .setText(marketAdmin.getNombre());

            //ponemos la password de acceso al mercado
            ((TextView) root.findViewById(R.id.market_password_field))
                    .setText(market.getPassword());

            updateOwner();
            updateList();
        }
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

            }
        }
        else optionsLinearMenu.removeView(deleteMarketButton);

    }
}

