package com.josealex.granadacontributions.utils;

import com.josealex.granadacontributions.MainActivity;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.ui.home.ClientPendingOrdersFragment;
import com.josealex.granadacontributions.ui.home.HomeFragment;
import com.josealex.granadacontributions.ui.home.ListPedidosFragment;
import com.josealex.granadacontributions.ui.home.MercadoFragment;
import com.josealex.granadacontributions.ui.home.ProductosListFragment;
import com.josealex.granadacontributions.ui.home.ShoppingCardFragment;
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
    public static ShoppingCardFragment userShopping = new ShoppingCardFragment();
    public static MercadoFragment mercadoFragment = new MercadoFragment();
    public static ClientPendingOrdersFragment clientPendingListFragment = new ClientPendingOrdersFragment();
    public static ListPedidosFragment marketPendingListFragment = new ListPedidosFragment();

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
        mercadoFragment = new MercadoFragment();
        clientPendingListFragment = new ClientPendingOrdersFragment();
        marketPendingListFragment = new ListPedidosFragment();
    }
}
