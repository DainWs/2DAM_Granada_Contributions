package com.josealex.granadacontributions.modules;

import com.josealex.granadacontributions.utils.Consulta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class Mercado {

    private String uid;
    private String nombre;
    private ArrayList<String> gestores = new ArrayList<>();
    private ArrayList<Productos> productos = new ArrayList<>();

    public Mercado(){}

    public Mercado(String uid, String nombre, ArrayList<String> gestores, ArrayList<Productos> productos) {
        this.uid = uid;
        this.nombre = nombre;
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

    public ArrayList<String> getGestoresWhere(Consulta<String> where) {
        ArrayList<String> select = new ArrayList<>();

        for (String gestor : gestores) {
            if(where.test(gestor)) {
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
            if(where.test(producto)) {
                select.add(producto);
            }
        }

        return select;
    }
}
