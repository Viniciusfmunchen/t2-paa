package structures;

/**
 * Representa um nó individual na lista encadeada de adjacências de um vértice.
 */
public class NoAdjacencia {
    private final int destino;
    private final int peso;
    private NoAdjacencia proximo;

    /**
     * Construtor do nó de adjacência.
     * 
     * @param destino Índice do vértice de destino.
     * @param peso    Peso da aresta associada.
     */
    public NoAdjacencia(int destino, int peso) {
        this.destino = destino;
        this.peso = peso;
        this.proximo = null;
    }

    public int getDestino() {
        return destino;
    }

    public int getPeso() {
        return peso;
    }

    public NoAdjacencia getProximo() {
        return proximo;
    }

    public void setProximo(NoAdjacencia proximo) {
        this.proximo = proximo;
    }
}