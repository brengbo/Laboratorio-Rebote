package juego.reglas;

/** Puntaje de la partida con bonus por racha de aciertos del mismo material. */
public class Puntaje {

    private int total;
    private int racha;

    public int getTotal() { return total; }
    public int getRacha() { return racha; }

    /**
     * Suma un acierto.
     * @param base puntos del material (no puede ser negativo)
     * @param igualAlAnterior true si el material repite el del acierto anterior
     * @return puntos ganados en este acierto
     */
    public int registrarAcierto(int base, boolean igualAlAnterior) {
        if (base < 0) {
            throw new IllegalArgumentException("Los puntos base no pueden ser negativos: " + base);
        }
        racha = igualAlAnterior ? racha + 1 : 0;
        int ganados = base + base * Math.min(racha, 4) / 2;
        total += ganados;
        return ganados;
    }

    public void reiniciar() {
        total = 0;
        racha = 0;
    }
}
