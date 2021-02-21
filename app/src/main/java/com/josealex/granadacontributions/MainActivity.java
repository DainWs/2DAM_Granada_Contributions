package com.josealex.granadacontributions;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.utils.GlobalInformation;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public static final String USER_BUNDLE_ID = "User";

    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseDBManager dbManager;
    private User loggedUser;

    private MenuItem switchModeItem;
    private Menu menu;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar.getMenu()
        //        .add(Menu.NONE, R.id.mode_user_switch, Menu.FIRST, R.string.change_user_mode);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(navigationView.getHeaderCount()-1);

        ((TextView)headerView.findViewById(R.id.usuario_nombre))
                .setText(loggedUser.getNombre());
        ((TextView)headerView.findViewById(R.id.usuario_mail))
                .setText(loggedUser.getCorreo());

        Glide.with(getBaseContext())
                .load( loggedUser.getFotoURL() )
                .circleCrop()
                .error(R.drawable.ic_launcher_foreground)
                .into(
                        (ImageView)headerView.findViewById(R.id.usuario_image)
                );

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        switchModeItem = menu.findItem(R.id.mode_user_switch);
        Switch switchView = ((Switch) switchModeItem.getActionView().findViewById(R.id.switch_view));

        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != GlobalInformation.ON_MANAGER_MODE) {
                    GlobalInformation.ON_MANAGER_MODE = isChecked;
                    GlobalInformation.home.changeMode(isChecked);
                    //TODO(FALTAN POR ACTUALIZAR)
                }
            }
        });

        update();

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void update() {
        loggedUser = GlobalInformation.SIGN_IN_USER;
        if(menu != null) {
            if (loggedUser.getGestiona().size() <= 0) {
                switchModeItem.setEnabled(false);
                switchModeItem.setVisible(false);
            } else {
                switchModeItem.setEnabled(true);
                switchModeItem.setVisible(true);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalInformation.reset();
    }
}