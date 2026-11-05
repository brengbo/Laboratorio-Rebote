package juego.pantallas;

import contratos.Actualizable;
import contratos.Dibujable;
import estructuras.Lista;
import estructuras.MiLista;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * Administra los efectos visuales. Usa la Lista propia: los efectos nuevos
 * se agregan al final y los que terminaron se eliminan de atrás para adelante.
 */
public class Efectos implements Actualizable, Dibujable {

    private final Lista<Efecto> efectos = new MiLista<>();
    private final Random azar = new Random();

    public void agregar(Efecto efecto) {
        if (efecto == null) {
            throw new IllegalArgumentException("No se puede agregar un efecto nulo");
        }
        efectos.agregar(efecto);
    }

    public int cantidad() {
        return efectos.tamanio();
    }

    /** Chispas en todas direcciones. */
    public void explosion(double x, double y, Color color, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            double angulo = azar.nextDouble() * Math.PI * 2;
            double rapidez = 80 + azar.nextDouble() * 220;
            agregar(new Chispa(x, y, Math.cos(angulo) * rapidez, Math.sin(angulo) * rapidez,
                    color, 0.5 + azar.nextDouble() * 0.5));
        }
    }

    public void textoFlotante(double x, double y, String texto, Color color) {
        agregar(new TextoFlotante(x, y, texto, color));
    }

    public void bolaDeFondo(double ancho, double suelo, Color color) {
        agregar(new BolaFondo(azar.nextDouble() * ancho, -20, (azar.nextDouble() - 0.5) * 300,
                10 + azar.nextDouble() * 18, color, ancho, suelo));
    }

    @Override
    public void actualizar(double dt) {
        for (int i = 0; i < efectos.tamanio(); i++) {
            efectos.obtener(i).actualizar(dt);
        }
        for (int i = efectos.tamanio() - 1; i >= 0; i--) {
            if (!efectos.obtener(i).estaViva()) {
                efectos.eliminar(i);
            }
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        for (int i = 0; i < efectos.tamanio(); i++) {
            efectos.obtener(i).dibujar(g);
        }
    }
}
