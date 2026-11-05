package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;

/** Partícula que sale disparada y cae con gravedad mientras se desvanece. */
public class Chispa extends Efecto {

    private double x;
    private double y;
    private double vx;
    private double vy;
    private final Color color;

    public Chispa(double x, double y, double vx, double vy, Color color, double duracion) {
        super(duracion);
        if (color == null) {
            throw new IllegalArgumentException("La chispa necesita un color");
        }
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.color = color;
    }

    @Override
    protected void avanzar(double dt) {
        vy += 600 * dt;
        x += vx * dt;
        y += vy * dt;
    }

    @Override
    public void dibujar(Graphics2D g) {
        int alfa = (int) (255 * (1 - getProgreso()));
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, alfa)));
        g.fillOval((int) x - 3, (int) y - 3, 6, 6);
    }
}
