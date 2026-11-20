package com.simos;

import com.simos.core.MenuPrincipal;
import com.simos.core.Modulo;
import com.simos.core.Sesion;
import com.simos.seguridad.*;

import java.util.List;

import com.simos.core.ModuloProvisional;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Bitacora bitacora = new Bitacora("datos/bitacora.txt");
        GestorUsuarios gestor = new GestorUsuarios("datos/usuarios.txt");
        ControlAcceso control = new ControlAcceso();

        Usuario usuario = InicioSesion.autenticar(gestor, bitacora);
        if (usuario == null) {
            System.out.println("\nDemasiados intentos fallidos. SimOS se cerrará.");
            return;
        }

        // Cada responsable reemplaza su ModuloProvisional por su clase real
        List<Modulo> modulos = new ArrayList<>();
        modulos.add(new ModuloProvisional(1, "Planificación de CPU"));
        modulos.add(new ModuloProvisional(2, "Concurrencia y sincronización"));
        modulos.add(new ModuloProvisional(3, "Gestión de memoria"));
        modulos.add(new ModuloProvisional(4, "Almacenamiento (planificación de disco)"));
        modulos.add(new ModuloSeguridad(gestor, control, bitacora, modulos));

        new MenuPrincipal(new Sesion(usuario, bitacora), modulos, control).iniciar();
    }
}

