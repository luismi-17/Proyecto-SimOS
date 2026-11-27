package com.simos.concurrencia;

import com.simos.core.Consola;
import com.simos.core.Modulo;
import com.simos.core.Sesion;

public class ModuloConcurrencia implements Modulo {

    private static final int REPETICIONES = 5;

    @Override
    public int getId() {
        return 2; // opción 2 del menú
    }

    @Override
    public String getNombre() {
        return "Concurrencia y sincronización";
    }

    @Override
    public void ejecutar(Sesion sesion) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Módulo 2: Concurrencia y sincronización ---");
            System.out.println("1. Contador compartido (sin sincronización vs. con candado)");
            System.out.println("2. Productor-consumidor");
            System.out.println("3. Interbloqueo (deadlock) y cómo evitarlo");
            System.out.println("0. Volver al menú principal");
            int opcion = Consola.leerEnteroEnRango("Seleccione una opción: ", 0, 3);

            switch (opcion) {
                case 1:
                    registrar(sesion, "Concurrencia: contador compartido");
                    contador();
                    Consola.pausar();
                    break;
                case 2:
                    registrar(sesion, "Concurrencia: productor-consumidor");
                    productorConsumidor();
                    Consola.pausar();
                    break;
                case 3:
                    registrar(sesion, "Concurrencia: simulación de interbloqueo");
                    interbloqueo();
                    Consola.pausar();
                    break;
                default:
                    volver = true;
            }
        }
    }

    private void contador() {
        int hilos = 10;
        int incrementos = 1000;
        if (!usarDatosDePrueba("10 hilos x 1000 incrementos")) {
            hilos = Consola.leerEnteroEnRango("Número de hilos (1 a 100): ", 1, 100);
            incrementos = Consola.leerEnteroEnRango("Incrementos por hilo (1 a 100000): ", 1, 100000);
        }
        int esperado = hilos * incrementos;

        System.out.println("\nResultado esperado: " + esperado);
        System.out.println("\nSIN sincronización (" + REPETICIONES + " ejecuciones):");
        boolean huboError = false;
        for (int i = 1; i <= REPETICIONES; i++) {
            int resultado = ContadorCompartido.ejecutar(hilos, incrementos, false);
            huboError |= resultado != esperado;
            System.out.printf("  Ejecución %d: %d  (%s)%n", i, resultado, estado(resultado, esperado));
        }

        System.out.println("\nCON candado / mutex (" + REPETICIONES + " ejecuciones):");
        for (int i = 1; i <= REPETICIONES; i++) {
            int resultado = ContadorCompartido.ejecutar(hilos, incrementos, true);
            System.out.printf("  Ejecución %d: %d  (%s)%n", i, resultado, estado(resultado, esperado));
        }

        System.out.println();
        if (huboError) {
            System.out.println("Conclusión: sin sincronización se pierden incrementos (condición de carrera);");
        } else {
            System.out.println("Conclusión: esta vez no se notó la carrera (es no determinista); prueba con más incrementos.");
        }
        System.out.println("con el candado solo un hilo a la vez modifica el contador y el resultado es siempre exacto.");
    }

    private String estado(int resultado, int esperado) {
        return resultado == esperado ? "correcto" : "ERROR: faltan " + (esperado - resultado);
    }

    private void productorConsumidor() {
        int capacidad = 5;
        int elementos = 20;
        if (!usarDatosDePrueba("buffer de 5 posiciones y 20 elementos")) {
            capacidad = Consola.leerEnteroEnRango("Tamaño del buffer (1 a 20): ", 1, 20);
            elementos = Consola.leerEnteroEnRango("Cantidad de elementos (1 a 200): ", 1, 200);
        }
        System.out.println("\n2 productores y 2 consumidores, buffer de " + capacidad
                + ", " + elementos + " elementos.\n");
        new ProductorConsumidor(capacidad).ejecutar(elementos, 2, 2);
        System.out.println("\nSe produjeron y consumieron " + elementos + " elementos sin perder ninguno.");
    }

    private void interbloqueo() {
        System.out.println("\n=== Caso 1: cada proceso pide los recursos en orden distinto ===");
        new SimulacionInterbloqueo().ejecutar(false);

        System.out.println("\n=== Caso 2: ambos piden R1 y luego R2 (mismo orden) ===");
        new SimulacionInterbloqueo().ejecutar(true);

        System.out.println("\nConclusión: pedir siempre los recursos en el mismo orden rompe la espera circular,");
        System.out.println("una de las cuatro condiciones necesarias para el interbloqueo.");
    }

    private boolean usarDatosDePrueba(String descripcion) {
        String r = Consola.leerTexto("¿Usar datos de prueba (" + descripcion + ")? (s/n): ");
        return r.equalsIgnoreCase("s");
    }

    private void registrar(Sesion sesion, String accion) {
        if (sesion != null) { // sesion es null solo si se prueba el módulo suelto
            sesion.registrar(accion, true);
        }
    }
}
