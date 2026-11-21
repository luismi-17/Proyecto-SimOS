package com.simos.cpu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultadoPlanificacion {
    private final List<String> gantt = new ArrayList<>();
    private final Map<String, Integer> finalizacion = new HashMap<>();

    void agregarTramo(String proceso, int inicio, int fin) {
        gantt.add(proceso + "(" + inicio + "-" + fin + ")");
    }

    void registrarFin(String proceso, int tiempo) {
        finalizacion.put(proceso, tiempo);
    }

    public List<String> getGantt() { return gantt; }
    public int getFinalizacion(String proceso) { return finalizacion.get(proceso); }
    public int totalFinalizados() { return finalizacion.size(); }
}