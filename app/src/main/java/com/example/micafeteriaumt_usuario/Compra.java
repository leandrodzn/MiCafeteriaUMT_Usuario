package com.example.micafeteriaumt_usuario;

import java.util.ArrayList;
import java.util.List;

public class Compra {
    int id;
    int id_cliente;
    String nombre_cliente;
    String telefono_cliente;
    String tipo_pago;
    Double total;
    String hora_compra;
    String hora_recogida;
    int status;
    List<Pedido> pedidos = new ArrayList<>();

    public Compra(int id, int id_cliente, String nombre_cliente, String telefono_cliente, String tipo_pago, Double total, String hora_compra, String hora_recogida, int status) {
        this.id = id;
        this.id_cliente = id_cliente;
        this.nombre_cliente = nombre_cliente;
        this.telefono_cliente = telefono_cliente;
        this.tipo_pago = tipo_pago;
        this.total = total;
        this.hora_compra = hora_compra;
        this.hora_recogida = hora_recogida;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getTelefono_cliente() {
        return telefono_cliente;
    }

    public void setTelefono_cliente(String telefono_cliente) {
        this.telefono_cliente = telefono_cliente;
    }

    public String getTipo_pago() {
        return tipo_pago;
    }

    public void setTipo_pago(String tipo_pago) {
        this.tipo_pago = tipo_pago;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getHora_compra() {
        return hora_compra;
    }

    public void setHora_compra(String hora_compra) {
        this.hora_compra = hora_compra;
    }

    public String getHora_recogida() {
        return hora_recogida;
    }

    public void setHora_recogida(String hora_recogida) {
        this.hora_recogida = hora_recogida;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos.clear();
        this.pedidos.addAll(pedidos);
    }

    @Override
    public String toString() {
        return "Compra{" +
                "id=" + id +
                ", id_cliente=" + id_cliente +
                ", nombre_cliente='" + nombre_cliente + '\'' +
                ", telefono_cliente='" + telefono_cliente + '\'' +
                ", tipo_pago='" + tipo_pago + '\'' +
                ", total=" + total +
                ", hora_compra='" + hora_compra + '\'' +
                ", hora_recogida='" + hora_recogida + '\'' +
                ", status=" + status +
                '}';
    }
}
