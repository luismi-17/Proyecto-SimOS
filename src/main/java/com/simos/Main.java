package com.simos.core;

public class Main {

import simos.core.MenuPrincipal;
import simos.core.Modulo;
import simos.core.ModuloProvisional;
import simos.core.Sesion;
import simos.seguridad.Bitacora;
import simos.seguridad.ControlAcceso;
import simos.seguridad.GestorUsuarios;
import simos.seguridad.InicioSesion;
import simos.seguridad.ModuloSeguridad;
import simos.seguridad.Usuario;

import java.util.ArrayList;
import java.util.List;

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
            modulos.add(new zModuloSeguridad(gestor, control, bitacora, modulos));

            new com.simos.core.Main(new Sesion(usuario, bitacora), modulos, control).iniciar();
        }
    }
}
