package juego.pantallas;

import juego.entidades.Material;
import motor.Entrada;
import motor.Escena;
import ui.Texto;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

/** Menú inicial: título, instrucciones, leyenda de materiales y dos botones. */
public class EscenaMenu extends Escena {

    private final GeneradorDeEscena generadorPartida;
    private final Efectos fondo = new Efectos();
    private final Random azar = new Random();
    private Boton jugar;
    private Boton salir;
    private double reloj;
    private double proximaBola;

    public EscenaMenu(GeneradorDeEscena generadorPartida) {
        if (generadorPartida == null) {
            throw new IllegalArgumentException("Falta la escena de partida");
        }
        this.generadorPartida = generadorPartida;
    }

    @Override
    public void alEntrar() {
        int ancho = (int) getContexto().getAncho();
        int alto = (int) getContexto().getAlto();
        jugar = new Boton("JUGAR", ancho / 2 - 110, alto - 90, 190, 54);
        salir = new Boton("SALIR", ancho / 2 + 110, alto - 90, 190, 54);
    }

    @Override
    public void actualizar(double dt) {
        if (jugar == null) {
            return;
        }
        int ancho = (int) getContexto().getAncho();
        int alto = (int) getContexto().getAlto();
        reloj += dt;
        proximaBola -= dt;
        if (proximaBola <= 0) {
            proximaBola = 0.7;
            fondo.bolaDeFondo(ancho, alto - 20, Material.aleatorio(azar).getColor());
        }
        fondo.actualizar(dt);

        Entrada entrada = getEntrada();
        boolean clickJugar = entrada.huboClick() && jugar.contiene(entrada.getMouseX(), entrada.getMouseY());
        boolean clickSalir = entrada.huboClick() && salir.contiene(entrada.getMouseX(), entrada.getMouseY());
        if (clickJugar || entrada.fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(generadorPartida.crear(0));
        } else if (clickSalir || entrada.fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().salir();
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (jugar == null) {
            return;
        }
        int ancho = (int) getContexto().getAncho();
        int alto = (int) getContexto().getAlto();
        Entrada entrada = getEntrada();

        g.setColor(new Color(18, 24, 48));
        g.fillRect(0, 0, ancho, alto);
        fondo.dibujar(g);

        int pulso = (int) (4 * Math.sin(reloj * 3));
        Texto.centrado(g, "LABORATORIO DE REBOTE", ancho / 2, 110 + pulso, 52, Color.WHITE);
        Texto.centrado(g, "Embocá las pelotas en el aro antes de quedarte sin cargador",
                ancho / 2, 160, 20, new Color(200, 210, 230));

        Texto.centrado(g, "Apuntá con el mouse y lanzá con click. Cada acierto recarga una pelota.",
                ancho / 2, 230, 18, new Color(200, 210, 230));
        Texto.centrado(g, "Dos aciertos seguidos con el mismo material dan bonus de racha.",
                ancho / 2, 258, 18, new Color(255, 210, 90));

        Texto.centrado(g, "MATERIALES", ancho / 2, 320, 22, Color.WHITE);
        leyenda(g, Material.GOMA, "rebota mucho", ancho / 2 - 190, 360);
        leyenda(g, Material.MADERA, "rebote medio", ancho / 2 - 190, 396);
        leyenda(g, Material.ACERO, "pesada, casi no rebota", ancho / 2 - 190, 432);

        jugar.dibujar(g, jugar.contiene(entrada.getMouseX(), entrada.getMouseY()));
        salir.dibujar(g, salir.contiene(entrada.getMouseX(), entrada.getMouseY()));
    }

    private void leyenda(Graphics2D g, Material m, String detalle, int x, int y) {
        g.setColor(m.getColor());
        g.fillOval(x, y - 14, 20, 20);
        Texto.izquierda(g, m.getNombre() + " (" + m.getPuntos() + " pts): " + detalle,
                x + 34, y + 2, 18, Color.WHITE);
    }
}
