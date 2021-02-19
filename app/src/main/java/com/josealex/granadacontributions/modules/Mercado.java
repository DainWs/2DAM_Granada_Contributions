package com.josealex.granadacontributions.modules;

import com.josealex.granadacontributions.utils.Consulta;

import java.io.Serializable;
import java.util.ArrayList;

public class Mercado implements Serializable {

    private String uid = "";
    private String nombre = "";
    private String uidOwner = "";
    private String password = "";
    private ArrayList<String> gestores = new ArrayList<>();
    private ArrayList<Productos> productos = new ArrayList<>();

    public Mercado(){}

    public Mercado(
            String uid,
            String nombre,
            String password,
            ArrayList<String> gestores,
            ArrayList<Productos> productos
    ) {
        this.uid = uid;
        this.nombre = nombre;
        this.uidOwner = gestores.get(0);
        this.gestores = gestores;
        this.productos = productos;
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

    public String getUidOwner() {
        return (uidOwner == null) ? gestores.get(0) : uidOwner;
    }

    public void setUidOwner(String uidOwner) {
        if(uidOwner != null && !uidOwner.isEmpty()) this.uidOwner = uidOwner;
    }

    public void updateUidOwner(String newUIDOwner) {
        if (gestores.contains(newUIDOwner)) {
            for (int i = 0; i < gestores.size(); i++) {
                if (gestores.get(i).equalsIgnoreCase(newUIDOwner)) {
                    String oldOwner = gestores.get(0);
                    gestores.set(0, newUIDOwner);
                    gestores.set(i, oldOwner);
                }
            }
            this.uidOwner = newUIDOwner;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getGestores() {
        return gestores;
    }

    public void setGestores(ArrayList<String> gestores) {
        this.gestores = gestores;
    }

    public void addGestor(User newGestor) {
        addGestor(newGestor.getUid());
    }

    public void addGestor(String newUidGestor) {
        if(!gestores.contains(newUidGestor)) {
            gestores.add(newUidGestor);
        }
    }

    public void removeGestores(String uid) {
        if(gestores.contains(uid)) {
            gestores.remove(uid);
        }
    }

    public ArrayList<String> getGestoresWhere(Consulta<String> where) {
        ArrayList<String> select = new ArrayList<>();

        for (String gestor : gestores) {
            if(where.comprueba(gestor)) {
                select.add(gestor);
            }
        }

        return select;
    }

    public ArrayList<Productos> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Productos> productos) {
        this.productos = productos;
    }

    public void addProducto(Productos newProducto) {
        if(!productos.contains(newProducto)) {
            productos.add(newProducto);
        }
    }

    public ArrayList<Productos> getProductosWhere(Consulta<Productos> where) {
        ArrayList<Productos> select = new ArrayList<>();

        for (Productos producto : productos) {
            if(where.comprueba(producto)) {
                select.add(producto);
            }
        }

        return select;
    }



}
