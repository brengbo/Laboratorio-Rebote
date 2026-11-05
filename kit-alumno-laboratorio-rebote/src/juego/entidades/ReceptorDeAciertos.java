package juego.entidades;

/**
 * Contrato de quien quiere enterarse de que una pelota entró en el aro.
 * Lo define el paquete de entidades para que el Aro no dependa de las reglas
 * (las reglas dependen de las entidades, nunca al revés).
 */
public interface ReceptorDeAciertos {
    void registrarAcierto(Pelota pelota);
}
