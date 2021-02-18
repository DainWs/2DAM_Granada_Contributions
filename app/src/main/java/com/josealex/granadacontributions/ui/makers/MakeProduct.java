package com.josealex.granadacontributions.ui.makers;

import android.app.AlertDialog;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.ModelsSpinnerAdapter;
import com.josealex.granadacontributions.adapters.ProductsRecyclerAdapter;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.utils.GlobalInformation;
import com.josealex.granadacontributions.utils.ResourceManager;

import java.util.ArrayList;


public class MakeProduct {

    private static final String[] CATEGORIAS = ResourceManager.getArray(R.array.categorias);

    private static EditText nombreField;
    private static Spinner categorySpinner;
    private static EditText precioField;
    private static EditText cantidadField;
    private static ModelsSpinnerAdapter adapter;

    public static void makeProductWithAdapter(ProductsRecyclerAdapter productsRecyclerAdapter) {
        View dialogView =
                GlobalInformation.mainActivity
                        .getLayoutInflater()
                        .inflate(R.layout.dialog_make_product, null);

        nombreField = dialogView.findViewById(R.id.editProductnombre);
        categorySpinner = dialogView.findViewById(R.id.editProductspinner);
        precioField = dialogView.findViewById(R.id.editProductprice);
        cantidadField = dialogView.findViewById(R.id.editProductCantidad);

        SpinnerAdapter simpleSpinnerAdapter = new ArrayAdapter<String>(
                GlobalInformation.mainActivity,
                android.R.layout.simple_spinner_item,
                CATEGORIAS
        );

        adapter = new ModelsSpinnerAdapter(
                simpleSpinnerAdapter,
                GlobalInformation.mainActivity,
                ""
        );

        categorySpinner.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(GlobalInformation.mainActivity)
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
                        Productos newProduct = userApplyToMarket();
                        if(newProduct != null) {
                            ArrayList<Productos> listProductos = productsRecyclerAdapter.getList();
                            listProductos.add(newProduct);
                            productsRecyclerAdapter.update(listProductos);
                            it.dismiss();
                        }
                    });
        });
        dialog.show();
    }

    private static Productos userApplyToMarket() {
        Productos productos = new Productos();

        String errorMessage = "";
        boolean isValid = true;

        String nombre = nombreField.getText().toString();
        String categoria = categorySpinner.getSelectedItem().toString();
        String precio = precioField.getText().toString();
        String cantidad = cantidadField.getText().toString();

        System.out.println(nombre + " , " + categoria + " , " + precio + " , " + cantidad);

        if(!nombre.isEmpty()) {
            productos.setNombre(nombre);
        }
        else {
            isValid = false;
            errorMessage += "a";
        }

        if(!categoria.isEmpty()) {
            productos.setCategoria(categoria);
        }
        else {
            isValid = false;
            errorMessage += "a";
        }

        if(!precio.isEmpty()) {
            try {
                productos.setPrecio(Integer.parseInt(precio));
            }
            catch (Exception ex) {
                isValid = false;
                errorMessage += "a";
            }
        }
        else {
            isValid = false;
            errorMessage += "a";
        }

        if(!cantidad.isEmpty()) {
            try {
                productos.setPrecio(Integer.parseInt(cantidad));
            }
            catch (Exception ex) {
                isValid = false;
                errorMessage += "a";
            }
        }
        else {
            isValid = false;
            errorMessage += "a";
        }


        if(isValid) {
            return productos;
        }
        else
            Toast.makeText(GlobalInformation.mainActivity, errorMessage, Toast.LENGTH_LONG).show();

        return null;
    }

}