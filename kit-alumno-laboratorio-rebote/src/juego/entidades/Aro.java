package juego.entidades;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * Aro que se mueve de lado a lado. Cuenta un acierto cuando una pelota lo
 * cruza de arriba hacia abajo por la abertura; los dos extremos son postes
 * contra los que la pelota rebota.
 */
public class Aro extends Entidad implements Colisionable {

    private static final double RADIO_POSTE = 6;
    private static final double APERTURA_MINIMA = 80;
    private static final double APERTURA_MAXIMA = 300;
    private static final double VELOCIDAD_MAXIMA = 420;

    private final ReceptorDeAciertos receptor;
    private final Obstaculo posteIzquierdo;
    private final Obstaculo posteDerecho;
    private final double minX;
    private final double maxX;
    private double apertura;
    private double velocidad;
    private int sentido = 1;

    public Aro(double x, double y, double apertura, double velocidad,
               double minX, double maxX, ReceptorDeAciertos receptor) {
        super(x, y);
        if (receptor == null) {
            throw new IllegalArgumentException("El aro necesita un receptor de aciertos");
        }
        if (minX >= maxX || x < minX || x > maxX) {
            throw new IllegalArgumentException("Rango de movimiento inválido");
        }
        this.receptor = receptor;
        this.minX = minX;
        this.maxX = maxX;
        setApertura(apertura);
        setVelocidad(velocidad);
        this.posteIzquierdo = new Obstaculo(x, y, RADIO_POSTE);
        this.posteDerecho = new Obstaculo(x, y, RADIO_POSTE);
        sincronizarPostes();
    }

    public double getApertura() { return apertura; }
    public double getVelocidad() { return velocidad; }

    /** El radio del aro es la mitad de su abertura. */
    @Override
    public double getRadio() { return apertura / 2; }

    public void setApertura(double apertura) {
        if (apertura < APERTURA_MINIMA || apertura > APERTURA_MAXIMA) {
            throw new IllegalArgumentException("Apertura fuera de rango: " + apertura);
        }
        this.apertura = apertura;
    }

    public void setVelocidad(double velocidad) {
        if (velocidad < 0 || velocidad > VELOCIDAD_MAXIMA) {
            throw new IllegalArgumentException("Velocidad fuera de rango: " + velocidad);
        }
        this.velocidad = velocidad;
    }

    /** Dificultad creciente: más rápido y más angosto (con topes). */
    public void endurecer(double masVelocidad, double menosApertura) {
        setVelocidad(Math.min(VELOCIDAD_MAXIMA, velocidad + Math.max(0, masVelocidad)));
        setApertura(Math.max(APERTURA_MINIMA, apertura - Math.max(0, menosApertura)));
        sincronizarPostes();
    }

    @Override
    public void actualizar(double dt) {
        double nuevaX = getX() + sentido * velocidad * dt;
        if (nuevaX >= maxX) {
            nuevaX = maxX;
            sentido = -1;
        } else if (nuevaX <= minX) {
            nuevaX = minX;
            sentido = 1;
        }
        setPosicion(nuevaX, getY());
        sincronizarPostes();
    }

    @Override
    public void afectarA(Pelota pelota) {
        double e = pelota.getMaterial().getElasticidad();
        pelota.rebotarContra(posteIzquierdo, e);
        pelota.rebotarContra(posteDerecho, e);

        boolean cruzaHaciaAbajo = pelota.getVy() > 0
                && pelota.getYAnterior() < getY()
                && pelota.getY() >= getY();
        boolean pasaPorLaAbertura = Math.abs(pelota.getX() - getX())
                <= apertura / 2 - RADIO_POSTE - pelota.getRadio();
        if (pelota.isActiva() && cruzaHaciaAbajo && pasaPorLaAbertura) {
            pelota.desactivar();
            receptor.registrarAcierto(pelota);
        }
    }

    private void sincronizarPostes() {
        posteIzquierdo.setPosicion(getX() - apertura / 2, getY());
        posteDerecho.setPosicion(getX() + apertura / 2, getY());
    }

    @Override
    public void dibujar(Graphics2D g) {
        double izq = getX() - apertura / 2;
        double der = getX() + apertura / 2;
        int y = (int) getY();

        // Red
        g.setColor(new Color(255, 255, 255, 110));
        for (int i = 0; i <= 6; i++) {
            double xs = izq + (der - izq) * i / 6;
            double xi = getX() + (xs - getX()) * 0.6;
            g.drawLine((int) xs, y, (int) xi, y + 45);
        }
        g.drawLine((int) (getX() - apertura * 0.3), y + 45,
                   (int) (getX() + apertura * 0.3), y + 45);

        // Borde y postes
        Stroke anterior = g.getStroke();
        g.setStroke(new BasicStroke(4f));
        g.setColor(new Color(220, 60, 60));
        g.drawLine((int) izq, y, (int) der, y);
        g.setStroke(anterior);
        int rp = (int) RADIO_POSTE;
        g.fillOval((int) izq - rp, y - rp, rp * 2, rp * 2);
        g.fillOval((int) der - rp, y - rp, rp * 2, rp * 2);
    }
}
