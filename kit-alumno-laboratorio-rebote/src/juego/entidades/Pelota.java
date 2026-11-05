package juego.entidades;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Pelota que se lanza para embocar en el aro. Su material define el radio,
 * el rebote y el rozamiento. Velocidades en píxeles por segundo.
 */
public class Pelota extends Entidad implements Colisionable {

    public static final double GRAVEDAD = 900;
    private static final double VELOCIDAD_MAXIMA = 1400;
    private static final double TIEMPO_REPOSO_MAXIMO = 1.5;
    private static final double VIDA_MAXIMA = 20;

    private final Material material;
    private double vx;
    private double vy;
    private double yAnterior;
    private double tiempoReposo;
    private double edad;

    public Pelota(Material material, double x, double y) {
        this(material, x, y, 0, 0);
    }

    public Pelota(Material material, double x, double y, double vx, double vy) {
        super(x, y);
        if (material == null) {
            throw new IllegalArgumentException("La pelota necesita un material");
        }
        this.material = material;
        this.yAnterior = y;
        setVelocidad(vx, vy);
    }

    public Material getMaterial() { return material; }
    public double getVx() { return vx; }
    public double getVy() { return vy; }
    public double getYAnterior() { return yAnterior; }

    @Override
    public double getRadio() { return material.getRadio(); }

    /** Valida y limita la velocidad para que nunca quede un estado imposible. */
    public void setVelocidad(double vx, double vy) {
        if (!Double.isFinite(vx) || !Double.isFinite(vy)) {
            throw new IllegalArgumentException("Velocidad inválida: " + vx + ", " + vy);
        }
        double modulo = Math.hypot(vx, vy);
        if (modulo > VELOCIDAD_MAXIMA) {
            vx = vx / modulo * VELOCIDAD_MAXIMA;
            vy = vy / modulo * VELOCIDAD_MAXIMA;
        }
        this.vx = vx;
        this.vy = vy;
    }

    @Override
    public boolean esProyectil() { return true; }

    /** Doble despacho: la pelota le pide a la otra entidad que la afecte. */
    @Override
    public void interactuar(Entidad otra) {
        if (isActiva()) {
            otra.afectarA(this);
        }
    }

    @Override
    public void actualizar(double dt) {
        yAnterior = getY();
        edad += dt;

        vy += GRAVEDAD * dt;
        double freno = Math.max(0, 1 - material.getArrastre() * dt);
        vx *= freno;
        vy *= freno;
        setPosicion(getX() + vx * dt, getY() + vy * dt);

        double r = getRadio();
        boolean enSuelo = false;

        double limiteSuelo = getSueloY() - r;
        if (getY() >= limiteSuelo) {
            setPosicion(getX(), limiteSuelo);
            vy = -vy * material.getElasticidad();
            vx *= Math.max(0, 1 - 6 * dt);   // fricción con el suelo
            if (Math.abs(vy) < 40) {
                vy = 0;
            }
            enSuelo = true;
        }

        if (getX() <= r) {
            setPosicion(r, getY());
            vx = -vx * material.getElasticidad();
        } else if (getX() >= getAnchoArea() - r) {
            setPosicion(getAnchoArea() - r, getY());
            vx = -vx * material.getElasticidad();
        }

        if (enSuelo && vy == 0 && Math.abs(vx) < 10) {
            tiempoReposo += dt;
        } else {
            tiempoReposo = 0;
        }
        if (tiempoReposo >= TIEMPO_REPOSO_MAXIMO || edad >= VIDA_MAXIMA) {
            desactivar();
        }
    }

    public boolean estaDetenida() {
        return tiempoReposo > 0;
    }

    /**
     * Rebota contra otro círculo fijo. El coeficiente es 1 para un rebote
     * perfecto, menor que 1 pierde energía y mayor que 1 la gana (bumper).
     * @return true si hubo choque
     */
    public boolean rebotarContra(Colisionable otro, double coeficiente) {
        if (otro == null) {
            throw new IllegalArgumentException("Falta el objeto contra el que rebotar");
        }
        if (coeficiente < 0) {
            throw new IllegalArgumentException("Coeficiente negativo: " + coeficiente);
        }
        double dx = getX() - otro.getX();
        double dy = getY() - otro.getY();
        double minima = getRadio() + otro.getRadio();
        double d2 = dx * dx + dy * dy;
        if (d2 >= minima * minima) {
            return false;
        }
        double d = Math.sqrt(d2);
        double nx = 0;
        double ny = -1;
        if (d > 1e-9) {
            nx = dx / d;
            ny = dy / d;
        }
        setPosicion(otro.getX() + nx * minima, otro.getY() + ny * minima);
        double vn = vx * nx + vy * ny;
        if (vn < 0) {
            setVelocidad(vx - (1 + coeficiente) * vn * nx,
                         vy - (1 + coeficiente) * vn * ny);
        }
        return true;
    }

    @Override
    public void dibujar(Graphics2D g) {
        int r = (int) getRadio();
        g.setColor(material.getColor());
        g.fillOval((int) (getX() - r), (int) (getY() - r), r * 2, r * 2);
        g.setColor(material.getColor().brighter());
        g.fillOval((int) (getX() - r / 2.0), (int) (getY() - r / 2.0), r / 2, r / 2);
        g.setColor(new Color(0, 0, 0, 80));
        g.drawOval((int) (getX() - r), (int) (getY() - r), r * 2, r * 2);
    }
}
