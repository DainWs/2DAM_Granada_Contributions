package com.josealex.granadacontributions.modules;

import java.io.Serializable;

public class LineaPedido implements Serializable {

    private String uid = "";
    private String uidPedido = "";
    private String uidProducto = "";
    private String nombreProducto = "";
    private float precio = 0;
    private int cantidad = 0;

    public LineaPedido(){}

    public LineaPedido(Pedido pedido, Productos producto, int cantidad){
        this.uidPedido = pedido.getUid();
        this.uidProducto = producto.getUid();
        this.nombreProducto = producto.getNombre();
        this.precio = producto.getPrecio();
        this.cantidad = cantidad;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUidProducto() {
        return uidProducto;
    }

    public void setUidProducto(String uidProducto) {
        this.uidProducto = uidProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
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

    public void addCantidad(int cantidad) {
        this.cantidad += cantidad;
    }

    public void removeCantidad(int cantidad) {
        this.cantidad -= cantidad;
    }
}
