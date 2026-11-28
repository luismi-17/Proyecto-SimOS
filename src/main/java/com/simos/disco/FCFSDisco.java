package com.simos.disco;

import java.util.ArrayList;
import java.util.List;

public class FCFSDisco implements PlanificadorDisco{

    @Override
    public ResultadoDisco planificar(int cabezal, int minCil, int maxCil, int[] cola, boolean haciaMayores) {
        List<Integer> orden = new ArrayList<>();
        int actual = cabezal;
        int desplazamiento = 0;

        for (int solicitud : cola) {
            desplazamiento += Math.abs(solicitud - actual);
            actual = solicitud;
            orden.add(solicitud);
        }

        return new ResultadoDisco("FCFS", orden, desplazamiento);
    }
}
