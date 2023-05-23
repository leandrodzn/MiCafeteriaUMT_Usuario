package com.example.micafeteriaumt_usuario;

public class DatosGlobales {
    public static Cliente cliente;
    public static Double totalCarrito;

    public static Double getTotalCarrito() {
        return totalCarrito;
    }

    public static void setTotalCarrito(Double totalCarrito) {
        DatosGlobales.totalCarrito = totalCarrito;
    }

    public static Cliente getCliente() {
        return cliente;
    }

    public static void setCliente(Cliente cliente) {
        DatosGlobales.cliente = cliente;
    }
}
