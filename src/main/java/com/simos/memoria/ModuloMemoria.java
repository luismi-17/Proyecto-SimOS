package com.simos.memoria;

import com.simos.core.Consola;
import com.simos.core.Modulo;
import com.simos.core.Sesion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuloMemoria implements Modulo {

    private static final int[] CADENA_PRUEBA =
            {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1};

    // Para agregar otro algoritmo (por ejemplo OPT) solo se añade aquí.
    private final List<AlgoritmoReemplazo> algoritmos = Arrays.asList(new FIFO(), new LRU());

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public String getNombre() {
        return "Gestión de memoria";
    }

    @Override
    public void ejecutar(Sesion sesion) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Gestión de memoria (paginación) ---");
            System.out.println("1. Ejecutar caso de prueba (3 y 4 marcos)");
            System.out.println("2. Ingresar mis propios datos");
            System.out.println("0. Volver al menú principal");
            int opcion = Consola.leerEnteroEnRango("Seleccione una opción: ", 0, 2);

            switch (opcion) {
                case 1:
                    casoDePrueba(sesion);
                    break;
                case 2:
                    datosPropios(sesion);
                    break;
                default:
                    volver = true;
            }
        }
    }

    private void casoDePrueba(Sesion sesion) {
        System.out.println("\nCadena de referencias: " + cadenaATexto(CADENA_PRUEBA));
        simular(CADENA_PRUEBA, 3, sesion);
        Consola.pausar();
        simular(CADENA_PRUEBA, 4, sesion);
        Consola.pausar();
    }

    private void datosPropios(Sesion sesion) {
        int marcos = Consola.leerEnteroEnRango("Número de marcos (1 a 10): ", 1, 10);
        int[] cadena = leerCadena();
        simular(cadena, marcos, sesion);
        Consola.pausar();
    }

    /** Ejecuta todos los algoritmos con los mismos datos y muestra el resumen. */
    private void simular(int[] cadena, int marcos, Sesion sesion) {
        List<ResultadoMemoria> resultados = new ArrayList<>();
        for (AlgoritmoReemplazo algoritmo : algoritmos) {
            ResultadoMemoria resultado = algoritmo.simular(cadena, marcos);
            mostrarPasos(resultado);
            resultados.add(resultado);
        }
        mostrarResumen(resultados, marcos);
        sesion.registrar("Simulación de memoria con " + marcos + " marcos ("
                + cadena.length + " referencias)", true);
    }

    private void mostrarPasos(ResultadoMemoria r) {
        System.out.println("\n=== " + r.getAlgoritmo() + " con " + r.getNumMarcos() + " marcos ===");
        System.out.printf("%-6s%-6s%-18s%s%n", "Paso", "Ref", "Marcos", "Resultado");
        int numero = 1;
        for (PasoMemoria p : r.getPasos()) {
            String resultado = "acierto";
            if (p.isFallo()) {
                resultado = "FALLO";
                if (p.getPaginaReemplazada() >= 0) {
                    resultado += " (sale " + p.getPaginaReemplazada() + ")";
                }
            }
            System.out.printf("%-6d%-6d%-18s%s%n", numero, p.getReferencia(),
                    marcosATexto(p.getMarcos()), resultado);
            numero++;
        }
        System.out.println("Fallos de página: " + r.getFallos() + " | Aciertos: " + r.getAciertos());
    }

    private void mostrarResumen(List<ResultadoMemoria> resultados, int marcos) {
        System.out.println("\n--- Resumen con " + marcos + " marcos ---");
        System.out.printf("%-12s%-10s%s%n", "Algoritmo", "Fallos", "Aciertos");
        int minimo = Integer.MAX_VALUE;
        for (ResultadoMemoria r : resultados) {
            System.out.printf("%-12s%-10d%d%n", r.getAlgoritmo(), r.getFallos(), r.getAciertos());
            minimo = Math.min(minimo, r.getFallos());
        }
        List<String> mejores = new ArrayList<>();
        for (ResultadoMemoria r : resultados) {
            if (r.getFallos() == minimo) {
                mejores.add(r.getAlgoritmo());
            }
        }
        if (mejores.size() == 1) {
            System.out.println("Menos fallos: " + mejores.get(0) + " (" + minimo + " fallos)");
        } else {
            System.out.println("Empate con " + minimo + " fallos: " + String.join(", ", mejores));
        }
    }

    private int[] leerCadena() {
        while (true) {
            String linea = Consola.leerTexto("Cadena de referencias (números separados por espacios o comas): ");
            int[] cadena = parsear(linea);
            if (cadena != null) {
                return cadena;
            }
            System.out.println("  Escribe al menos un número entero mayor o igual a 0, por ejemplo: 7 0 1 2 0 3");
        }
    }

    /** Convierte el texto en arreglo de enteros, o devuelve null si no es válido. */
    private int[] parsear(String linea) {
        if (linea.isEmpty()) {
            return null;
        }
        String[] partes = linea.split("[,\\s]+");
        int[] cadena = new int[partes.length];
        for (int i = 0; i < partes.length; i++) {
            try {
                cadena[i] = Integer.parseInt(partes[i]);
            } catch (NumberFormatException e) {
                return null;
            }
            if (cadena[i] < 0) {
                return null;
            }
        }
        return cadena;
    }

    private String marcosATexto(int[] marcos) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < marcos.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(marcos[i] == -1 ? "-" : String.valueOf(marcos[i]));
        }
        return sb.append(']').toString();
    }

    private String cadenaATexto(int[] cadena) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cadena.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(cadena[i]);
        }
        return sb.toString();
    }
}
