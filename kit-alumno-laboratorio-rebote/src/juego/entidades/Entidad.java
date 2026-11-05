package juego.entidades;

import contratos.Actualizable;
import contratos.Dibujable;
import java.awt.Graphics2D;

/**
 * Clase abstracta base de todo lo que vive en el mundo.
 * Las subclases (Pelota, Aro, Obstaculo) deciden cómo se actualizan,
 * cómo se dibujan y qué le hacen a una pelota que las toca.
 */
public abstract class Entidad implements Actualizable, Dibujable {

    private double x;
    private double y;
    private boolean activa = true;
    private double anchoArea = 900;
    private double sueloY = 560;

    protected Entidad(double x, double y) {
        setPosicion(x, y);
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void setPosicion(double x, double y) {
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new IllegalArgumentException("Posición inválida: " + x + ", " + y);
        }
        this.x = x;
        this.y = y;
    }

    public boolean isActiva() { return activa; }

    /** Marca la entidad para que el mundo la retire. */
    public void desactivar() { activa = false; }

    /** El mundo le avisa el tamaño del área de juego al agregarla. */
    public void setArea(double ancho, double sueloY) {
        if (ancho <= 0 || sueloY <= 0) {
            throw new IllegalArgumentException("Área inválida: " + ancho + ", " + sueloY);
        }
        this.anchoArea = ancho;
        this.sueloY = sueloY;
    }

    protected double getAnchoArea() { return anchoArea; }
    protected double getSueloY() { return sueloY; }

    /** Polimorfismo en lugar de instanceof: solo las pelotas son proyectiles. */
    public boolean esProyectil() { return false; }

    /** Se llama por cada par de entidades. Solo la Pelota hace algo (ver Pelota). */
    public void interactuar(Entidad otra) { }

    /** Qué le hace esta entidad a una pelota que la toca. Por defecto, nada. */
    public void afectarA(Pelota pelota) { }

    @Override
    public abstract void actualizar(double dt);

    @Override
    public abstract void dibujar(Graphics2D g);
}
