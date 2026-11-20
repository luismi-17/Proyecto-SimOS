package com.simos.seguridad;

import com.simos.core.Consola;
import com.simos.core.Modulo;

import java.util.*;

public final class InicioSesion {

    private static final int MAX_INTENTOS_PANTALLA = 3;

    private InicioSesion() {
    }

    //Devuelve el usuario autenticado, o null si se agotaron los intentos
    public static Usuario autenticar(GestorUsuarios gestor, Bitacora bitacora) {
        System.out.println("========== SimOS v1.0 ==========");
        System.out.println("Inicio de sesión");

        for (int intento = 1; intento <= MAX_INTENTOS_PANTALLA; intento++) {
            String nombre = Consola.leerTexto("Usuario: ");
            String clave = Consola.leerTexto("Contraseña: ");
            Usuario usuario = gestor.buscar(nombre);

            boolean claveCorrecta = usuario != null && gestor.verificarClave(usuario, clave);

            if (claveCorrecta && usuario.isBloqueado()) {
                bitacora.registrar(usuario.getNombre(), "Inicio de sesión (usuario bloqueado)", false);
                System.out.println("  El usuario está bloqueado. Pide a un administrador que lo desbloquee.");
                continue;
            }

            if (claveCorrecta) {
                usuario.reiniciarFallos();
                bitacora.registrar(usuario.getNombre(), "Inicio de sesión", true);
                return usuario;
            }

            // No se dice si falló el usuario o la contraseña, para no dar pistas.
            System.out.println("  Usuario o contraseña incorrectos.");
            if (usuario == null) {
                bitacora.registrar(nombre.isEmpty() ? "(vacío)" : nombre,
                        "Inicio de sesión (usuario inexistente)", false);
            } else if (!usuario.isBloqueado()) {
                bitacora.registrar(usuario.getNombre(), "Inicio de sesión (contraseña incorrecta)", false);
                if (gestor.registrarFallo(usuario)) {
                    bitacora.registrar(usuario.getNombre(),
                            "Usuario bloqueado por " + GestorUsuarios.MAX_INTENTOS + " intentos fallidos", false);
                    System.out.println("  El usuario fue bloqueado por " + GestorUsuarios.MAX_INTENTOS
                            + " intentos fallidos.");
                }
            }
        }
        return null;
    }
}


