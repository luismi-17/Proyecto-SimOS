package com.simos.core;

public interface Modulo {

        //Número de la opción en el menú (1 a 5).
        int getId();

        //Nombre que se muestra en el menú.
        String getNombre();

        //Corre el módulo. La sesión da acceso al usuario actual y a la bitácora.
        void ejecutar(Sesion sesion);

}
