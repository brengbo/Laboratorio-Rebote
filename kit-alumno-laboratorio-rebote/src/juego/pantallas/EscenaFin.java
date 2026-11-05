package juego.pantallas;

import juego.entidades.Material;
import motor.Entrada;
import motor.Escena;
import ui.Texto;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

/** Pantalla de fin: muestra el puntaje (solo en pantalla) y permite repetir o volver al menú. */
public class EscenaFin extends Escena {

    private final int puntaje;
    private final GeneradorDeEscena generadorPartida;
    private final GeneradorDeEscena generadorMenu;
    private final Efectos confeti = new Efectos();
    private final Random azar = new Random();
    private Boton otraVez;
    private Boton menu;
    private double proximoFestejo;

    public EscenaFin(int puntaje, GeneradorDeEscena generadorPartida, GeneradorDeEscena generadorMenu) {
        if (puntaje < 0) {
            throw new IllegalArgumentException("El puntaje no puede ser negativo: " + puntaje);
        }
        if (generadorPartida == null || generadorMenu == null) {
            throw new IllegalArgumentException("Faltan las escenas de destino");
        }
        this.puntaje = puntaje;
        this.generadorPartida = generadorPartida;
        this.generadorMenu = generadorMenu;
    }

    @Override
    public void alEntrar() {
        int ancho = (int) getContexto().getAncho();
        int alto = (int) getContexto().getAlto();
        otraVez = new Boton("JUGAR DE NUEVO", ancho / 2 - 140, alto - 90, 250, 54);
        menu = new Boton("MENÚ", ancho / 2 + 140, alto - 90, 190, 54);
    }

    @Override
    public void actualizar(double dt) {
        if (otraVez == null) {
            return;
        }
        int ancho = (int) getContexto().getAncho();
        int alto = (int) getContexto().getAlto();

        proximoFestejo -= dt;
        if (puntaje > 0 && proximoFestejo <= 0) {
            proximoFestejo = 0.5;
            confeti.explosion(80 + azar.nextDouble() * (ancho - 160), 80 + azar.nextDouble() * (alto / 3.0),
                    Material.aleatorio(azar).getColor(), 18);
        }
        confeti.actualizar(dt);

        Entrada entrada = getEntrada();
        boolean clickOtraVez = entrada.huboClick() && otraVez.contiene(entrada.getMouseX(), entrada.getMouseY());
        boolean clickMenu = entrada.huboClick() && menu.contiene(entrada.getMouseX(), entrada.getMouseY());
        if (clickOtraVez || entrada.fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(generadorPartida.crear(0));
        } else if (clickMenu || entrada.fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().irA(generadorMenu.crear(0));
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (otraVez == null) {
            return;
        }
        int ancho = (int) getContexto().getAncho();
        int alto = (int) getContexto().getAlto();
        Entrada entrada = getEntrada();

        g.setColor(new Color(18, 24, 48));
        g.fillRect(0, 0, ancho, alto);
        confeti.dibujar(g);

        Texto.centrado(g, "FIN DE LA PARTIDA", ancho / 2, 150, 52, Color.WHITE);
        Texto.centrado(g, "Puntaje: " + puntaje, ancho / 2, 270, 44, new Color(255, 210, 90));
        Texto.centrado(g, comentario(), ancho / 2, 340, 24, new Color(200, 210, 230));

        otraVez.dibujar(g, otraVez.contiene(entrada.getMouseX(), entrada.getMouseY()));
        menu.dibujar(g, menu.contiene(entrada.getMouseX(), entrada.getMouseY()));
    }

    private String comentario() {
        if (puntaje == 0) {
            return "No entró ninguna. ¡A practicar la puntería!";
        } else if (puntaje < 60) {
            return "Buen intento, se puede mejorar.";
        } else if (puntaje < 150) {
            return "¡Muy bien! Ya le agarraste la mano.";
        }
        return "¡Maestro del rebote!";
    }
}
