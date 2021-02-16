package com.josealex.granadacontributions.modules;

import com.josealex.granadacontributions.utils.Consulta;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String uid;
    private String nombre;
    private String apellido;
    private String correo;
    private String fotoURL;
    private ArrayList<String> gestiona = new ArrayList<>();

    public User(){}

    public User(String uid, String nombre, String apellido, String correo, String fotoURL) {
        this.uid = uid;
        this.nombre = nombre;
        this.apellido = apellido;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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

    public ArrayList<String> getGestionesWhere(Consulta<String> where) {
        ArrayList<String> select = new ArrayList<>();

        for (String gestiones : gestiona) {
            if(where.test(gestiones)) {
                select.add(gestiones);
            }
        }

        return select;
    }
}
