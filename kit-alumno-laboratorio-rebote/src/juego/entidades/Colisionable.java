package juego.entidades;

/**
 * Contrato de todo lo que se comporta como un círculo en el mundo
 * (tiene un centro y un radio). Lo implementan Pelota, Obstaculo y Aro.
 */
public interface Colisionable {
    double getX();
    double getY();
    double getRadio();
}
