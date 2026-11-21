package com.simos.core;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Consola {

    private static final Scanner ENTRADA = new Scanner(System.in);

    private Consola() {
    }

    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        try {
            return ENTRADA.nextLine().trim();
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("Entrada finalizada. Cerrando SimOS.");
            System.exit(0);
            return "";
        }
    }

    public static int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("  Valor no válido. Escribe un número entero.");
            }
        }
    }

    public static int leerEnteroEnRango(String mensaje, int min, int max) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.println("  Opción fuera de rango (" + min + " a " + max + ").");
        }
    }

    public static void pausar() {
        leerTexto("\nPresiona Enter para continuar...");
    }
}
