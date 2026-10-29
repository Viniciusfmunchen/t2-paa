package model;

import structures.ListaAdjacencia;

/**
 * Entidade que representa a estrutura de um grafo por meio de listas de adjacencia
 * e um arranjo dinamico de arestas.
 */
public class Grafo {
    private static final int CAPACIDADE_INICIAL_ARESTAS = 20;

    private final int numVertices;
    private final boolean orientado;
    private final ListaAdjacencia[] adjacencias;
    private Aresta[] arestas;
    private int numArestas;
    private boolean possuiPesoNegativo;

    /**
     * Construtor da estrutura do grafo.
     *
     * Pré-condição: numVertices deve ser maior ou igual a zero.
     * Pós-condição: O grafo e criado com numVertices vertices, listas de adjacencia vazias e sem arestas.
     *
     * @param numVertices Quantidade total de vertices do grafo (rotulados de 0 a numVertices - 1).
     * @param orientado   true se as arestas forem direcionadas, false caso contrario.
     */
    public Grafo(int numVertices, boolean orientado) {
        this.numVertices = numVertices;
        this.orientado = orientado;
        this.adjacencias = new ListaAdjacencia[numVertices];
        for (int i = 0; i < numVertices; i++) {
            this.adjacencias[i] = new ListaAdjacencia();
        }
        this.arestas = new Aresta[CAPACIDADE_INICIAL_ARESTAS];
        this.numArestas = 0;
        this.possuiPesoNegativo = false;
    }

    /**
     * Adiciona uma aresta ponderada ao grafo mantendo as listas de adjacencia ordenadas de forma crescente.
     *
     * Pré-condição: u e v devem ser vertices validos no intervalo [0, numVertices - 1].
     * Pós-condição: A aresta e inserida na lista de adjacencia de u e no arranjo de arestas;
     * caso o grafo seja nao-orientado e nao seja laco, a aresta reversa tambem e inserida na adjacencia de v.
     *
     * @param u    Vertice de origem.
     * @param v    Vertice de destino.
     * @param peso Peso associado a aresta.
     */
    public void adicionarAresta(int u, int v, int peso) {
        if (peso < 0) {
            this.possuiPesoNegativo = true;
        }
        adjacencias[u].inserirOrdenado(v, peso);
        if (!orientado && u != v) {
            adjacencias[v].inserirOrdenado(u, peso);
        }

        if (numArestas == arestas.length) {
            redimensionarVetorArestas();
        }
        arestas[numArestas++] = new Aresta(u, v, peso);
    }

    /**
     * Duplica a capacidade do vetor interno de arestas preservando os elementos existentes.
     *
     * Pré-condição: O vetor de arestas deve ter atingido a capacidade maxima atual.
     * Pós-condição: O vetor de arestas tem seu tamanho duplicado com os dados anteriores preservados.
     */
    private void redimensionarVetorArestas() {
        Aresta[] novo = new Aresta[arestas.length * 2];
        for (int i = 0; i < numArestas; i++) {
            novo[i] = arestas[i];
        }
        arestas = novo;
    }

    /**
     * Remove o destaque visual de todas as arestas do grafo.
     *
     * Pré-condição: O grafo deve estar inicializado.
     * Pós-condição: O atributo destacada de todas as arestas cadastradas passa a ser false.
     */
    public void limparDestaquesArestas() {
        for (int i = 0; i < numArestas; i++) {
            arestas[i].setDestacada(false);
        }
    }

    /**
     * Retorna a aresta localizada na posicao indicada do vetor interno de arestas.
     *
     * Pré-condição: indice deve pertencer ao intervalo [0, numArestas - 1].
     * Pós-condição: Retorna a aresta consultada sem alterar o estado do grafo.
     *
     * @param indice Posicao da aresta no vetor interno.
     * @return A instancia de Aresta correspondente.
     * @throws IndexOutOfBoundsException Se indice for menor que 0 ou maior ou igual a numArestas.
     */
    public Aresta getAresta(int indice) {
        if (indice < 0 || indice >= numArestas) {
            throw new IndexOutOfBoundsException("Índice de aresta inválido: " + indice);
        }
        return arestas[indice];
    }

    /**
     * Retorna a quantidade total de vertices do grafo.
     *
     * Pré-condição: O grafo deve estar instanciado.
     * Pós-condição: Retorna o numero de vertices sem alterar o estado do grafo.
     *
     * @return O numero total de vertices.
     */
    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Informa se o grafo e orientado (direcionado).
     *
     * Pré-condição: O grafo deve estar instanciado.
     * Pós-condição: Retorna true se for orientado e false se for nao-orientado.
     *
     * @return true se o grafo for orientado, false caso contrario.
     */
    public boolean isOrientado() {
        return orientado;
    }

    /**
     * Obtem a lista encadeada de adjacencias de um determinado vertice.
     *
     * Pré-condição: v deve ser um vertice valido no intervalo [0, numVertices - 1].
     * Pós-condição: Retorna a lista de adjacencias de v sem alterar o grafo.
     *
     * @param v Indice do vertice consultado.
     * @return A ListaAdjacencia associada ao vertice v.
     */
    public ListaAdjacencia getAdjacencia(int v) {
        return adjacencias[v];
    }

    /**
     * Retorna o arranjo contendo as arestas cadastradas no grafo.
     *
     * Pré-condição: O grafo deve estar instanciado.
     * Pós-condição: Retorna a referencia para o vetor interno de arestas.
     *
     * @return Vetor contendo as instancias de Aresta.
     */
    public Aresta[] getArestas() {
        return arestas;
    }

    /**
     * Retorna a quantidade total de arestas inseridas no grafo.
     *
     * Pré-condição: O grafo deve estar instanciado.
     * Pós-condição: Retorna o total de arestas sem alterar a estrutura do grafo.
     *
     * @return O numero de arestas cadastradas.
     */
    public int getNumArestas() {
        return numArestas;
    }

    /**
     * Informa se o grafo possui ao menos uma aresta com peso negativo.
     *
     * Pré-condição: O grafo deve estar instanciado.
     * Pós-condição: Retorna true se houver aresta com peso negativo e false caso contrario.
     *
     * @return true se existir aresta com peso negativo, false caso contrario.
     */
    public boolean isPossuiPesoNegativo() {
        return possuiPesoNegativo;
    }
}
