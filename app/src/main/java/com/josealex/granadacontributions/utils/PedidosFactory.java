package com.josealex.granadacontributions.utils;

import com.josealex.granadacontributions.modules.LineaPedido;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Pedido;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.modules.User;

public class PedidosFactory {

    private static Pedido pedido;

    public static void make(User cliente, Mercado mercado) {
        pedido = new Pedido(cliente, mercado);
    }

    public static void addLinea(Mercado mercado, Productos producto) {
        if(pedido == null) {
            make(GlobalInformation.SIGN_IN_USER, mercado);
        }

        pedido.addLineas(new LineaPedido(pedido, producto, 1));
    }

    public static void removeLinea(LineaPedido lineaPedido) {
        if(pedido != null) {
            removeLinea(lineaPedido);
        }
    }

    public static void cancel() {
        pedido = null;
    }

    public static Pedido get() {
        return pedido;
    }




}
