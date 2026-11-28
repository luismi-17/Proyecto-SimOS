package com.simos.disco;

import java.util.List;

public class ResultadoDisco {

    private final String algoritmo;
    private final List<Integer> ordenAtencion;
    private final int desplazamientoTotal;

    public ResultadoDisco(String algoritmo, List<Integer> ordenAtencion, int desplazamientoTotal) {
        this.algoritmo = algoritmo;
        this.ordenAtencion = ordenAtencion;
        this.desplazamientoTotal = desplazamientoTotal;
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public List<Integer> getOrdenAtencion() {
        return ordenAtencion;
    }

    public int getDesplazamientoTotal() {
        return desplazamientoTotal;
    }
}
