package com.josealex.granadacontributions.ui.makers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.ProductsRecyclerAdapter;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.ui.home.HomeFragment;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.DialogsFactory;
import com.josealex.granadacontributions.utils.GlobalInformation;
import com.josealex.granadacontributions.utils.ResourceManager;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class MakeMarketFragment extends Fragment {

    private View root;
    private User user;

    private Mercado market;

    private EditText nombreMercado;
    private EditText passwordMercado;
    private RecyclerView productList;
    private ProductsRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_make_market, container, false);

        market = new Mercado();
        user = (User)getArguments().getSerializable(HomeFragment.USER_BUNDLE_ID);

        nombreMercado = root.findViewById(R.id.make_market_nombre_field);
        passwordMercado = root.findViewById(R.id.make_market_password_field);

        productList = root.findViewById(R.id.make_market_product_list);
        adapter = new ProductsRecyclerAdapter(new ArrayList<>()) {
            @Override
            public void onViewClick(View v, Productos producto, int position) {
                PopupMenu popup = new PopupMenu(getContext(), v);

                popup.setOnMenuItemClickListener(
                        item ->
                        {
                            switch (item.getItemId()) {
                                case R.id.popup_edit:
                                    MakeProduct.editProductWithAdapter(producto, dialog -> {

                                        ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                                            Productos editedProducto = MakeProduct.userApplyToMarket();

                                            ArrayList<Productos> productosList = adapter.getList();
                                            productosList.set(position, editedProducto);
                                            adapter.update(productosList);

                                            dialog.dismiss();
                                        });
                                    });
                                    return true;
                                case R.id.popup_remove:
                                    DialogsFactory.makeAreYouSureDialog((dialog, which) -> {
                                        ArrayList<Productos> productos = adapter.getList();
                                        productos.remove(producto);
                                        adapter.update(productos);

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
        productList.setAdapter(adapter);

        root.findViewById(R.id.buttonCancelmercado).setOnClickListener(
                v -> GlobalInformation.mainActivity.onBackPressed()
        );

        root.findViewById(R.id.buacceptmercado).setOnClickListener(v -> {
            market.setUidOwner(user.getUid());

            String nombre = nombreMercado.getText().toString();
            String password = passwordMercado.getText().toString();

            String errorMessage = "";
            boolean isValid = true;

            if(!nombre.isEmpty()) {
                ArrayList thatOnes = Consulta.getMercadosWhere(new Consulta<Mercado>() {
                    @Override
                    public boolean comprueba(Mercado o) {
                        return (o.getNombre().equalsIgnoreCase(nombre));
                    }
                });

                if(thatOnes.size() <= 0) market.setNombre(nombre);
                else {
                    isValid = false;
                    errorMessage += ResourceManager.getString(R.string.that_name_already_exist);
                }
            }
            else {
                isValid = false;
                errorMessage += ResourceManager.getString(R.string.name_market_error_message);
            }

            if(!password.isEmpty()) market.setPassword(password);
            else {
                isValid = false;
                errorMessage += ResourceManager.getString(R.string.password_market_error_message);
            }

            market.addGestor(user.getUid());
            market.setProductos(adapter.getList());

            if(isValid) {
                FirebaseDBManager.saveMercado(market);
                ArrayList<String> gestiones = user.getGestiona();
                gestiones.add(market.getUid());
                user.setGestiona(gestiones);
                FirebaseDBManager.saveUserData(user);
                GlobalInformation.mainActivity.onBackPressed();
            }
            else
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();

        });

        root.findViewById(R.id.make_market_add_product).setOnClickListener(
                v -> MakeProduct.makeProductWithAdapter(adapter)
        );

        return root;
    }
}