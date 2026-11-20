package com.simos.core;

import com.simos.seguridad.Bitacora;
import com.simos.seguridad.Usuario;

public class Sesion {

    private final Usuario usuario;
    private final Bitacora bitacora;

    public Sesion(Usuario usuario, Bitacora bitacora) {
        this.usuario = usuario;
        this.bitacora = bitacora;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    //Registra una acción del usuario actual en la bitácora de auditoría.
    public void registrar(String accion, boolean permitida) {
        bitacora.registrar(usuario.getNombre(), accion, permitida);
    }
}
