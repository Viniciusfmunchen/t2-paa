package structures;

/**
 * Estrutura de dados de pilha sequencial (LIFO) para numeros inteiros,
 * utilizada em algoritmos como Ordenacao Topologica e Kosaraju.
 */
public class PilhaInt {
    private final int[] dados;
    private int topo;

    /**
     * Construtor da pilha sequencial.
     *
     * Pré-condição: capacidade deve ser um inteiro maior que zero.
     * Pós-condição: Uma pilha vazia com capacidade para armazenar ate capacidade elementos e criada.
     *
     * @param capacidade Limite maximo de elementos suportados pela pilha.
     */
    public PilhaInt(int capacidade) {
        this.dados = new int[capacidade];
        this.topo = -1;
    }

    /**
     * Empilha um novo elemento no topo da pilha.
     *
     * Pré-condição: A pilha nao deve estar cheia.
     * Pós-condição: O elemento valor e inserido no topo e a posicao do topo e incrementada em 1.
     *
     * @param valor Valor inteiro a ser empilhado.
     * @throws IllegalStateException Se a pilha ja estiver cheia.
     */
    public void empilhar(int valor) {
        if (estaCheia()) {
            throw new IllegalStateException("Capacidade máxima da pilha excedida (" + dados.length + ").");
        }
        dados[++topo] = valor;
    }

    /**
     * Remove e retorna o elemento atualmente posicionado no topo da pilha.
     *
     * Pré-condição: A pilha nao deve estar vazia.
     * Pós-condição: O elemento do topo e removido e retornado, e o ponteiro de topo e decrementado em 1.
     *
     * @return O valor inteiro removido do topo da pilha.
     * @throws IllegalStateException Se a pilha estiver vazia.
     */
    public int desempilhar() {
        if (estaVazia()) {
            throw new IllegalStateException("Tentativa de desempilhar de uma pilha vazia.");
        }
        return dados[topo--];
    }

    /**
     * Verifica se a pilha esta vazia.
     *
     * Pré-condição: A pilha deve estar instanciada.
     * Pós-condição: Retorna true se a pilha nao contiver elementos e false caso contrario.
     *
     * @return true se a pilha estiver vazia, false caso contrario.
     */
    public boolean estaVazia() {
        return topo == -1;
    }

    /**
     * Verifica se a pilha alcancou a capacidade maxima de armazenamento.
     *
     * Pré-condição: A pilha deve estar instanciada.
     * Pós-condição: Retorna true se a pilha estiver cheia e false caso contrario.
     *
     * @return true se a pilha estiver cheia, false caso contrario.
     */
    public boolean estaCheia() {
        return topo == dados.length - 1;
    }
}
