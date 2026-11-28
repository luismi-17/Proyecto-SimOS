package com.simos.disco;

import com.simos.core.Modulo;
import com.simos.core.Sesion;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ModuloDisco implements Modulo{

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public String getNombre() {
        return "Planificación de Disco";
    }

    @Override
    public void ejecutar(Sesion sesion) {
        Scanner scanner = new Scanner(System.in);

        // Caso de prueba del enunciado
        int minCil = 0;
        int maxCil = 199;
        int cabezal = 70;
        int[] cola = {130, 25, 160, 45, 90, 10, 185, 75};
        boolean haciaMayores = true;

        System.out.println("=== " + getNombre().toUpperCase() + " (Opción " + getId() + ") ===");

        System.out.print("¿Deseas ingresar datos personalizados? (s/n, por defecto usa el caso de prueba): ");
        String respuesta = scanner.nextLine().trim().toLowerCase();

        if (respuesta.equals("s")) {
            System.out.print("Cilindro mínimo: ");
            minCil = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Cilindro máximo: ");
            maxCil = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Posición inicial del cabezal: ");
            cabezal = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Cola de solicitudes separadas por coma (ej. 130, 25, 160): ");
            String[] partes = scanner.nextLine().split(",");
            cola = new int[partes.length];
            for (int i = 0; i < partes.length; i++) {
                cola[i] = Integer.parseInt(partes[i].trim());
            }

            System.out.print("Dirección inicial hacia cilindros mayores? (true/false): ");
            haciaMayores = Boolean.parseBoolean(scanner.nextLine().trim());
        } else {
            System.out.println("Usando caso de prueba: [" + minCil + ".." + maxCil + "], cabezal=" +
                    cabezal + ", cola=" + Arrays.toString(cola));
        }

        List<PlanificadorDisco> planificadores = List.of(
                new FCFSDisco(),
                new SSTFDisco(),
                new SCANDisco()
        );

        ResultadoDisco mejor = null;

        System.out.println("\n--- Resultados ---");
        for (PlanificadorDisco planificador : planificadores) {
            ResultadoDisco res = planificador.planificar(cabezal, minCil, maxCil, cola, haciaMayores);
            System.out.println(res.getAlgoritmo() + " -> Orden: " + res.getOrdenAtencion() +
                    " | Desplazamiento total: " + res.getDesplazamientoTotal());

            if (mejor == null || res.getDesplazamientoTotal() < mejor.getDesplazamientoTotal()) {
                mejor = res;
            }
        }

        if (mejor != null) {
            System.out.println("\nMejor algoritmo: " + mejor.getAlgoritmo() +
                    " (desplazamiento de " + mejor.getDesplazamientoTotal() + " cilindros)");
        }
    }
}
