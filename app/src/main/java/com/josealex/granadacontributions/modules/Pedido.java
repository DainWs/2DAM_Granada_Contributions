package com.josealex.granadacontributions.modules;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class Pedido implements Serializable, Comparable {

    private String uid = "";
    private String uidCliente = "";
    private String uidMercado = "";
    private String nombreCliente = "";
    private String date = "";
    private boolean isPending = true;
    private float total = 0;
    private ArrayList<LineaPedido> lineas = new ArrayList<>();

    public Pedido() {
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    public Pedido(User cliente, Mercado mercado) {
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        this.uidCliente = cliente.getUid();
        this.uidMercado = mercado.getUid();
        this.uid = uidCliente+uidMercado+date;
        this.nombreCliente = cliente.getNombre();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUidCliente() {
        return uidCliente;
    }

    public void setUidCliente(String uidCliente) {
        this.uidCliente = uidCliente;
        this.uid = uidCliente+uidMercado+date;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public String getUidMercado() {
        return uidMercado;
    }

    public void setUidMercado(String uidMercado) {
        this.uidMercado = uidMercado;
        this.uid = uidCliente+uidMercado+date;
    }

    public ArrayList<LineaPedido> getLineas() {
        return lineas;
    }

    public void setLineas(ArrayList<LineaPedido> lineas) {
        this.lineas = lineas;
    }

    public void addLineas(LineaPedido lineaPedido) {
        if(!lineas.contains(lineaPedido)) {
            lineaPedido.setUid(lineas.size()+"");
            lineas.add(lineaPedido);
        }
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        this.uid = uidCliente+uidMercado+date;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    @Override
    public int compareTo(Object o) {
        Date dateOne;
        Date dateTwo;
        try {
            dateOne = new SimpleDateFormat().parse(((Pedido)o).getDate());
            dateTwo = new SimpleDateFormat().parse(date);
        }catch (Exception e) {
            return -1;
        }
        return dateOne.compareTo(dateTwo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return pedido.getUid()==uid;
    }
}
