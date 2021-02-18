package com.josealex.granadacontributions.ui.makers;

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
                    GlobalInformation.mainActivity.onBackPressed();
                }
                else
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();

            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeProduct.makeProductWithAdapter(adapter);
            }
        });


        return root;
    }
}