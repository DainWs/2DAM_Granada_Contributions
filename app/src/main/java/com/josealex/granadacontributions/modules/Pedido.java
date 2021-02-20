package com.josealex.granadacontributions.modules;

import java.util.ArrayList;

public class Pedido {

    private String uid = "";
    private String uidCliente = "";
    private String uidMercado = "";
    private ArrayList<LineaPedido> lineas = new ArrayList<>();

    public Pedido() {}

    public Pedido(String uidCliente, String uidMercado) {
        this.uidCliente = uidCliente;
        this.uidMercado = uidMercado;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
        for (LineaPedido linea: lineas) {
            linea.setUidProducto(uid);
        }
    }

    public String getUidCliente() {
        return uidCliente;
    }

    public void setUidCliente(String uidCliente) {
        this.uidCliente = uidCliente;
    }

    public String getUidMercado() {
        return uidMercado;
    }

    public void setUidMercado(String uidMercado) {
        this.uidMercado = uidMercado;
    }

    public ArrayList<LineaPedido> getLineas() {
        return lineas;
    }

    public void setLineas(ArrayList<LineaPedido> lineas) {
        this.lineas = lineas;
    }

    public void addLineas(LineaPedido lineaPedido) {
        if(!lineas.contains(lineaPedido)) lineas.add(lineaPedido);
    }

    public void removeLineas(LineaPedido lineaPedido) {
        if(lineas.contains(lineaPedido)) lineas.remove(lineaPedido);
    }

    public LineaPedido getLineas(int index) {
        return lineas.get(index);
    }

    public LineaPedido getLineasWhere(int index) {
        return lineas.get(index);
    }


}
