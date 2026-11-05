package juego.reglas;

import estructuras.Cola;
import estructuras.MiCola;
import juego.entidades.Material;
import juego.entidades.Pelota;
import juego.pantallas.GeneradorDeEscena;
import juego.pantallas.Hud;
import motor.Entrada;
import motor.Escena;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Escena de la partida. Se apunta con el mouse y se lanza con click.
 *
 * Estructuras:
 *  - Cola: el cargador. Las pelotas salen en el orden en que se cargaron.
 *  - Lista y Pila: las usa el Mundo (entidades e historial de aciertos).
 *
 * La partida termina cuando el cargador queda vacío y ya no hay pelotas en vuelo.
 */
public class EscenaPartida extends Escena {

    private static final int PELOTAS_INICIALES = 8;
    private static final double FUERZA = 1.6;            // velocidad por píxel de distancia
    private static final double RAPIDEZ_MAXIMA = 1100;
    private static final double ESPERA_FINAL = 0.8;      // segundos antes de mostrar el fin

    private final GeneradorDeEscena generadorFin;
    private final Random azar = new Random();

    private Mundo mundo;
    private Cola<Material> cargador;
    private double lanzadorX;
    private double lanzadorY;
    private int recargasEntregadas;
    private double esperaFin;

    public EscenaPartida(GeneradorDeEscena generadorFin) {
        if (generadorFin == null) {
            throw new IllegalArgumentException("Falta la escena de fin");
        }
        this.generadorFin = generadorFin;
    }

    @Override
    public void alEntrar() {
        int ancho = (int) getContexto().getAncho();
        int alto = (int) getContexto().getAlto();
        mundo = new Mundo(ancho, alto);
        lanzadorX = 90;
        lanzadorY = mundo.getSueloY() - 40;

        cargador = new MiCola<>();
        for (int i = 0; i < PELOTAS_INICIALES; i++) {
            cargador.encolar(Material.aleatorio(azar));
        }
        recargasEntregadas = 0;
        esperaFin = 0;
    }

    @Override
    public void actualizar(double dt) {
        if (mundo == null) {
            return;
        }
        Entrada entrada = getEntrada();

        if (entrada.fuePresionada(KeyEvent.VK_ESCAPE)) {
            terminar();
            return;
        }
        if (entrada.huboClick()) {
            lanzar(entrada.getMouseX(), entrada.getMouseY());
        }

        mundo.actualizar(dt);

        // Cada acierto recarga una pelota en el cargador.
        while (recargasEntregadas < mundo.getAciertos()) {
            cargador.encolar(Material.aleatorio(azar));
            recargasEntregadas++;
        }

        if (cargador.estaVacia() && mundo.pelotasEnVuelo() == 0) {
            esperaFin += dt;
            if (esperaFin >= ESPERA_FINAL) {
                terminar();
            }
        } else {
            esperaFin = 0;
        }
    }

    private void terminar() {
        getContexto().irA(generadorFin.crear(mundo.getPuntaje()));
    }

    private void lanzar(double mouseX, double mouseY) {
        if (cargador.estaVacia()) {
            return;
        }
        double dx = mouseX - lanzadorX;
        double dy = mouseY - lanzadorY;
        double distancia = Math.hypot(dx, dy);
        if (distancia < 1) {
            return;
        }
        double rapidez = Math.min(distancia * FUERZA, RAPIDEZ_MAXIMA);
        Material material = cargador.desencolar();
        mundo.agregar(new Pelota(material, lanzadorX, lanzadorY,
                dx / distancia * rapidez, dy / distancia * rapidez));
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (mundo == null) {
            return;
        }
        mundo.dibujar(g);
        dibujarLanzador(g);

        Hud.puntaje(g, mundo.getPuntaje(), mundo.getRacha());
        mundo.dibujarHistorial(g, 20, 110);
        Hud.cargador(g, cargador, (int) getContexto().getAncho());
        Hud.ayuda(g, (int) getContexto().getAncho(), (int) getContexto().getAlto());
    }

    private void dibujarLanzador(Graphics2D g) {
        g.setColor(new Color(200, 200, 210));
        g.fillOval((int) lanzadorX - 14, (int) lanzadorY - 14, 28, 28);
        if (cargador.estaVacia()) {
            return;
        }
        Entrada entrada = getEntrada();
        double dx = entrada.getMouseX() - lanzadorX;
        double dy = entrada.getMouseY() - lanzadorY;
        double distancia = Math.hypot(dx, dy);
        if (distancia < 1) {
            return;
        }
        // Trayectoria prevista (sin contar rebotes) para la próxima pelota.
        Material siguiente = cargador.frente();
        double rapidez = Math.min(distancia * FUERZA, RAPIDEZ_MAXIMA);
        double vx = dx / distancia * rapidez;
        double vy = dy / distancia * rapidez;
        double px = lanzadorX;
        double py = lanzadorY;
        double paso = 1.0 / 60;
        double freno = Math.max(0, 1 - siguiente.getArrastre() * paso);
        g.setColor(new Color(255, 255, 255, 150));
        for (int i = 0; i < 60; i++) {
            vy += Pelota.GRAVEDAD * paso;
            vx *= freno;
            vy *= freno;
            px += vx * paso;
            py += vy * paso;
            if (py > mundo.getSueloY()) {
                break;
            }
            if (i % 3 == 0) {
                g.fillOval((int) px - 2, (int) py - 2, 4, 4);
            }
        }
    }
}
