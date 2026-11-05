package juego.entidades;

import java.awt.Color;
import java.awt.Graphics2D;

/** Bumper fijo: las pelotas rebotan contra él (con un coeficiente propio). */
public class Obstaculo extends Entidad implements Colisionable {

    private final double radio;
    private final double coeficiente;

    public Obstaculo(double x, double y, double radio) {
        this(x, y, radio, 1.1);
    }

    public Obstaculo(double x, double y, double radio, double coeficiente) {
        super(x, y);
        if (radio <= 0) {
            throw new IllegalArgumentException("El radio debe ser positivo: " + radio);
        }
        if (coeficiente < 0 || coeficiente > 2) {
            throw new IllegalArgumentException("Coeficiente fuera de [0, 2]: " + coeficiente);
        }
        this.radio = radio;
        this.coeficiente = coeficiente;
    }

    @Override
    public double getRadio() { return radio; }

    public double getCoeficiente() { return coeficiente; }

    @Override
    public void actualizar(double dt) {
        // Es fijo: no cambia con el tiempo.
    }

    @Override
    public void afectarA(Pelota pelota) {
        pelota.rebotarContra(this, coeficiente);
    }

    @Override
    public void dibujar(Graphics2D g) {
        int r = (int) radio;
        g.setColor(new Color(90, 100, 120));
        g.fillOval((int) (getX() - r), (int) (getY() - r), r * 2, r * 2);
        g.setColor(new Color(170, 185, 210));
        g.drawOval((int) (getX() - r), (int) (getY() - r), r * 2, r * 2);
    }
}
