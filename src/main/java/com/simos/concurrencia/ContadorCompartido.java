package com.simos.concurrencia;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Parte 1 del módulo: varios hilos incrementan un mismo contador.
 * Sin sincronización aparece la condición de carrera; con un candado (mutex) no.
 */
public class ContadorCompartido {

    private int valor = 0;
    private final ReentrantLock candado = new ReentrantLock();

    // Incrementar NO es atómico: son tres pasos (leer, sumar, escribir).
    // De vez en cuando cedemos la CPU justo entre leer y escribir: simula que el planificador
    // interrumpe al hilo en ese momento, algo que en un computador real ocurre de forma impredecible.
    private void incrementar() {
        int leido = valor;
        if (ThreadLocalRandom.current().nextInt(100) == 0) {
            Thread.yield();
        }
        valor = leido + 1;
    }

    // Sin sincronización: dos hilos pueden leer el mismo valor y uno de los incrementos se pierde.
    private void incrementarSinSincronizar() {
        incrementar();
    }

    // Con candado (mutex): solo un hilo a la vez ejecuta la sección crítica, así no se pierde nada.
    private void incrementarConCandado() {
        candado.lock();
        try {
            incrementar();
        } finally {
            candado.unlock();
        }
    }

    /**
     * Lanza numHilos hilos, cada uno incrementa el contador "incrementos" veces,
     * y devuelve el valor final.
     */
    public static int ejecutar(int numHilos, int incrementos, boolean sincronizado) {
        ContadorCompartido contador = new ContadorCompartido();
        CountDownLatch salida = new CountDownLatch(1); // todos arrancan al mismo tiempo
        Thread[] hilos = new Thread[numHilos];

        for (int i = 0; i < numHilos; i++) {
            Runnable tarea = () -> {
                try {
                    salida.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                for (int j = 0; j < incrementos; j++) {
                    if (sincronizado) {
                        contador.incrementarConCandado();
                    } else {
                        contador.incrementarSinSincronizar();
                    }
                }
            };
            hilos[i] = new Thread(tarea, "Hilo-" + (i + 1));
            hilos[i].start();
        }

        salida.countDown();
        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return contador.valor;
    }
}
