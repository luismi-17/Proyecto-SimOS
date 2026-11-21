package com.simos.seguridad;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class GestorUsuarios {

    public static final int MAX_INTENTOS = 3;

    private final Path archivo;
    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();

    public GestorUsuarios(String ruta) {
        this.archivo = Paths.get(ruta);
        cargar();
    }

    /** Busca un usuario sin distinguir mayúsculas. Devuelve null si no existe. */
    public Usuario buscar(String nombre) {
        if (nombre == null) {
            return null;
        }
        return usuarios.get(nombre.trim().toLowerCase());
    }

    public boolean verificarClave(Usuario usuario, String clave) {
        return usuario.getHashClave().equals(sha256(clave));
    }

    /**
     * Cuenta un intento fallido. Al llegar a MAX_INTENTOS bloquea al usuario
     * (y queda guardado en el archivo). Devuelve true si quedó bloqueado.
     */
    public boolean registrarFallo(Usuario usuario) {
        if (usuario.sumarFallo() >= MAX_INTENTOS) {
            usuario.setBloqueado(true);
            guardar();
            return true;
        }
        return false;
    }

    public void desbloquear(Usuario usuario) {
        usuario.setBloqueado(false);
        usuario.reiniciarFallos();
        guardar();
    }

    public Collection<Usuario> listar() {
        return new ArrayList<>(usuarios.values());
    }

    public static String sha256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no está disponible", e);
        }
    }

    private void cargar() {
        if (Files.exists(archivo)) {
            try {
                for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8)) {
                    leerLinea(linea);
                }
            } catch (IOException e) {
                System.out.println("[Aviso] No se pudo leer " + archivo + ": " + e.getMessage());
            }
        }
        if (usuarios.isEmpty()) {
            crearUsuariosIniciales();
            guardar();
        }
    }

    private void leerLinea(String linea) {
        linea = linea.trim();
        if (linea.isEmpty() || linea.startsWith("#")) {
            return;
        }
        String[] p = linea.split(";");
        if (p.length < 4) {
            System.out.println("[Aviso] Línea ignorada en " + archivo + " (formato incorrecto).");
            return;
        }
        try {
            Usuario u = new Usuario(p[0].trim(), p[1].trim(),
                    Rol.valueOf(p[2].trim().toUpperCase()), "BLOQUEADO".equalsIgnoreCase(p[3].trim()));
            usuarios.put(u.getNombre().toLowerCase(), u);
        } catch (IllegalArgumentException e) {
            System.out.println("[Aviso] Línea ignorada en " + archivo + " (rol no válido).");
        }
    }

    private void crearUsuariosIniciales() {
        agregar("admin", "admin123", Rol.ADMINISTRADOR);
        agregar("maria", "maria123", Rol.USUARIO);
        agregar("carlos", "carlos123", Rol.USUARIO);
    }

    private void agregar(String nombre, String clave, Rol rol) {
        usuarios.put(nombre.toLowerCase(), new Usuario(nombre, sha256(clave), rol, false));
    }

    private void guardar() {
        List<String> lineas = new ArrayList<>();
        lineas.add("# usuario;hash_sha256;rol;estado");
        for (Usuario u : usuarios.values()) {
            lineas.add(u.getNombre() + ";" + u.getHashClave() + ";" + u.getRol().name() + ";"
                    + (u.isBloqueado() ? "BLOQUEADO" : "ACTIVO"));
        }
        try {
            Path carpeta = archivo.getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            Files.write(archivo, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("[Aviso] No se pudo guardar " + archivo + ": " + e.getMessage());
        }
    }
}
