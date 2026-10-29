package structures;

/**
 * Lista simplesmente encadeada de adjacencias de um vertice, mantendo os nos
 * vizinhos ordenados de forma estritamente crescente pelo indice de destino.
 */
public class ListaAdjacencia {
    private NoAdjacencia cabeca;

    /**
     * Construtor da lista de adjacencia.
     *
     * Pré-condição: Nenhuma.
     * Pós-condição: Uma lista de adjacencia vazia e instanciada com cabeca nula.
     */
    public ListaAdjacencia() {
        this.cabeca = null;
    }

    /**
     * Insere um novo vertice adjacente na lista encadeada mantendo a ordem crescente pelo indice de destino.
     *
     * Pré-condição: destino deve ser um indice de vertice valido.
     * Pós-condição: Um novo no contendo destino e peso e inserido de forma ordenada na lista.
     *
     * @param destino Vertice adjacente de destino.
     * @param peso    Peso associado a aresta.
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

    /**
     * Obtem o primeiro no da lista encadeada de adjacencias.
     *
     * Pré-condição: A lista deve estar instanciada.
     * Pós-condição: Retorna o no cabeca da lista sem altera-la.
     *
     * @return O primeiro NoAdjacencia da lista, ou null se a lista estiver vazia.
     */
    public NoAdjacencia getCabeca() {
        return cabeca;
    }
}
