package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;

/** Bola decorativa que rebota por la pantalla (se usa de fondo en el menú y en el fin). */
public class BolaFondo extends Efecto {

    private double x;
    private double y;
    private double vx;
    private double vy;
    private final double radio;
    private final double ancho;
    private final double suelo;
    private final Color color;

    public BolaFondo(double x, double y, double vx, double radio, Color color,
                     double ancho, double suelo) {
        super(14);
        if (radio <= 0 || ancho <= 0 || suelo <= 0 || color == null) {
            throw new IllegalArgumentException("Datos inválidos para la bola de fondo");
        }
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.radio = radio;
        this.ancho = ancho;
        this.suelo = suelo;
        this.color = color;
    }

    @Override
    protected void avanzar(double dt) {
        vy += 900 * dt;
        x += vx * dt;
        y += vy * dt;
        if (y >= suelo - radio) {
            y = suelo - radio;
            vy = -vy * 0.9;
            if (Math.abs(vy) < 200) {
                vy = -600;
            }
        }
        if (x <= radio) {
            x = radio;
            vx = Math.abs(vx);
        } else if (x >= ancho - radio) {
            x = ancho - radio;
            vx = -Math.abs(vx);
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 90));
        g.fillOval((int) (x - radio), (int) (y - radio), (int) radio * 2, (int) radio * 2);
    }
}
