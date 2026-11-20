package com.simos.seguridad;

import com.simos.core.Consola;
import com.simos.core.Modulo;
import com.simos.core.Sesion;

import java.util.ArrayList;
import java.util.List;

/* Módulo 5: usuarios, matriz de permisos y consulta de la bitácora. */
public class ModuloSeguridad implements Modulo {

    private static final int ENTRADAS_A_MOSTRAR = 25;

    private final GestorUsuarios gestor;
    private final ControlAcceso control;
    private final Bitacora bitacora;
    private final List<Modulo> modulos;

    public ModuloSeguridad(GestorUsuarios gestor, ControlAcceso control, Bitacora bitacora,
                           List<Modulo> modulos) {
        this.gestor = gestor;
        this.control = control;
        this.bitacora = bitacora;
        this.modulos = modulos;
    }

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public String getNombre() {
        return "Usuarios, permisos y bitácora";
    }

    @Override
    public void ejecutar(Sesion sesion) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Usuarios, permisos y bitácora ---");
            System.out.println("1. Ver usuarios");
            System.out.println("2. Ver matriz de permisos");
            System.out.println("3. Consultar bitácora");
            System.out.println("4. Desbloquear usuario");
            System.out.println("0. Volver al menú principal");
            int opcion = Consola.leerEnteroEnRango("Seleccione una opción: ", 0, 4);

            switch (opcion) {
                case 1:
                    verUsuarios();
                    sesion.registrar("Consultó la lista de usuarios", true);
                    break;
                case 2:
                    System.out.println();
                    control.mostrarMatriz(modulos);
                    sesion.registrar("Consultó la matriz de permisos", true);
                    break;
                case 3:
                    verBitacora();
                    sesion.registrar("Consultó la bitácora", true);
                    break;
                case 4:
                    desbloquear(sesion);
                    break;
                default:
                    volver = true;
            }
        }
    }

    private void verUsuarios() {
        System.out.println();
        System.out.printf("%-12s %-15s %s%n", "Usuario", "Rol", "Estado");
        System.out.println("----------------------------------------");
        for (Usuario u : gestor.listar()) {
            System.out.printf("%-12s %-15s %s%n", u.getNombre(), u.getRol().getNombre(),
                    u.isBloqueado() ? "BLOQUEADO" : "Activo");
        }
    }

    private void verBitacora() {
        List<String> lineas = bitacora.leer();
        System.out.println("\nÚltimos registros (archivo completo: " + bitacora.getRuta() + ")");
        if (lineas.isEmpty()) {
            System.out.println("La bitácora está vacía.");
            return;
        }
        System.out.println(lineas.get(0));
        System.out.println("------------------------------------------------------------");
        int desde = Math.max(1, lineas.size() - ENTRADAS_A_MOSTRAR);
        for (int i = desde; i < lineas.size(); i++) {
            System.out.println(lineas.get(i));
        }
    }

    private void desbloquear(Sesion sesion) {
        List<Usuario> bloqueados = new ArrayList<>();
        for (Usuario u : gestor.listar()) {
            if (u.isBloqueado()) {
                bloqueados.add(u);
            }
        }
        if (bloqueados.isEmpty()) {
            System.out.println("\nNo hay usuarios bloqueados.");
            return;
        }
        System.out.println("\nUsuarios bloqueados:");
        for (Usuario u : bloqueados) {
            System.out.println(" - " + u.getNombre());
        }
        String nombre = Consola.leerTexto("Usuario a desbloquear (Enter para cancelar): ");
        if (nombre.isEmpty()) {
            return;
        }
        Usuario usuario = gestor.buscar(nombre);
        if (usuario == null || !usuario.isBloqueado()) {
            System.out.println("Ese usuario no está bloqueado.");
            return;
        }
        gestor.desbloquear(usuario);
        sesion.registrar("Desbloqueó al usuario " + usuario.getNombre(), true);
        System.out.println("Usuario " + usuario.getNombre() + " desbloqueado.");
    }
}