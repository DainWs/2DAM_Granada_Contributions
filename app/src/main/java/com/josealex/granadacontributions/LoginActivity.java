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
    Button google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        google = findViewById(R.id.bgoogle);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registroConectoGoogle();
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
            }

        }

    }

    public void toMainActivity(FirebaseAuth mAuth) {
        Intent intent = new Intent(this, MainActivity.class);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String eemail = currentUser.getEmail();
            String displayName = currentUser.getDisplayName();
            Uri photoUrl = currentUser.getPhotoUrl();
            String phoneNumber = currentUser.getPhoneNumber();
            String uid = currentUser.getUid();
            intent.putExtra("email", eemail);
            if (displayName == null) {
                String a[] = eemail.split("@");
                intent.putExtra("displayName", a[0]);
            } else {
                intent.putExtra("displayName", displayName);
            }
            if (photoUrl == null) {
                intent.putExtra("photoUrl", "https://aaahockey.org/wp-content/uploads/2017/06/default-avatar.png");
            } else {
                intent.putExtra("photoUrl", photoUrl.toString());
            }
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("uid", uid);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "SIN USUARIO", Toast.LENGTH_LONG);
        }
    }

}