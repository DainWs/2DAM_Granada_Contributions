package com.josealex.granadacontributions.utils;

import com.josealex.granadacontributions.MainActivity;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.User;

import java.util.ArrayList;
import java.util.Collection;

public class GlobalInformation {
    public static User SIGN_IN_USER = new User();
    public static ArrayList<Mercado> MERCADOS = new ArrayList<>();
    public static ArrayList<User> USERS = new ArrayList<>();

    public static MainActivity mainActivity = null;
}
