package estructuras;

public class MiPila<T> implements Pila<T> {

    private static class Nodo<T> {
        T dato;
        Nodo<T> abajo;
        Nodo(T dato, Nodo<T> abajo) {
            this.dato = dato;
            this.abajo = abajo;
        }
    }

    private Nodo<T> cima;
    private int cantidad;

    @Override
    public void apilar(T elemento) {
        cima = new Nodo<>(elemento, cima);
        cantidad++;
    }

    @Override
    public T desapilar() {
        if (estaVacia()) {
            throw new RuntimeException("La pila está vacía");
        }
        T dato = cima.dato;
        cima = cima.abajo;
        cantidad--;
        return dato;
    }

    @Override
    public T tope() {
        if (estaVacia()) {
            throw new RuntimeException("La pila está vacía");
        }
        return cima.dato;
    }

    @Override
    public boolean estaVacia() {
        return cima == null;
    }

    @Override
    public int tamanio() {
        return cantidad;
    }

    @Override
    public void vaciar() {
        cima = null;
        cantidad = 0;
    }
}