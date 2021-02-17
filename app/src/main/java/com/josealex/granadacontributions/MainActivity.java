package com.josealex.granadacontributions;

import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.utils.GlobalInformation;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String USER_BUNDLE_ID = "User";


    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseDBManager dbManager;
    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        if(b != null) {
            loggedUser = (User) b.getSerializable(USER_BUNDLE_ID);
        }
        
        setContentView(R.layout.activity_main);
        GlobalInformation.mainActivity = this;
        dbManager = new FirebaseDBManager(this, loggedUser);
        FirebaseDBManager.createUserData(this,loggedUser);
        User user = new User("123", "123", "123", "");
        FirebaseDBManager.createUserData(this, user);
        user = new User("1234", "1234", "1234", "");
        FirebaseDBManager.createUserData(this, user);
        user = new User("1235", "1235", "1235", "");
        FirebaseDBManager.createUserData(this, user);
        user = new User("1236", "1236", "1236", "");
        FirebaseDBManager.createUserData(this, user);

        Mercado mercado = new Mercado();
        mercado.setUid("1235");
        mercado.setNombre("Otro mercado");
        Mercado mercado2 = new Mercado();
        mercado.setUid("1235");
        mercado.setNombre("Otro mercado2");

        ArrayList<String> a = new ArrayList<String>();
        a.add("1234");
        a.add("1235");

        mercado.setGestores(a);
        mercado2.setGestores(a);
        ArrayList<Productos> c = new ArrayList<Productos>();
        c.add(new Productos("4", "4", "4", 4));
        c.add(new Productos("5", "5", "5", 5));

        mercado.setProductos(c);
        mercado2.setProductos(c);


        FirebaseDBManager.saveMercado(mercado);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void update() {

        for (User user: GlobalInformation.USERS) {
            System.out.println("------------------<Correo "+user.getCorreo());
        }

        for (Mercado m : GlobalInformation.MERCADOS) {
            System.out.println("------------------Mercado "+m.getNombre());
            for (Productos p : m.getProductos()) {
                System.out.println("---------producto "+p.getNombre());
            }
        }
    }
}