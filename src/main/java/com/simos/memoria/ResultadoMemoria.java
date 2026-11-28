package com.simos.memoria;

import java.util.ArrayList;
import java.util.List;

public class ResultadoMemoria {

    private final String algoritmo;
    private final int numMarcos;
    private final List<PasoMemoria> pasos = new ArrayList<>();
    private int fallos;

    public ResultadoMemoria(String algoritmo, int numMarcos) {
        this.algoritmo = algoritmo;
        this.numMarcos = numMarcos;
    }

    public void agregar(PasoMemoria paso) {
        pasos.add(paso);
        if (paso.isFallo()) {
            fallos++;
        }
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public int getNumMarcos() {
        return numMarcos;
    }

    public List<PasoMemoria> getPasos() {
        return pasos;
    }

    public int getFallos() {
        return fallos;
    }

    public int getAciertos() {
        return pasos.size() - fallos;
    }
}