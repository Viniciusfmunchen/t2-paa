package model;

import structures.ListaAdjacencia;

/**
 * Entidade de domínio que encapsula a estrutura do Grafo (Listas de Adjacências
 * e Vetor Dinâmico de Arestas).
 */
public class Grafo {
    private final int numVertices;
    private final boolean orientado;
    private final ListaAdjacencia[] adjacencias;
    private Aresta[] arestas;
    private int numArestas;
    private boolean possuiPesoNegativo;

    /**
     * Construtor da classe Grafo.
     * 
     * @param numVertices Número total de vértices (0 a V-1).
     * @param orientado   Indica se as arestas são direcionadas.
     */
    public Grafo(int numVertices, boolean orientado) {
        this.numVertices = numVertices;
        this.orientado = orientado;
        this.adjacencias = new ListaAdjacencia[numVertices];
        for (int i = 0; i < numVertices; i++) {
            this.adjacencias[i] = new ListaAdjacencia();
        }
        this.arestas = new Aresta[20];
        this.numArestas = 0;
        this.possuiPesoNegativo = false;
    }

    /**
     * Adiciona uma aresta mantendo a lista de adjacências ordenada em ordem
     * crescente.
     * 
     * @param u    Vértice de origem.
     * @param v    Vértice de destino.
     * @param peso Peso da aresta.
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
     * Dobra a capacidade do vetor interno de arestas quando necessário.
     */
    private void redimensionarVetorArestas() {
        Aresta[] novo = new Aresta[arestas.length * 2];
        for (int i = 0; i < numArestas; i++) {
            novo[i] = arestas[i];
        }
        arestas = novo;
    }

    /**
     * Remove todas as marcações de destaque das arestas do grafo.
     */
    public void limparDestaquesArestas() {
        for (int i = 0; i < numArestas; i++) {
            arestas[i].setDestacada(false);
        }
    }

    public int getNumVertices() {
        return numVertices;
    }

    public boolean isOrientado() {
        return orientado;
    }

    public ListaAdjacencia getAdjacencia(int v) {
        return adjacencias[v];
    }

    public Aresta[] getArestas() {
        return arestas;
    }

    public int getNumArestas() {
        return numArestas;
    }

    public boolean isPossuiPesoNegativo() {
        return possuiPesoNegativo;
    }
}