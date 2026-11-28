package com.simos.memoria;

public class PasoMemoria {

    private final int referencia;
    private final int[] marcos;
    private final boolean fallo;
    private final int paginaReemplazada;


    public PasoMemoria(int referencia, int[] marcos, boolean fallo, int paginaReemplazada) {
        this.referencia = referencia;
        this.marcos = marcos.clone();
        this.fallo = fallo;
        this.paginaReemplazada = paginaReemplazada;
    }

    public int getReferencia() {
        return referencia;
    }

    public int[] getMarcos() {
        return marcos.clone();
    }

    public boolean isFallo() {
        return fallo;
    }

    public int getPaginaReemplazada() {
        return paginaReemplazada;
    }
}