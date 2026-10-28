package structures;

/**
 * Estrutura de dados Pilha (LIFO) implementada do zero para Ordenação
 * Topológica e Kosaraju.
 */
public class PilhaInt {
    private final int[] dados;
    private int topo;

    /**
     * Construtor da pilha.
     * 
     * @param capacidade Quantidade máxima de elementos.
     */
    public PilhaInt(int capacidade) {
        this.dados = new int[capacidade];
        this.topo = -1;
    }

    /**
     * Insere um elemento no topo da pilha.
     * 
     * @param valor Identificador do vértice.
     */
    public void empilhar(int valor) {
        if (estaCheia()) {
            throw new IllegalStateException("Capacidade máxima da pilha excedida (" + dados.length + ").");
        }
        dados[++topo] = valor;
    }

    /**
     * Remove e retorna o elemento do topo da pilha.
     * 
     * @return Identificador do vértice no topo.
     */
    public int desempilhar() {
        if (estaVazia()) {
            throw new IllegalStateException("Tentativa de desempilhar de uma pilha vazia.");
        }
        return dados[topo--];
    }

    /**
     * Indica se a pilha está vazia.
     * 
     * @return true se vazia, false caso contrário.
     */
    public boolean estaVazia() {
        return topo == -1;
    }

    /**
     * Indica se a pilha atingiu a capacidade máxima.
     *
     * @return true se cheia, false caso contrário.
     */
    public boolean estaCheia() {
        return topo == dados.length - 1;
    }
}
