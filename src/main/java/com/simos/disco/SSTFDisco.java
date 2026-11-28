package com.simos.disco;

import java.util.ArrayList;
import java.util.List;

public class SSTFDisco implements PlanificadorDisco{

    @Override
    public ResultadoDisco planificar(int cabezal, int minCil, int maxCil, int[] cola, boolean haciaMayores) {
        List<Integer> orden = new ArrayList<>();
        boolean[] visitado = new boolean[cola.length];
        int actual = cabezal;
        int desplazamiento = 0;

        for (int i = 0; i < cola.length; i++) {
            int indiceMasCercano = -1;
            int menorDistancia = Integer.MAX_VALUE;

            for (int j = 0; j < cola.length; j++) {
                if (!visitado[j]) {
                    int distancia = Math.abs(cola[j] - actual);
                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        indiceMasCercano = j;
                    }
                }
            }

            if (indiceMasCercano != -1) {
                visitado[indiceMasCercano] = true;
                int siguiente = cola[indiceMasCercano];
                desplazamiento += menorDistancia;
                actual = siguiente;
                orden.add(siguiente);
            }
        }

        return new ResultadoDisco("SSTF", orden, desplazamiento);
    }
}
