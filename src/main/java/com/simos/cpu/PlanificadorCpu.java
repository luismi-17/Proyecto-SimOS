package com.simos.cpu;

import java.util.*;

public class PlanificadorCpu {

    public static ResultadoPlanificacion fcfs(List<Proceso> procesos) {
        List<Proceso> orden = new ArrayList<>(procesos);
        orden.sort(Comparator.comparingInt(Proceso::getLlegada));
        ResultadoPlanificacion r = new ResultadoPlanificacion();
        int t = 0;
        for (Proceso p : orden) {
            t = Math.max(t, p.getLlegada());
            r.agregarTramo(p.getNombre(), t, t + p.getRafaga());
            t += p.getRafaga();
            r.registrarFin(p.getNombre(), t);
        }
        return r;
    }

    public static ResultadoPlanificacion roundRobin(List<Proceso> procesos, int quantum) {
        List<Proceso> pendientes = new ArrayList<>(procesos);
        pendientes.sort(Comparator.comparingInt(Proceso::getLlegada));
        Map<String, Integer> restante = new HashMap<>();
        for (Proceso p : procesos) restante.put(p.getNombre(), p.getRafaga());

        Deque<Proceso> cola = new ArrayDeque<>();
        ResultadoPlanificacion r = new ResultadoPlanificacion();
        int t = 0, i = 0;

        while (r.totalFinalizados() < procesos.size()) {
            while (i < pendientes.size() && pendientes.get(i).getLlegada() <= t) cola.add(pendientes.get(i++));
            if (cola.isEmpty()) {            // CPU ociosa hasta la próxima llegada
                t = pendientes.get(i).getLlegada();
                continue;
            }
            Proceso p = cola.poll();
            int ejecuta = Math.min(quantum, restante.get(p.getNombre()));
            r.agregarTramo(p.getNombre(), t, t + ejecuta);
            t += ejecuta;
            restante.put(p.getNombre(), restante.get(p.getNombre()) - ejecuta);

            // los que llegaron durante la ejecución entran antes que el expulsado
            while (i < pendientes.size() && pendientes.get(i).getLlegada() <= t) cola.add(pendientes.get(i++));
            if (restante.get(p.getNombre()) > 0) cola.add(p);
            else r.registrarFin(p.getNombre(), t);
        }
        return r;
    }

    public static double esperaPromedio(List<Proceso> procesos, ResultadoPlanificacion r) {
        double suma = 0;
        for (Proceso p : procesos) {
            int retorno = r.getFinalizacion(p.getNombre()) - p.getLlegada();
            suma += retorno - p.getRafaga();
        }
        return suma / procesos.size();
    }
}