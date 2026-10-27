package structures;

/**
 * Lista simplesmente encadeada que mantém os vértices adjacentes em estrita
 * ordem crescente.
 */
public class ListaAdjacencia {
    private NoAdjacencia cabeca;

    /**
     * Insere um novo vértice adjacente garantindo a ordenação crescente pelo índice
     * do vértice.
     * 
     * @param destino Vértice adjacente de destino.
     * @param peso    Peso da aresta.
     */
    public void inserirOrdenado(int destino, int peso) {
        NoAdjacencia novo = new NoAdjacencia(destino, peso);
        if (cabeca == null || cabeca.getDestino() > destino) {
            novo.setProximo(cabeca);
            cabeca = novo;
        } else {
            NoAdjacencia atual = cabeca;
            while (atual.getProximo() != null && atual.getProximo().getDestino() < destino) {
                atual = atual.getProximo();
            }
            novo.setProximo(atual.getProximo());
            atual.setProximo(novo);
        }
    }

    public NoAdjacencia getCabeca() {
        return cabeca;
    }
}