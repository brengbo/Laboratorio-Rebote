package verificador;

import estructuras.MiCola;
import estructuras.MiLista;
import estructuras.MiPila;

public class PruebaMisEstructuras {

    // Punto de entrada
    public static void main(String[] args) {

        // Lista: descomentar y reemplazar por su clase
        VerificadorEstructuras.verificarLista(new MiLista<Integer>());

        // Pila: descomentar y reemplazar por su clase
        VerificadorEstructuras.verificarPila(new MiPila<Integer>());

        // Cola: descomentar y reemplazar por su clase
        VerificadorEstructuras.verificarCola(new MiCola<Integer>());

    }
}
