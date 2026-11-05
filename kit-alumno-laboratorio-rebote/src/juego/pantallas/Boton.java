package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;
import ui.Texto;

/** Botón rectangular con texto; cambia de color cuando el mouse está encima. */
public class Boton {

    private final String texto;
    private final int x;
    private final int y;
    private final int ancho;
    private final int alto;

    public Boton(String texto, int centroX, int centroY, int ancho, int alto) {
        if (texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException("El botón necesita un texto");
        }
        if (ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("Tamaño inválido: " + ancho + "x" + alto);
        }
        this.texto = texto;
        this.ancho = ancho;
        this.alto = alto;
        this.x = centroX - ancho / 2;
        this.y = centroY - alto / 2;
    }

    public boolean contiene(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + ancho && mouseY >= y && mouseY <= y + alto;
    }

    public void dibujar(Graphics2D g, boolean resaltado) {
        g.setColor(resaltado ? new Color(255, 210, 90) : new Color(60, 80, 130));
        g.fillRoundRect(x, y, ancho, alto, 18, 18);
        g.setColor(resaltado ? Color.WHITE : new Color(150, 170, 220));
        g.drawRoundRect(x, y, ancho, alto, 18, 18);
        Texto.centrado(g, texto, x + ancho / 2, y + alto / 2 + 9, 26,
                resaltado ? new Color(30, 30, 50) : Color.WHITE);
    }
}
