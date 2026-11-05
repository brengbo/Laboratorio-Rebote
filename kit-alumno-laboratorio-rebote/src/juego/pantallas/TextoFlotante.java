package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;
import ui.Texto;

/** Texto que sube y se desvanece (por ejemplo, los puntos de un acierto). */
public class TextoFlotante extends Efecto {

    private final double x;
    private double y;
    private final String texto;
    private final Color color;

    public TextoFlotante(double x, double y, String texto, Color color) {
        super(1.0);
        if (texto == null || color == null) {
            throw new IllegalArgumentException("Faltan el texto o el color");
        }
        this.x = x;
        this.y = y;
        this.texto = texto;
        this.color = color;
    }

    @Override
    protected void avanzar(double dt) {
        y -= 50 * dt;
    }

    @Override
    public void dibujar(Graphics2D g) {
        int alfa = (int) (255 * (1 - getProgreso()));
        Texto.centrado(g, texto, (int) x, (int) y, 26,
                new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, alfa)));
    }
}
