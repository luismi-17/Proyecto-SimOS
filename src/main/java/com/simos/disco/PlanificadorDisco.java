package com.simos.disco;

public interface PlanificadorDisco {

    ResultadoDisco planificar(int cabezal, int minCil, int maxCil, int[] cola, boolean haciaMayores);
}
