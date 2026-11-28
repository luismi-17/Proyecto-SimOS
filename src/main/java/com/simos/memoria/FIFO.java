package com.simos.memoria;

import java.util.Arrays;

public class FIFO implements AlgoritmoReemplazo{
    @Override
    public String getNombre() {
        return "FIFO";
    }

    @Override
    public ResultadoMemoria simular(int[] referencias, int numMarcos) {
        ResultadoMemoria resultado = new ResultadoMemoria(getNombre(), numMarcos);
        int[] marcos = new int[numMarcos];
        Arrays.fill(marcos, -1);
        int siguiente = 0; // posición del marco que se reemplaza primero

        for (int referencia : referencias) {
            boolean fallo = buscar(marcos, referencia) == -1;
            int reemplazada = -1;

            if (fallo) {
                reemplazada = marcos[siguiente];
                marcos[siguiente] = referencia;
                siguiente = (siguiente + 1) % numMarcos;
            }
            resultado.agregar(new PasoMemoria(referencia, marcos, fallo, reemplazada));
        }
        return resultado;
    }

    //Devuelve la posición de la página en los marcos, o -1 si no está
    private int buscar(int[] marcos, int pagina) {
        for (int i = 0; i < marcos.length; i++) {
            if (marcos[i] == pagina) {
                return i;
            }
        }
        return -1;
    }
}
