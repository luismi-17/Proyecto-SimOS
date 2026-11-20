package com.simos.core;

import com.simos.seguridad.ControlAcceso;
import com.simos.seguridad.Usuario;

import java.util.List;

public class MenuPrincipal {

    private final Sesion sesion;
    private final List<Modulo> modulos;
    private final ControlAcceso control;

    public MenuPrincipal(Sesion sesion, List<Modulo> modulos, ControlAcceso control) {
        this.sesion = sesion;
        this.modulos = modulos;
        this.control = control;
    }

    public void iniciar() {
        int mayorId = 0;
        for (Modulo m : modulos) {
            mayorId = Math.max(mayorId, m.getId());
        }

        boolean salir = false;
        while (!salir) {
            mostrar();
            int opcion = Consola.leerEnteroEnRango("Seleccione una opción: ", 0, mayorId);
            if (opcion == 0) {
                sesion.registrar("Cierre de sesión", true);
                System.out.println("\nSesión cerrada. ¡Hasta pronto!");
                salir = true;
            } else {
                abrir(opcion);
            }
        }
    }

    private void mostrar() {
        Usuario usuario = sesion.getUsuario();
        System.out.println();
        System.out.println("========== SimOS v1.0 ==========");
        System.out.printf("Usuario: %-12s Rol: %s%n", usuario.getNombre(), usuario.getRol().getNombre());
        System.out.println("--------------------------------");
        for (Modulo m : modulos) {
            System.out.println(m.getId() + ". " + m.getNombre());
        }
        System.out.println("0. Salir");
    }

    private void abrir(int id) {
        Modulo elegido = null;
        for (Modulo m : modulos) {
            if (m.getId() == id) {
                elegido = m;
            }
        }
        if (elegido == null) {
            System.out.println("  Esa opción no está disponible.");
            return;
        }

        String nombre = elegido.getId() + " - " + elegido.getNombre();
        Usuario usuario = sesion.getUsuario();

        if (!control.puedeAcceder(usuario, id)) {
            System.out.println("\n  ACCESO DENEGADO: el rol " + usuario.getRol().getNombre()
                    + " no tiene permiso para el módulo " + nombre + ".");
            sesion.registrar("Intento de ingreso al módulo " + nombre, false);
            Consola.pausar();
            return;
        }

        sesion.registrar("Ingreso al módulo " + nombre, true);
        elegido.ejecutar(sesion);
    }
}
