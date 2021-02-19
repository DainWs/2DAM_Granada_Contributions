package com.josealex.granadacontributions.modules;

import com.josealex.granadacontributions.utils.Consulta;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String uid;
    private String nombre;
    private String correo;
    private String fotoURL;
    private int saldo;
    private ArrayList<String> gestiona = new ArrayList<>();
    private ArrayList<String> lineafactura = new ArrayList<>();

    public User(){}

    public User(String uid, String nombre, String correo, String fotoURL) {
        this.uid = uid;
        this.nombre = nombre;
        this.correo = correo;
        this.fotoURL = fotoURL;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public ArrayList<String> getGestiona() {
        return gestiona;
    }

    public void setGestiona(ArrayList<String> gestiona) {
        this.gestiona = gestiona;
    }

    public void addGestiones(Mercado newMercado) {
        addGestiones(newMercado.getUid());
    }

    public void addGestiones(String newUidMercado) {
        if(!gestiona.contains(newUidMercado)) {
            gestiona.add(newUidMercado);
        }
    }

    public void  removeGestiones(String marketUID) {
        if(gestiona.contains(marketUID)) {
            gestiona.remove(marketUID);
        }
    }

    public ArrayList<String> getGestionesWhere(Consulta<String> where) {
        ArrayList<String> select = new ArrayList<>();

        for (String gestiones : gestiona) {
            if(where.comprueba(gestiones)) {
                select.add(gestiones);
            }
        }

        return select;
    }

    public boolean hasGestionesWhere(Consulta<String> where) {
        for (String gestiones : gestiona) {
            if(where.comprueba(gestiones)) {
                return true;
            }
        }
        return false;
    }
}
