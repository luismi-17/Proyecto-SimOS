package com.simos.seguridad;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
  Bitácora de auditoría en archivo de texto. Cada línea:
  fecha | hora | usuario | acción | PERMITIDO o DENEGADO
 */
public class Bitacora {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final String ENCABEZADO = "FECHA | HORA | USUARIO | ACCIÓN | RESULTADO";

    private final Path archivo;

    public Bitacora(String ruta) {
        this.archivo = Paths.get(ruta);
        try {
            Path carpeta = archivo.getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            if (!Files.exists(archivo)) {
                Files.write(archivo, Collections.singletonList(ENCABEZADO), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            System.out.println("[Aviso] No se pudo crear la bitácora: " + e.getMessage());
        }
    }

    public synchronized void registrar(String usuario, String accion, boolean permitida) {
        LocalDateTime ahora = LocalDateTime.now();
        String linea = ahora.format(FECHA) + " | " + ahora.format(HORA) + " | " + limpiar(usuario)
                + " | " + limpiar(accion) + " | " + (permitida ? "PERMITIDO" : "DENEGADO");
        try (BufferedWriter w = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(linea);
            w.newLine();
        } catch (IOException e) {
            System.out.println("[Aviso] No se pudo escribir en la bitácora: " + e.getMessage());
        }
    }

    public List<String> leer() {
        try {
            return Files.readAllLines(archivo, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public String getRuta() {
        return archivo.toString();
    }

    //Evita que un texto escrito por el usuario rompa el formato o falsifique otra línea
    private static String limpiar(String texto) {
        return texto == null ? "" : texto.replaceAll("[\\r\\n|]+", " ").trim();
    }
}