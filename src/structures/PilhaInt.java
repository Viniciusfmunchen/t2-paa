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
        dados[++topo] = valor;
    }

    /**
     * Remove e retorna o elemento do topo da pilha.
     * 
     * @return Identificador do vértice no topo.
     */
    public int desempilhar() {
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
}