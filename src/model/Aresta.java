package model;

/**
 * Representa uma aresta do grafo com origem, destino, peso e flag de destaque
 * visual.
 */
public class Aresta {
    private final int u;
    private final int v;
    private final int peso;
    private boolean destacada;

    /**
     * Construtor da Aresta.
     * 
     * @param u    Vértice de origem.
     * @param v    Vértice de destino.
     * @param peso Peso inteiro da aresta.
     */
    public Aresta(int u, int v, int peso) {
        this.u = u;
        this.v = v;
        this.peso = peso;
        this.destacada = false;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public int getPeso() {
        return peso;
    }

    public boolean isDestacada() {
        return destacada;
    }

    public void setDestacada(boolean destacada) {
        this.destacada = destacada;
    }
}