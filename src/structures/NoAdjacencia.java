package structures;

/**
 * Representa um elemento (no) na lista encadeada de adjacencias de um vertice,
 * armazenando o vertice de destino, o peso da aresta e a referencia para o proximo no.
 */
public class NoAdjacencia {
    private final int destino;
    private final int peso;
    private NoAdjacencia proximo;

    /**
     * Construtor do no de adjacencia.
     *
     * Pré-condição: destino deve ser um indice de vertice valido e peso a ponderacao da aresta.
     * Pós-condição: Instancia um no de adjacencia com o destino e peso informados e proximo nulo.
     *
     * @param destino Indice do vertice de destino.
     * @param peso    Peso associado a aresta adjacente.
     */
    public NoAdjacencia(int destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    /**
     * Retorna o identificador do vertice de destino associado a este no.
     *
     * Pré-condição: O no deve estar instanciado.
     * Pós-condição: Retorna o indice do vertice de destino sem alterar o no.
     *
     * @return O indice do vertice de destino.
     */
    public int getDestino() {
        return destino;
    }

    /**
     * Retorna o peso associado a aresta representada por este no.
     *
     * Pré-condição: O no deve estar instanciado.
     * Pós-condição: Retorna o valor do peso sem alterar o no.
     *
     * @return O peso da aresta.
     */
    public int getPeso() {
        return peso;
    }

    /**
     * Retorna a referencia para o proximo no encadeado na lista de adjacencias.
     *
     * Pré-condição: O no deve estar instanciado.
     * Pós-condição: Retorna o proximo no da lista ou null se for o final da lista.
     *
     * @return O proximo NoAdjacencia da lista, ou null caso nao exista.
     */
    public NoAdjacencia getProximo() {
        return proximo;
    }

    /**
     * Atualiza a referencia para o proximo no da lista encadeada.
     *
     * Pré-condição: O no deve estar instanciado.
     * Pós-condição: O ponteiro proximo passa a referenciar o no informado.
     *
     * @param proximo O proximo NoAdjacencia a ser encadeado.
     */
    public void setProximo(NoAdjacencia proximo) {
        this.proximo = proximo;
    }
}
