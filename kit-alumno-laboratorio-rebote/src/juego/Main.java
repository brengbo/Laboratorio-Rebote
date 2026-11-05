package juego;

import juego.pantallas.EscenaFin;
import juego.pantallas.EscenaMenu;
import juego.reglas.EscenaPartida;
import motor.Escena;
import motor.Ventana;

/**
 * Punto de entrada del juego: ejecutar esta clase (juego.Main).
 * Conecta las tres escenas (menú, partida, fin) con fábricas, así ninguna
 * escena conoce a la clase concreta de la siguiente y no hay dependencias circulares.
 */
public class Main {

    private static Escena menu() {
        return new EscenaMenu(puntaje -> partida());
    }

    private static Escena partida() {
        return new EscenaPartida(puntaje -> fin(puntaje));
    }

    private static Escena fin(int puntaje) {
        return new EscenaFin(puntaje, p -> partida(), p -> menu());
    }

    public static void main(String[] args) {
        Ventana.abrir("Laboratorio de rebote", 900, 600, menu());
    }
}
