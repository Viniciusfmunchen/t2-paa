package structures;

/**
 * Representa um nó na lista encadeada de adjacências de um vértice.
 */
public class NoAdjacencia {
    private final int destino;
    private final int peso;
    private NoAdjacencia proximo;

    public NoAdjacencia(int destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public int getDestino() { return destino; }
    public int getPeso() { return peso; }
    public NoAdjacencia getProximo() { return proximo; }
    public void setProximo(NoAdjacencia proximo) { this.proximo = proximo; }
}
