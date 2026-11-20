package com.simos.core;

public class ModuloProvisional implements Modulo {

    private final int id;
    private final String nombre;

    public ModuloProvisional(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void ejecutar(Sesion sesion) {
        System.out.println("\n--- " + nombre + " ---");
        System.out.println("Este módulo aún está en desarrollo.");
        Consola.pausar();
    }
}
