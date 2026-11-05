package juego.pantallas;

import motor.Escena;

/**
 * Fabrica la escena siguiente. Así una escena no necesita conocer a la clase
 * concreta de la otra, y se evitan dependencias circulares entre paquetes.
 * El puntaje se usa al pasar a la pantalla de fin; las demás transiciones lo ignoran.
 */
public interface GeneradorDeEscena {
    Escena crear(int puntaje);
}
