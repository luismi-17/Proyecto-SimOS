package com.simos.disco;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SCANDisco implements PlanificadorDisco{

    @Override
    public ResultadoDisco planificar(int cabezal, int minCil, int maxCil, int[] cola, boolean haciaMayores) {
        List<Integer> menores = new ArrayList<>();
        List<Integer> mayores = new ArrayList<>();

        for (int solicitud : cola) {
            if (solicitud < cabezal) {
                menores.add(solicitud);
            } else {
                mayores.add(solicitud);
            }
        }

        Collections.sort(menores);
        Collections.reverse(menores); // Descendente para ir bajando
        Collections.sort(mayores);  // Ascendente para ir subiendo

        List<Integer> orden = new ArrayList<>();
        int actual = cabezal;
        int desplazamiento = 0;

        if (haciaMayores) {
            for (int cil : mayores) {
                desplazamiento += Math.abs(cil - actual);
                actual = cil;
                orden.add(cil);
            }
            if (!menores.isEmpty()) {
                desplazamiento += Math.abs(maxCil - actual);
                actual = maxCil;
                for (int cil : menores) {
                    desplazamiento += Math.abs(cil - actual);
                    actual = cil;
                    orden.add(cil);
                }
            }
        } else {
            for (int cil : menores) {
                desplazamiento += Math.abs(cil - actual);
                actual = cil;
                orden.add(cil);
            }
            if (!mayores.isEmpty()) {
                desplazamiento += Math.abs(actual - minCil);
                actual = minCil;
                for (int cil : mayores) {
                    desplazamiento += Math.abs(cil - actual);
                    actual = cil;
                    orden.add(cil);
                }
            }
        }

        return new ResultadoDisco("SCAN", orden, desplazamiento);
    }
}
