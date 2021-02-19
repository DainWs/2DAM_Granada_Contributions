package com.josealex.granadacontributions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.josealex.granadacontributions.modules.User;

public class LoginActivity extends AppCompatActivity {


    private static int GOOGLEIN = 100;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    Button google,registro,conect;
    EditText email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        google = findViewById(R.id.bgoogle);
        registro = findViewById(R.id.loginReg);
        conect = findViewById(R.id.loginCon);
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registroConectoGoogle();
            }
        });
        conect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    conecto(email.getText().toString(), password.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Conexion fallida", Toast.LENGTH_SHORT).show();
                }
            }
        });
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createusuerbasic(email.getText().toString(), password.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Registro fallido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createusuerbasic(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                            user = mAuth.getCurrentUser();
                            Toast.makeText(getBaseContext(), "Registro valido", Toast.LENGTH_SHORT).show();
                            toMainActivity(mAuth);


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(getBaseContext(), "Registro fallido", Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }
    public void conecto(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            user = mAuth.getCurrentUser();
                            Toast.makeText(getBaseContext(), "conecto", Toast.LENGTH_SHORT).show();
                            toMainActivity(mAuth);
                        } else {
                            Toast.makeText(getBaseContext(), "Registro fallido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void registroConectoGoogle() {

        GoogleSignInOptions googleConf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleClient = GoogleSignIn.getClient(getBaseContext(), googleConf);
        googleClient.signOut();
        startActivityForResult(googleClient.getSignInIntent(), GOOGLEIN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLEIN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //GoogleSignInAccount cuenta= signedInAccountFromIntent.getResult(ApiException.class);
                GoogleSignInAccount cuenta = task.getResult(ApiException.class);
                //AUTENTICARSE CON GOOGLE
                if (cuenta != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(cuenta.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getBaseContext(), "CONECTADO", Toast.LENGTH_SHORT).show();

                                toMainActivity(mAuth);
                            } else {
                                Toast.makeText(getBaseContext(), "NO CONECTO", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }

        }

    }

    public void toMainActivity(FirebaseAuth mAuth) {
        Intent intent = new Intent(this, MainActivity.class);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            String mail = currentUser.getEmail();
            String displayName = currentUser.getDisplayName();
            Uri photoUrl = currentUser.getPhotoUrl();
            String uid = currentUser.getUid();

            //(String uid, String nombre, String apellido, String correo, String fotoURL
            User user =  new User(uid, displayName, mail, photoUrl.toString());
            intent.putExtra(MainActivity.USER_BUNDLE_ID, user);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "SIN USUARIO", Toast.LENGTH_LONG);
        }
    }

}