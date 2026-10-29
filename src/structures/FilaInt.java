package structures;

/**
 * Estrutura de dados de fila circular sequencial (FIFO) para numeros inteiros,
 * utilizada na execucao de algoritmos como a Busca em Largura (BFS).
 */
public class FilaInt {
    private final int[] dados;
    private int frente;
    private int tras;
    private int tamanho;

    /**
     * Construtor da fila circular com capacidade definida.
     *
     * Pré-condição: capacidade deve ser um numero inteiro maior que zero.
     * Pós-condição: Uma fila circular vazia e criada para armazenar ate capacidade elementos.
     *
     * @param capacidade Quantidade maxima de elementos suportados pela fila.
     */
    public FilaInt(int capacidade) {
        this.dados = new int[capacidade];
        this.frente = 0;
        this.tras = -1;
        this.tamanho = 0;
    }

    /**
     * Insere um novo valor no final da fila.
     *
     * Pré-condição: A fila nao deve estar cheia.
     * Pós-condição: O elemento e inserido na posicao de final da fila e o tamanho e incrementado em 1.
     *
     * @param valor Valor inteiro a ser inserido na fila.
     * @throws IllegalStateException Se a fila ja estiver cheia.
     */
    public void enfileirar(int valor) {
        if (estaCheia()) {
            throw new IllegalStateException("Capacidade máxima da fila excedida (" + dados.length + ").");
        }
        tras = (tras + 1) % dados.length;
        dados[tras] = valor;
        tamanho++;
    }

    /**
     * Remove e retorna o valor posicionado na frente da fila.
     *
     * Pré-condição: A fila nao deve estar vazia.
     * Pós-condição: O elemento da frente e removido e retornado, e o tamanho da fila e decrementado em 1.
     *
     * @return O valor inteiro removido da frente da fila.
     * @throws IllegalStateException Se a fila estiver vazia.
     */
    public int desenfileirar() {
        if (estaVazia()) {
            throw new IllegalStateException("Tentativa de desenfileirar de uma fila vazia.");
        }
        int valor = dados[frente];
        frente = (frente + 1) % dados.length;
        tamanho--;
        return valor;
    }

    /**
     * Verifica se a fila esta sem nenhum elemento.
     *
     * Pré-condição: A fila deve estar instanciada.
     * Pós-condição: Retorna true se a fila estiver vazia e false caso contrario.
     *
     * @return true se a fila estiver vazia, false caso contrario.
     */
    public boolean estaVazia() {
        return tamanho == 0;
    }

    /**
     * Verifica se a fila atingiu a sua capacidade maxima.
     *
     * Pré-condição: A fila deve estar instanciada.
     * Pós-condição: Retorna true se a fila estiver cheia e false caso contrario.
     *
     * @return true se a fila estiver cheia, false caso contrario.
     */
    public boolean estaCheia() {
        return tamanho == dados.length;
    }

    /**
     * Obtem o numero atual de elementos armazenados na fila.
     *
     * Pré-condição: A fila deve estar instanciada.
     * Pós-condição: Retorna o total de elementos enfileirados sem modificar a fila.
     *
     * @return Quantidade de elementos na fila.
     */
    public int getTamanho() {
        return tamanho;
    }
}
