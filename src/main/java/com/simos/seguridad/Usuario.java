package com.simos.seguridad;

public class Usuario {

    private final String nombre;
    private final String hashClave;
    private final Rol rol;
    private boolean bloqueado;
    private int intentosFallidos;

    public Usuario(String nombre, String hashClave, Rol rol, boolean bloqueado) {
        this.nombre = nombre;
        this.hashClave = hashClave;
        this.rol = rol;
        this.bloqueado = bloqueado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getHashClave() {
        return hashClave;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    //Suma un intento fallido y devuelve el total acumulado
    public int sumarFallo() {
        intentosFallidos++;
        return intentosFallidos;
    }

    public void reiniciarFallos() {
        intentosFallidos = 0;
    }
}
