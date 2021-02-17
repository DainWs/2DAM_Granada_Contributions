package com.josealex.granadacontributions;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.utils.GlobalInformation;

import androidx.annotation.NonNull;
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

    private MenuItem switchModeItem;
    private boolean onGestorMode = false;

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

        loggedUser.addGestiones("1234");
        FirebaseDBManager.createUserData(this, loggedUser);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar.getMenu()
        //        .add(Menu.NONE, R.id.mode_user_switch, Menu.FIRST, R.string.change_user_mode);

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
        switchModeItem = menu.findItem(R.id.mode_user_switch);
        Switch switchView = ((Switch) switchModeItem.getActionView().findViewById(R.id.switch_view));

        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != onGestorMode) {
                    onGestorMode = isChecked;
                    GlobalInformation.home.updateMode(isChecked);
                }
            }
        });

        if(loggedUser.getGestiona().size() <= 0) {
            menu.removeItem(R.id.mode_user_switch);
        }

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_settings:
                Intent intent = new Intent(this, PreferencesActivity.class);
                intent.putExtra(USER_BUNDLE_ID, loggedUser);
                startActivity(intent);
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}