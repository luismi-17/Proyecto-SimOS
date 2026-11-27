package com.simos.concurrencia;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Parte 2 del módulo: productor-consumidor con buffer de tamaño fijo.
 * - espaciosLibres: semáforo que cuenta los huecos del buffer (el productor espera si está lleno).
 * - espaciosOcupados: semáforo que cuenta los elementos (el consumidor espera si está vacío).
 * - candado: protege el buffer mientras un hilo lo modifica.
 */
public class ProductorConsumidor {

    private final int capacidad;
    private final Queue<Integer> buffer = new ArrayDeque<>();
    private final Semaphore espaciosLibres;
    private final Semaphore espaciosOcupados = new Semaphore(0);
    private final ReentrantLock candado = new ReentrantLock();
    private int ultimoElemento = 0; // numera los elementos; se toca solo con el candado tomado

    public ProductorConsumidor(int capacidad) {
        this.capacidad = capacidad;
        this.espaciosLibres = new Semaphore(capacidad);
    }

    public void ejecutar(int totalElementos, int numProductores, int numConsumidores) {
        Thread[] hilos = new Thread[numProductores + numConsumidores];
        int k = 0;

        for (int i = 0; i < numProductores; i++) {
            int cuota = repartir(totalElementos, numProductores, i);
            hilos[k++] = new Thread(() -> producir(cuota), "Productor-" + (i + 1));
        }
        for (int i = 0; i < numConsumidores; i++) {
            int cuota = repartir(totalElementos, numConsumidores, i);
            hilos[k++] = new Thread(() -> consumir(cuota), "Consumidor-" + (i + 1));
        }

        for (Thread hilo : hilos) {
            hilo.start();
        }
        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Reparte el total entre n hilos (los primeros reciben el resto si no es exacto).
    private int repartir(int total, int n, int indice) {
        return total / n + (indice < total % n ? 1 : 0);
    }

    private void producir(int cuota) {
        String nombre = Thread.currentThread().getName();
        try {
            for (int i = 0; i < cuota; i++) {
                dormir(40, 120);
                espaciosLibres.acquire();      // espera si el buffer está lleno
                candado.lock();
                try {
                    int elemento = ++ultimoElemento;
                    buffer.add(elemento);
                    System.out.printf("%-13s PRODUCE  elemento %2d | buffer %-23s (%d/%d)%n",
                            nombre, elemento, buffer, buffer.size(), capacidad);
                } finally {
                    candado.unlock();
                }
                espaciosOcupados.release();    // avisa que hay un elemento más
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void consumir(int cuota) {
        String nombre = Thread.currentThread().getName();
        try {
            for (int i = 0; i < cuota; i++) {
                espaciosOcupados.acquire();    // espera si el buffer está vacío
                candado.lock();
                try {
                    int elemento = buffer.poll();
                    System.out.printf("%-13s CONSUME  elemento %2d | buffer %-23s (%d/%d)%n",
                            nombre, elemento, buffer, buffer.size(), capacidad);
                } finally {
                    candado.unlock();
                }
                espaciosLibres.release();      // avisa que hay un hueco más
                dormir(80, 200);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void dormir(int minMs, int maxMs) {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(minMs, maxMs + 1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
