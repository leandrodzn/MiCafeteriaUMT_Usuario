package com.example.micafeteriaumt_usuario;

import com.google.gson.Gson;

public class Cliente {
    int id;
    String nombre;
    String contrasena;
    String telefono;
    String creacion;
    String actualizado;

    public Cliente(int id, String nombre, String telefono, String contrasena, String creacion, String actualizado) {
        this.id = id;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.creacion = creacion;
        this.actualizado = actualizado;
    }

    public static Cliente fromJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Cliente.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCreacion() {
        return creacion;
    }

    public void setCreacion(String creacion) {
        this.creacion = creacion;
    }

    public String getActualizado() {
        return actualizado;
    }

    public void setActualizado(String actualizado) {
        this.actualizado = actualizado;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", telefono='" + telefono + '\'' +
                ", creacion='" + creacion + '\'' +
                ", actualizado='" + actualizado + '\'' +
                '}';
    }
}
