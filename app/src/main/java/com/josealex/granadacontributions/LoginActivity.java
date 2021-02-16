package com.josealex.granadacontributions;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.josealex.granadacontributions.modules.User;

public class LoginActivity extends AppCompatActivity {

    private boolean hasSignIn;

    /**
     * LOS ID DE LOS CAMPOS DE LOS LAYOUT TENDRAN EL SIGUIENTE FORMATO:
     *          nombreactivity_identificador_tipo
     *  ejemplo:
     *          login_google_button
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void addListeners() {
        //TODO(agregar listener al boton de inicio de sesion con google)
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //TODO(COMPLETAR onActivityResult)
    }

    public void startMainActivity(User loggedUser) {
        clearForm();
        hasSignIn = true;
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.USER_BUNDLE_ID, loggedUser);
        startActivity(intent);
    }

    private void clearForm() {
        //TODO(LIMPIAR LOS CAMPOS)
    }
}