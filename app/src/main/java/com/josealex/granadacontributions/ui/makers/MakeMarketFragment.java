package com.josealex.granadacontributions.ui.makers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.ProductsRecyclerAdapter;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.ui.home.HomeFragment;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.GlobalInformation;
import com.josealex.granadacontributions.utils.ResourceManager;

import java.util.ArrayList;


public class MakeMarketFragment extends Fragment {

    private View root;
    private User user;

    private EditText nombreMercado;
    private EditText passwordMercado;
    private RecyclerView productList;
    private ProductsRecyclerAdapter adapter;

    private Button addProductBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_make_market, container, false);
        user = (User)getArguments().getSerializable(HomeFragment.USER_BUNDLE_ID);

        nombreMercado = root.findViewById(R.id.make_market_nombre_field);
        passwordMercado = root.findViewById(R.id.make_market_password_field);

        productList = root.findViewById(R.id.make_market_product_list);
        adapter = new ProductsRecyclerAdapter(new ArrayList<>());
        productList.setAdapter(adapter);

        addProductBtn = root.findViewById(R.id.make_market_add_product);

        root.findViewById(R.id.buttonCancelmercado).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalInformation.mainActivity.onBackPressed();
            }
        });

        root.findViewById(R.id.buacceptmercado).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mercado market = new Mercado();
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
                    errorMessage += ResourceManager.getString(R.string.password_market_error_message);;
                }

                market.addGestor(user.getUid());
                market.setProductos(adapter.getList());

                if(isValid) {
                    FirebaseDBManager.saveMercado(market);
                    user.addGestiones(market.getUid());
                    FirebaseDBManager.saveUserData(user);
                    GlobalInformation.mainActivity.onBackPressed();
                }
                else
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();

            }
        });

        root.findViewById(R.id.make_market_add_product).setOnClickListener(
                v -> MakeProduct.makeProductWithAdapter(adapter)
        );

        root.findViewById(R.id.make_market_remove_product).setOnClickListener(v -> {
            ArrayList<Productos> selectedProducts = new ArrayList<>();
            ArrayList<Productos> productos = adapter.getList();
            String[] list = new String[productos.size()];

            for (int i = 0; i < productos.size(); i++) {
                list[i] = productos.get(i).getNombre();
            }

            DialogInterface.OnMultiChoiceClickListener multiChoiceClickListener =
                    (dialog, which, isChecked) -> {
                        if (isChecked) {
                            selectedProducts.add(productos.get(which));
                        }
                        else if (selectedProducts.contains(which)) {
                            selectedProducts.remove(which);
                        }
                    };

            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.remove_products)
                    .setMultiChoiceItems(list, new boolean[list.length], multiChoiceClickListener)
                    .setPositiveButton(R.string.apply_button_text, (dialog, id) -> {
                        productos.removeAll(selectedProducts);
                        adapter.update(productos);
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.cancel_button_text, (dialog, id) -> {
                        dialog.dismiss();
                    })
                    .create()
                    .show();
        });

        return root;
    }
}