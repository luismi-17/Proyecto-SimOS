package com.simos.memoria;

import java.util.Arrays;

public class LRU implements AlgoritmoReemplazo{

    @Override
    public String getNombre() {
        return "LRU";
    }

    @Override
    public ResultadoMemoria simular(int[] referencias, int numMarcos) {
        ResultadoMemoria resultado = new ResultadoMemoria(getNombre(), numMarcos);
        int[] marcos = new int[numMarcos];
        int[] ultimoUso = new int[numMarcos];
        Arrays.fill(marcos, -1);

        for (int paso = 0; paso < referencias.length; paso++) {
            int referencia = referencias[paso];
            int posicion = buscar(marcos, referencia);
            boolean fallo = posicion == -1;
            int reemplazada = -1;

            if (fallo) {
                posicion = elegirMarco(marcos, ultimoUso);
                reemplazada = marcos[posicion];
                marcos[posicion] = referencia;
            }
            ultimoUso[posicion] = paso; // tanto en fallo como en acierto
            resultado.agregar(new PasoMemoria(referencia, marcos, fallo, reemplazada));
        }
        return resultado;
    }

    // Un marco libre si lo hay; si no, el de uso más antiguo
    private int elegirMarco(int[] marcos, int[] ultimoUso) {
        for (int i = 0; i < marcos.length; i++) {
            if (marcos[i] == -1) {
                return i;
            }
        }
        int masAntiguo = 0;
        for (int i = 1; i < marcos.length; i++) {
            if (ultimoUso[i] < ultimoUso[masAntiguo]) {
                masAntiguo = i;
            }
        }
        return masAntiguo;
    }

    // Devuelve la posición de la página en los marcos, o -1 si no está
    private int buscar(int[] marcos, int pagina) {
        for (int i = 0; i < marcos.length; i++) {
            if (marcos[i] == pagina) {
                return i;
            }
        }
        return -1;
    }
}
