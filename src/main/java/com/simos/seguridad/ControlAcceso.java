package com.simos.seguridad;

import com.simos.core.Modulo;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ControlAcceso {

    private final Map<Rol, Set<Integer>> permisos = new EnumMap<>(Rol.class);

    public ControlAcceso() {
        permisos.put(Rol.ADMINISTRADOR, new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5)));
        permisos.put(Rol.USUARIO, new TreeSet<>(Arrays.asList(1, 2, 3, 4)));
    }

    public boolean puedeAcceder(Usuario usuario, int idModulo) {
        Set<Integer> permitidos = permisos.get(usuario.getRol());
        return permitidos != null && permitidos.contains(idModulo);
    }

    public void mostrarMatriz(List<Modulo> modulos) {
        System.out.printf("%-46s", "Módulo");
        for (Rol rol : Rol.values()) {
            System.out.printf("%-15s", rol.getNombre());
        }
        System.out.println();
        System.out.println("-".repeat(46 + 15 * Rol.values().length));
        for (Modulo m : modulos) {
            System.out.printf("%-46s", m.getId() + ". " + m.getNombre());
            for (Rol rol : Rol.values()) {
                System.out.printf("%-15s", permisos.get(rol).contains(m.getId()) ? "SI" : "NO");
            }
            System.out.println();
        }
    }
}