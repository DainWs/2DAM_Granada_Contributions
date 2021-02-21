package com.josealex.granadacontributions.modules;

import java.io.Serializable;

public class Productos implements Serializable {

    private String uid="";
    private String nombre="";
    private String fotoURL="";
    private String categoria="";
    private float precio=0;
    private int cantidad=0;

    public Productos() {
    }

    public Productos(String uid, String nombre, String categoria, float precio) {
        this.uid = uid;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
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

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
