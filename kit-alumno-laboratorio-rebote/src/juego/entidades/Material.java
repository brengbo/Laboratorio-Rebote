package juego.entidades;

import java.awt.Color;
import java.util.Random;

/**
 * Materiales de las pelotas. Cada uno cambia cómo se comporta la pelota:
 * la elasticidad define el rebote y la densidad el rozamiento con el aire.
 */
public enum Material {
    GOMA("Goma", 18, 1.0, 0.85, 10, new Color(230, 90, 20)),
    MADERA("Madera", 16, 2.0, 0.60, 20, new Color(160, 110, 60)),
    ACERO("Acero", 14, 5.0, 0.35, 30, new Color(150, 160, 175));

    private final String nombre;
    private final double radio;
    private final double densidad;
    private final double elasticidad;
    private final int puntos;
    private final Color color;

    Material(String nombre, double radio, double densidad, double elasticidad,
             int puntos, Color color) {
        this.nombre = nombre;
        this.radio = radio;
        this.densidad = densidad;
        this.elasticidad = elasticidad;
        this.puntos = puntos;
        this.color = color;
    }

    public String getNombre() { return nombre; }
    public double getRadio() { return radio; }
    public double getDensidad() { return densidad; }
    public double getElasticidad() { return elasticidad; }
    public int getPuntos() { return puntos; }
    public Color getColor() { return color; }

    /** Cuánta velocidad pierde por segundo en el aire: lo pesado casi no frena. */
    public double getArrastre() {
        return 0.08 / densidad;
    }

    /** Material al azar (sin usar arreglos). */
    public static Material aleatorio(Random azar) {
        switch (azar.nextInt(3)) {
            case 0:  return GOMA;
            case 1:  return MADERA;
            default: return ACERO;
        }
    }
}
