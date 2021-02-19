package com.josealex.granadacontributions.utils;

import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.User;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Consulta<T> {

    public Consulta(){}

    public abstract boolean comprueba(T o);

    public static ArrayList<Mercado> getMercadosWhere(Consulta<Mercado> where) {
        ArrayList<Mercado> select = new ArrayList<>();

        for (Mercado mercado : GlobalInformation.MERCADOS) {
            if(where.comprueba(mercado)) {
                select.add(mercado);
            }
        }

        return select;
    }

    public static ArrayList<Mercado> getMercadosWhere(
            Collection<Mercado> mercados, Consulta<Mercado> where) {
        ArrayList<Mercado> select = new ArrayList<>();

        for (Mercado mercado : mercados) {
            if(where.comprueba(mercado)) {
                select.add(mercado);
            }
        }

        return select;
    }

    public static ArrayList<User> getUsersWhere(Consulta<User> where) {
        ArrayList<User> select = new ArrayList<>();

        if(where.comprueba(GlobalInformation.SIGN_IN_USER)) {
            select.add(GlobalInformation.SIGN_IN_USER);
        }

        for (User user : GlobalInformation.USERS) {
            if(where.comprueba(user)) {
                select.add(user);
            }
        }

        return select;
    }

    public static ArrayList<User> getUsersWhere(
            Collection<User> users, Consulta<User> where) {
        ArrayList<User> select = new ArrayList<>();

        for (User user : users) {
            if(where.comprueba(user)) {
                select.add(user);
            }
        }

        return select;
    }

}
