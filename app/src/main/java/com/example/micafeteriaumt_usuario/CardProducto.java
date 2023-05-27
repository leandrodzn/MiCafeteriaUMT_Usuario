package com.example.micafeteriaumt_usuario;
//Objeto que sirve para mostrar los productos existentes en la vista de Productos
public class CardProducto {
    public int id;
    public String nombreProducto;

    public CardProducto(int id, String nombreProducto) {
        this.id = id;
        this.nombreProducto = nombreProducto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    @Override
    public String toString() {
        return "CardProducto{" +
                "id=" + id +
                ", nombreProducto='" + nombreProducto + '\'' +
                '}';
    }
}
