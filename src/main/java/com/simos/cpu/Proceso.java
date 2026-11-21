package com.simos.cpu;

public class Proceso {
    private final String nombre;
    private final int llegada;
    private final int rafaga;

    public Proceso(String nombre, int llegada, int rafaga) {
        this.nombre = nombre;
        this.llegada = llegada;
        this.rafaga = rafaga;
    }

    public String getNombre() { return nombre; }
    public int getLlegada() { return llegada; }
    public int getRafaga() { return rafaga; }
}