package com.simos.concurrencia;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Mejora opcional: dos procesos y dos recursos.
 * Caso 1: cada proceso pide los recursos en orden contrario -> interbloqueo.
 * Caso 2: ambos piden los recursos en el mismo orden -> no hay interbloqueo.
 * Se usa tryLock con tiempo límite para detectar el bloqueo sin que el programa se congele.
 */
public class SimulacionInterbloqueo {

    private final ReentrantLock recurso1 = new ReentrantLock();
    private final ReentrantLock recurso2 = new ReentrantLock();

    public void ejecutar(boolean mismoOrden) {
        Thread a = new Thread(
                () -> usar("Proceso A", recurso1, "R1 (Impresora)", recurso2, "R2 (Archivo)"), "Proceso A");
        Thread b = mismoOrden
                ? new Thread(() -> usar("Proceso B", recurso1, "R1 (Impresora)", recurso2, "R2 (Archivo)"), "Proceso B")
                : new Thread(() -> usar("Proceso B", recurso2, "R2 (Archivo)", recurso1, "R1 (Impresora)"), "Proceso B");

        a.start();
        b.start();
        try {
            a.join();
            b.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void usar(String proceso, ReentrantLock primero, String nPrimero,
                      ReentrantLock segundo, String nSegundo) {
        boolean tienePrimero = false;
        boolean tieneSegundo = false;
        try {
            primero.lock();
            tienePrimero = true;
            System.out.println(proceso + " obtuvo " + nPrimero);
            Thread.sleep(300); // da tiempo a que el otro proceso tome su primer recurso

            System.out.println(proceso + " pide " + nSegundo + "...");
            if (segundo.tryLock(1500, TimeUnit.MILLISECONDS)) {
                tieneSegundo = true;
                System.out.println(proceso + " obtuvo " + nSegundo + " y termina su trabajo.");
                Thread.sleep(200);
            } else {
                System.out.println("  >>> INTERBLOQUEO: " + proceso + " tiene " + nPrimero
                        + " y espera " + nSegundo + ", que está tomado por el otro proceso.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (tieneSegundo) {
                segundo.unlock();
            }
            if (tienePrimero) {
                primero.unlock();
                System.out.println(proceso + " libera sus recursos.");
            }
        }
    }
}
