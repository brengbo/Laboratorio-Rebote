package juego.pantallas;

import contratos.Actualizable;
import contratos.Dibujable;
import java.awt.Graphics2D;

/**
 * Clase abstracta de los efectos visuales temporales (chispas, textos
 * flotantes, bolas de fondo). Cada subclase decide cómo se mueve y se dibuja.
 */
public abstract class Efecto implements Actualizable, Dibujable {

    private final double duracion;
    private double edad;

    protected Efecto(double duracion) {
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración debe ser positiva: " + duracion);
        }
        this.duracion = duracion;
    }

    /** De 0 (recién creado) a 1 (a punto de desaparecer). */
    protected double getProgreso() {
        return Math.min(1, edad / duracion);
    }

    public boolean estaViva() {
        return edad < duracion;
    }

    @Override
    public void actualizar(double dt) {
        edad += dt;
        avanzar(dt);
    }

    protected abstract void avanzar(double dt);

    @Override
    public abstract void dibujar(Graphics2D g);
}
