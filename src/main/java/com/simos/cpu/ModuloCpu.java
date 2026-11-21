package com.simos.cpu;

import com.simos.core.Modulo;
import com.simos.core.Sesion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ModuloCpu implements Modulo {

    private final Scanner sc;

    public ModuloCpu(Scanner sc) {
        this.sc = sc;
    }

    @Override
    public int getId() {
        return 1; // opción 1 del menú
    }

    @Override
    public String getNombre() {
        return "Planificación de CPU";
    }

    @Override
    public void ejecutar(Sesion sesion) {
        Locale.setDefault(Locale.US);
        System.out.println("\n--- Módulo 1: Planificación de CPU ---");
        List<Proceso> procesos = pedirProcesos();
        int quantum = leerEntero("Quantum para Round Robin: ", 1);

        double esperaFcfs = mostrar("FCFS", procesos, PlanificadorCpu.fcfs(procesos));
        double esperaRr = mostrar("Round Robin (quantum = " + quantum + ")", procesos,
                PlanificadorCpu.roundRobin(procesos, quantum));

        System.out.println("\n=== Comparación ===");
        System.out.printf("FCFS: %.2f | Round Robin: %.2f%n", esperaFcfs, esperaRr);
        if (esperaFcfs < esperaRr) System.out.println("Mejor algoritmo: FCFS (menor espera promedio)");
        else if (esperaRr < esperaFcfs) System.out.println("Mejor algoritmo: Round Robin (menor espera promedio)");
        else System.out.println("Empate: ambos tienen la misma espera promedio");
    }

    private List<Proceso> pedirProcesos() {
        System.out.print("¿Usar datos de prueba P1 a P5? (s/n): ");
        List<Proceso> ps = new ArrayList<>();
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            ps.add(new Proceso("P1", 0, 8));
            ps.add(new Proceso("P2", 1, 4));
            ps.add(new Proceso("P3", 2, 9));
            ps.add(new Proceso("P4", 3, 5));
            ps.add(new Proceso("P5", 6, 2));
            return ps;
        }
        int n = leerEntero("Número de procesos: ", 1);
        for (int i = 1; i <= n; i++) {
            System.out.print("Proceso " + i + " - nombre: ");
            String nombre = sc.nextLine().trim();
            int llegada = leerEntero("  Tiempo de llegada: ", 0);
            int rafaga = leerEntero("  Ráfaga de CPU: ", 1);
            ps.add(new Proceso(nombre, llegada, rafaga));
        }
        return ps;
    }

    private double mostrar(String titulo, List<Proceso> procesos, ResultadoPlanificacion r) {
        System.out.println("\n=== " + titulo + " ===");
        System.out.println("Gantt: |" + String.join("|", r.getGantt()) + "|");
        System.out.printf("%-9s%-9s%-9s%-14s%-9s%s%n",
                "Proceso", "Llegada", "Ráfaga", "Finalización", "Retorno", "Espera");
        for (Proceso p : procesos) {
            int fin = r.getFinalizacion(p.getNombre());
            int retorno = fin - p.getLlegada();
            System.out.printf("%-9s%-9d%-9d%-14d%-9d%d%n", p.getNombre(), p.getLlegada(),
                    p.getRafaga(), fin, retorno, retorno - p.getRafaga());
        }
        double promedio = PlanificadorCpu.esperaPromedio(procesos, r);
        System.out.printf("Tiempo promedio de espera: %.2f%n", promedio);
        return promedio;
    }

    private int leerEntero(String mensaje, int minimo) {
        while (true) {
            System.out.print(mensaje);
            try {
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v >= minimo) return v;
                System.out.println("El valor debe ser mayor o igual a " + minimo + ".");
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }

    // TEMPORAL: solo para probar el módulo solo. Bórralo al conectarlo al menú.
    public static void main(String[] args) {
        new ModuloCpu(new Scanner(System.in)).ejecutar(null);
    }
}