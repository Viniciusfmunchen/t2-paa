package model;

/**
 * Representa uma aresta ponderada em um grafo, conectando um vertice de origem
 * a um vertice de destino com peso e controle de destaque visual.
 */
public class Aresta {
    private final int u;
    private final int v;
    private final int peso;
    private boolean destacada;

    /**
     * Construtor da aresta ponderada.
     *
     * Pré-condição: u e v devem ser indices validos de vertices no grafo e peso o valor associado.
     * Pós-condição: Instancia uma aresta com origem, destino e peso definidos e sem destaque visual.
     *
     * @param u    Vertice de origem.
     * @param v    Vertice de destino.
     * @param peso Valor inteiro do peso da aresta.
     */
    public Aresta(int u, int v, int peso) {
        this.u = u;
        this.v = v;
        this.peso = peso;
        this.destacada = false;
    }

    /**
     * Obtem o vertice de origem da aresta.
     *
     * Pré-condição: A instancia de Aresta deve estar criada.
     * Pós-condição: Retorna o identificador do vertice de origem sem alterar o estado do objeto.
     *
     * @return O vertice de origem.
     */
    public int getU() {
        return u;
    }

    /**
     * Obtem o vertice de destino da aresta.
     *
     * Pré-condição: A instancia de Aresta deve estar criada.
     * Pós-condição: Retorna o identificador do vertice de destino sem alterar o estado do objeto.
     *
     * @return O vertice de destino.
     */
    public int getV() {
        return v;
    }

    /**
     * Obtem o peso associado a aresta.
     *
     * Pré-condição: A instancia de Aresta deve estar criada.
     * Pós-condição: Retorna o valor do peso da aresta sem alterar o estado do objeto.
     *
     * @return O peso inteiro da aresta.
     */
    public int getPeso() {
        return peso;
    }

    /**
     * Informa se a aresta esta com destaque visual ativado.
     *
     * Pré-condição: A instancia de Aresta deve estar criada.
     * Pós-condição: Retorna true se a aresta estiver destacada e false caso contrario.
     *
     * @return true se destacada, false caso contrario.
     */
    public boolean isDestacada() {
        return destacada;
    }

    /**
     * Altera o estado de destaque visual da aresta.
     *
     * Pré-condição: A instancia de Aresta deve estar criada.
     * Pós-condição: O atributo de destaque e atualizado com o valor informado.
     *
     * @param destacada true para ativar o destaque visual, false para desativar.
     */
    public void setDestacada(boolean destacada) {
        this.destacada = destacada;
    }
}
