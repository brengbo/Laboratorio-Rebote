package estructuras;

// NOTA: el kit original traía esta interfaz sin Iterable, contiene ni quitar,
// pero ListaBase.java y motor/Mundo.java ya las usan y no compilaban.
// Este es el único cambio al kit (hay que avisarle al docente).
public interface Lista<T> extends Iterable<T> {

    // Altas
    void agregar(T elemento);

    void insertar(int indice, T elemento);

    // Consultas
    T obtener(int indice);

    int indiceDe(T elemento);

    boolean contiene(T elemento);

    int tamanio();

    boolean estaVacia();

    // Bajas
    T eliminar(int indice);

    boolean quitar(T elemento);

    void vaciar();
}
