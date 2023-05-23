package com.example.micafeteriaumt_usuario;

public class Pedido {
    int id;
    int id_producto;
    String nombre_producto;
    int cantidad;
    Double precio_producto;

    public Pedido(int id, int id_producto, String nombre_producto, int cantidad, Double precio_producto) {
        this.id = id;
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.cantidad = cantidad;
        this.precio_producto = precio_producto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio_producto() {
        return precio_producto;
    }

    public void setPrecio_producto(Double precio_producto) {
        this.precio_producto = precio_producto;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", id_producto=" + id_producto +
                ", nombre_producto='" + nombre_producto + '\'' +
                ", cantidad=" + cantidad +
                ", precio_producto=" + precio_producto +
                '}';
    }
}
