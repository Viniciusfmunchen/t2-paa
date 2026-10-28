package structures;

/**
 * Estrutura de dados Fila circular (FIFO) implementada do zero para a Busca em
 * Largura (BFS).
 */
public class FilaInt {
    private final int[] dados;
    private int frente;
    private int tras;
    private int tamanho;

    /**
     * Inicializa a fila com uma capacidade fixa.
     * 
     * @param capacidade Número máximo de elementos suportados.
     */
    public FilaInt(int capacidade) {
        this.dados = new int[capacidade];
        this.frente = 0;
        this.tras = -1;
        this.tamanho = 0;
    }

    /**
     * Adiciona um vértice no final da fila.
     * 
     * @param valor Identificador do vértice.
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
     * Remove e retorna o vértice na frente da fila.
     * 
     * @return Identificador do vértice removido.
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
     * Verifica se a fila não possui elementos.
     * 
     * @return true se vazia, false caso contrário.
     */
    public boolean estaVazia() {
        return tamanho == 0;
    }

    /**
     * Verifica se a fila atingiu a capacidade máxima.
     *
     * @return true se cheia, false caso contrário.
     */
    public boolean estaCheia() {
        return tamanho == dados.length;
    }

    public int getTamanho() {
        return tamanho;
    }
}
