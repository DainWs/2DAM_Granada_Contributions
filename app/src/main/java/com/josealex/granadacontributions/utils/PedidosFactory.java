package com.josealex.granadacontributions.utils;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.LineaPedido;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Pedido;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.modules.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PedidosFactory {

    private static Pedido pedido = new Pedido();
    private static User cliente = new User();
    private static Mercado mercadoActual = new Mercado();

    private static Map<String, Productos> productosMap = new HashMap<>();

    public static void make(User nCliente, Mercado mercado) {
        mercadoActual = mercado;
        cliente = nCliente;
        productosMap = new HashMap<>();
        pedido = new Pedido(cliente, mercado);
    }

    public static void addLinea(Mercado mercado, Productos producto) {
        boolean valid;
        if(pedido == null || productosMap.size() == 0) {
            make(GlobalInformation.SIGN_IN_USER, mercado);
        }
        else {
            if(!mercadoActual.getUid().equals(mercado.getUid())) {
                DialogsFactory.makeWarnningDialog(
                        ResourceManager.getString(R.string.not_equals_market)
                );
                return;
            }
            else if(productosMap.containsKey(producto.getUid())) {
                DialogsFactory.makeWarnningDialog(
                        ResourceManager.getString(R.string.duplicated_warnning)
                );
                return;
            }
        }

        productosMap.put(producto.getUid(), producto);
        pedido.addLineas(new LineaPedido(pedido, producto, 1));
    }

    public static void removeLinea(LineaPedido lineaPedido) {
        if(pedido != null) {
            productosMap.remove(lineaPedido.getUidProducto());
            pedido.removeLineas(lineaPedido);
        }
    }

    public static void set(Pedido newPedido, Mercado mercado) {
        pedido = newPedido;
        mercadoActual = mercado;

        productosMap = new HashMap<>();

        ArrayList<Productos> list = mercado.getProductosWhere(new Consulta<Productos>() {
            @Override
            public boolean comprueba(Productos o) {
                return pedido.getLineas().contains(o.getUid());
            }
        });

        for (Productos producto: list) {
            addLinea(mercadoActual, producto);
        }
    }

    public static void cancel() {
        pedido = null;
        mercadoActual = new Mercado();
        cliente = new User();
        productosMap = new HashMap<>();
    }

    public static Pedido get() {
        return pedido;
    }

    public static User getCliente() {return cliente;}

    public static Mercado getMercadoActual() {return mercadoActual;}

    public static Productos getProducto(String uidProducto) {
        return productosMap.get(uidProducto);
    }

    private static float total = 0;

    public static float getTotal() {
        total = 0;
        for (LineaPedido linea: pedido.getLineas()) {
            Productos producto = productosMap.get(linea.getUid());
            total += linea.getPrecio() * linea.getCantidad();
        }

        return total;
    }



}
