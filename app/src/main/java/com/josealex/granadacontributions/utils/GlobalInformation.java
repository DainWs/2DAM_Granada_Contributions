package com.josealex.granadacontributions.utils;

import com.josealex.granadacontributions.MainActivity;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.ui.home.HomeFragment;
import com.josealex.granadacontributions.ui.home.ProductosListFragment;
import com.josealex.granadacontributions.ui.home.UserFragment;
import com.josealex.granadacontributions.ui.setting.PreferenceFragment;
import java.util.ArrayList;

public class GlobalInformation {
    public static User SIGN_IN_USER = new User();
    public static ArrayList<Mercado> MERCADOS = new ArrayList<>();
    public static ArrayList<User> USERS = new ArrayList<>();

    public static boolean ON_MANAGER_MODE = false;

    public static MainActivity mainActivity = null;
    public static HomeFragment home = new HomeFragment();
    public static PreferenceFragment preferences = new PreferenceFragment();
    public static ProductosListFragment productosListFragment = new ProductosListFragment();
    public static UserFragment userFragment = new UserFragment();

    public static void reset() {
        SIGN_IN_USER = new User();
        MERCADOS = new ArrayList<>();
        USERS = new ArrayList<>();

        ON_MANAGER_MODE = false;

        mainActivity = null;
        home = new HomeFragment();
        preferences = new PreferenceFragment();
        productosListFragment = new ProductosListFragment();
        userFragment = new UserFragment();
    }
}
