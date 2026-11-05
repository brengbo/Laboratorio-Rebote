package juego.pantallas;

import estructuras.Cola;
import estructuras.MiPila;
import estructuras.Pila;
import juego.entidades.Material;
import java.awt.Color;
import java.awt.Graphics2D;
import ui.Texto;

/**
 * Elementos del HUD de la partida. Son métodos estáticos que reciben los
 * datos ya calculados, así este paquete no depende de las reglas.
 */
public final class Hud {

    private static final int HISTORIAL_VISIBLE = 6;
    private static final int CARGADOR_VISIBLE = 10;

    private Hud() { }

    public static void puntaje(Graphics2D g, int puntaje, int racha) {
        g.setColor(new Color(0, 0, 0, 90));
        g.fillRoundRect(10, 8, 250, racha > 0 ? 70 : 46, 14, 14);
        Texto.izquierda(g, "Puntaje: " + puntaje, 22, 40, 28, Color.WHITE);
        if (racha > 0) {
            Texto.izquierda(g, "Racha x" + (racha + 1), 22, 68, 18, new Color(255, 210, 90));
        }
    }

    /**
     * Últimos aciertos: se desapila a una pila auxiliar y se vuelve a apilar
     * para dejar el historial intacto.
     */
    public static void historial(Graphics2D g, Pila<Material> historial, int aciertos, int x, int y) {
        Texto.izquierda(g, "Aciertos: " + aciertos, x, y, 18, Color.WHITE);
        Pila<Material> auxiliar = new MiPila<>();
        int mostrados = 0;
        while (!historial.estaVacia() && mostrados < HISTORIAL_VISIBLE) {
            Material m = historial.desapilar();
            g.setColor(m.getColor());
            g.fillOval(x + mostrados * 26, y + 10, 20, 20);
            auxiliar.apilar(m);
            mostrados++;
        }
        while (!auxiliar.estaVacia()) {
            historial.apilar(auxiliar.desapilar());
        }
    }

    /**
     * Cargador: se desencola y se vuelve a encolar cada pelota, así el
     * orden de salida queda exactamente igual.
     */
    public static void cargador(Graphics2D g, Cola<Material> cargador, int ancho) {
        int n = cargador.tamanio();
        String siguiente = n == 0 ? "-" : cargador.frente().getNombre();
        g.setColor(new Color(0, 0, 0, 90));
        g.fillRoundRect(ancho - 330, 8, 320, 78, 14, 14);
        Texto.derecha(g, "Cargador: " + n + "   Siguiente: " + siguiente, ancho - 22, 36, 18, Color.WHITE);
        for (int i = 0; i < n; i++) {
            Material m = cargador.desencolar();
            if (i < CARGADOR_VISIBLE) {
                int radio = i == 0 ? 11 : 8;
                g.setColor(m.getColor());
                g.fillOval(ancho - 34 - i * 24 - radio, 62 - radio, radio * 2, radio * 2);
            }
            cargador.encolar(m);
        }
    }

    public static void ayuda(Graphics2D g, int ancho, int alto) {
        Texto.centrado(g, "Click: lanzar   |   ESC: terminar la partida", ancho / 2, alto - 12, 16,
                new Color(255, 255, 255, 170));
    }
}
