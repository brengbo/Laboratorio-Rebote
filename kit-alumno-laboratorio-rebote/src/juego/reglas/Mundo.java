package juego.reglas;

import contratos.Actualizable;
import contratos.Dibujable;
import estructuras.Lista;
import estructuras.MiLista;
import estructuras.MiPila;
import estructuras.Pila;
import juego.entidades.Aro;
import juego.entidades.Entidad;
import juego.entidades.Material;
import juego.entidades.Obstaculo;
import juego.entidades.ReceptorDeAciertos;
import juego.entidades.Pelota;
import juego.pantallas.Efectos;
import juego.pantallas.Hud;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;

/**
 * El mundo del juego: contiene las entidades, las recorre en cada paso,
 * resuelve las interacciones, lleva el puntaje y sube la dificultad.
 *
 * Estructuras:
 *  - Lista: todas las entidades del mundo (aro, obstáculos y pelotas en vuelo).
 *  - Pila: historial de aciertos; su tope decide si hay racha de material.
 */
public class Mundo implements Actualizable, Dibujable, ReceptorDeAciertos {

    private static final int ACIERTOS_PARA_OBSTACULO = 3;
    private static final int HISTORIAL_VISIBLE = 6;

    private final int ancho;
    private final int alto;
    private final double sueloY;

    private final Lista<Entidad> entidades = new MiLista<>();
    private final Pila<Material> historial = new MiPila<>();
    private final Puntaje puntaje = new Puntaje();
    private final Efectos efectos = new Efectos();
    private final Aro aro;

    private int aciertos;
    private boolean subioNivel;
    private boolean obstaculoColocado;

    public Mundo(int ancho, int alto) {
        if (ancho < 400 || alto < 300) {
            throw new IllegalArgumentException("El mundo es demasiado chico: " + ancho + "x" + alto);
        }
        this.ancho = ancho;
        this.alto = alto;
        this.sueloY = alto - 40;
        this.aro = new Aro(ancho * 0.65, alto * 0.35, 130, 90,
                           ancho * 0.45, ancho * 0.88, this);
        agregar(aro);
    }

    public double getSueloY() { return sueloY; }
    public int getPuntaje() { return puntaje.getTotal(); }
    public int getRacha() { return puntaje.getRacha(); }
    public int getAciertos() { return aciertos; }

    /** Agrega una entidad al mundo y le avisa el tamaño del área. */
    public void agregar(Entidad entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("No se puede agregar una entidad nula");
        }
        entidad.setArea(ancho, sueloY);
        entidades.agregar(entidad);
    }

    /** Cantidad de pelotas que todavía se están moviendo. */
    public int pelotasEnVuelo() {
        int cuenta = 0;
        for (int i = 0; i < entidades.tamanio(); i++) {
            Entidad e = entidades.obtener(i);
            if (e.esProyectil() && e.isActiva()) {
                cuenta++;
            }
        }
        return cuenta;
    }

    @Override
    public void actualizar(double dt) {
        // 1) Cada entidad se actualiza a sí misma (polimorfismo).
        for (int i = 0; i < entidades.tamanio(); i++) {
            entidades.obtener(i).actualizar(dt);
        }

        // 2) Cada par interactúa: la pelota le pide a la otra que la afecte.
        int n = entidades.tamanio();
        for (int i = 0; i < n; i++) {
            Entidad a = entidades.obtener(i);
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    a.interactuar(entidades.obtener(j));
                }
            }
        }

        // 3) Se retiran las entidades que quedaron inactivas (de atrás para adelante).
        for (int i = entidades.tamanio() - 1; i >= 0; i--) {
            if (!entidades.obtener(i).isActiva()) {
                entidades.eliminar(i);
            }
        }

        efectos.actualizar(dt);

        // 4) Cambios de dificultad, fuera del recorrido de la lista.
        if (subioNivel) {
            subioNivel = false;
            obstaculoColocado = true;
            agregar(new Obstaculo(ancho * 0.5, alto * 0.5, 26));
        }
    }

    /** El aro avisa que una pelota entró. */
    @Override
    public void registrarAcierto(Pelota pelota) {
        Material material = pelota.getMaterial();
        boolean igualAlAnterior = !historial.estaVacia() && historial.tope() == material;
        int ganados = puntaje.registrarAcierto(material.getPuntos(), igualAlAnterior);
        efectos.explosion(pelota.getX(), pelota.getY(), material.getColor(), 16);
        efectos.textoFlotante(pelota.getX(), pelota.getY() - 20, "+" + ganados, new Color(255, 230, 120));
        historial.apilar(material);
        aciertos++;
        aro.endurecer(15, 4);
        if (aciertos >= ACIERTOS_PARA_OBSTACULO && !obstaculoColocado) {
            subioNivel = true;
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        Paint anterior = g.getPaint();
        g.setPaint(new GradientPaint(0, 0, new Color(18, 24, 48), 0, alto, new Color(40, 60, 100)));
        g.fillRect(0, 0, ancho, alto);
        g.setPaint(anterior);

        g.setColor(new Color(60, 140, 80));
        g.fillRect(0, (int) sueloY, ancho, alto - (int) sueloY);

        for (int i = 0; i < entidades.tamanio(); i++) {
            entidades.obtener(i).dibujar(g);
        }
        efectos.dibujar(g);
    }

    /** El HUD recorre la pila de aciertos y la deja intacta. */
    public void dibujarHistorial(Graphics2D g, int x, int y) {
        Hud.historial(g, historial, aciertos, x, y);
    }
}
