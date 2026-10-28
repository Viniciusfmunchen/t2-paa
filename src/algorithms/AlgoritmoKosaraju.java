package algorithms;

import model.Grafo;
import structures.NoAdjacencia;
import structures.PilhaInt;

/**
 * Implementação do Algoritmo de Kosaraju para detecção de Componentes
 * Fortemente Conexas (CFC) em grafos orientados.
 */
public class AlgoritmoKosaraju {

    private static final String[] PALETA_CORES_HEX = {
            "#FFB3BA", "#BAE1FF", "#BAFFC9", "#FFFFBA", "#FFDFBA",
            "#E0BBE4", "#957DAD", "#D291BC", "#FEC8D8", "#FFDFD3"
    };

    /**
     * Executa a identificação das Componentes Fortemente Conexas ordenadas por tamanho.
     *
     * @param grafo Grafo orientado a ser analisado.
     * @return Resultado contendo a lista formatada de componentes e mapeamento de cores.
     */
    public ResultadoAlgoritmo executar(Grafo grafo) {
        if (!grafo.isOrientado()) {
            return new ResultadoAlgoritmo(false,
                    "Algoritmo não aplicável: Componentes Fortemente Conexas exigem um grafo orientado.", null, false);
        }

        int n = grafo.getNumVertices();
        boolean[] visitado = new boolean[n];
        PilhaInt pilha = new PilhaInt(n);

        // Passo 1: Ordenação por tempo de término no grafo original
        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                empilharPorOrdemDeTermino(grafo, i, visitado, pilha);
            }
        }

        // Passo 2: Construir o grafo transposto
        Grafo transposto = construirGrafoTransposto(grafo);
        for (int i = 0; i < n; i++) {
            visitado[i] = false;
        }

        // Passo 3: Identificar componentes no grafo transposto
        int[][] componentes = new int[n][n];
        int[] tamanhos = new int[n];
        int qtdComp = 0;

        while (!pilha.estaVazia()) {
            int u = pilha.desempilhar();
            if (!visitado[u]) {
                int[] tamAtual = { 0 };
                coletarComponenteTransposto(transposto, u, visitado, componentes[qtdComp], tamAtual);
                tamanhos[qtdComp] = tamAtual[0];
                ordenarCrescente(componentes[qtdComp], tamanhos[qtdComp]);
                qtdComp++;
            }
        }

        // Passo 4: Ordenar componentes por tamanho decrescente
        ordenarComponentesPorTamanho(componentes, tamanhos, qtdComp);

        // Passo 5: Gerar formatação de saída e atribuição de cores
        return formatarResultadoCFC(n, componentes, tamanhos, qtdComp);
    }

    /**
     * Passo 1: Empilha vértices conforme término da exploração DFS.
     */
    private void empilharPorOrdemDeTermino(Grafo g, int u, boolean[] visitado, PilhaInt pilha) {
        visitado[u] = true;
        NoAdjacencia atual = g.getAdjacencia(u).getCabeca();
        while (atual != null) {
            if (!visitado[atual.getDestino()]) {
                empilharPorOrdemDeTermino(g, atual.getDestino(), visitado, pilha);
            }
            atual = atual.getProximo();
        }
        pilha.empilhar(u);
    }

    /**
     * Passo 2: Constrói o grafo com todas as arestas invertidas.
     */
    private Grafo construirGrafoTransposto(Grafo original) {
        Grafo gT = new Grafo(original.getNumVertices(), true);
        for (int u = 0; u < original.getNumVertices(); u++) {
            NoAdjacencia atual = original.getAdjacencia(u).getCabeca();
            while (atual != null) {
                gT.adicionarAresta(atual.getDestino(), u, atual.getPeso());
                atual = atual.getProximo();
            }
        }
        return gT;
    }

    /**
     * Passo 3: Coleta todos os vértices atingíveis a partir de u no grafo transposto.
     */
    private void coletarComponenteTransposto(Grafo gT, int u, boolean[] visitado, int[] comp, int[] tam) {
        visitado[u] = true;
        comp[tam[0]++] = u;
        NoAdjacencia atual = gT.getAdjacencia(u).getCabeca();
        while (atual != null) {
            if (!visitado[atual.getDestino()]) {
                coletarComponenteTransposto(gT, atual.getDestino(), visitado, comp, tam);
            }
            atual = atual.getProximo();
        }
    }

    /**
     * Ordena as componentes em ordem decrescente de tamanho, desempantando pelo menor índice.
     */
    private void ordenarComponentesPorTamanho(int[][] componentes, int[] tamanhos, int qtdComp) {
        for (int i = 0; i < qtdComp - 1; i++) {
            for (int j = 0; j < qtdComp - 1 - i; j++) {
                if (tamanhos[j] < tamanhos[j + 1] ||
                        (tamanhos[j] == tamanhos[j + 1] && componentes[j][0] > componentes[j + 1][0])) {
                    int tempTam = tamanhos[j];
                    tamanhos[j] = tamanhos[j + 1];
                    tamanhos[j + 1] = tempTam;

                    int[] tempComp = componentes[j];
                    componentes[j] = componentes[j + 1];
                    componentes[j + 1] = tempComp;
                }
            }
        }
    }

    /**
     * Ordena de forma crescente um vetor de inteiros.
     */
    private void ordenarCrescente(int[] vetor, int tam) {
        for (int i = 0; i < tam - 1; i++) {
            for (int j = 0; j < tam - 1 - i; j++) {
                if (vetor[j] > vetor[j + 1]) {
                    int tmp = vetor[j];
                    vetor[j] = vetor[j + 1];
                    vetor[j + 1] = tmp;
                }
            }
        }
    }

    /**
     * Monta a formatação textual e mapa de cores das Componentes Fortemente Conexas.
     */
    private ResultadoAlgoritmo formatarResultadoCFC(int n, int[][] componentes, int[] tamanhos, int qtdComp) {
        String[] coresVertices = new String[n];
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < qtdComp; i++) {
            sb.append("Componente ").append(i + 1).append(": ");
            String cor = PALETA_CORES_HEX[i % PALETA_CORES_HEX.length];
            for (int j = 0; j < tamanhos[i]; j++) {
                int v = componentes[i][j];
                coresVertices[v] = cor;
                sb.append(v);
                if (j < tamanhos[i] - 1) {
                    sb.append(", ");
                }
            }
            if (i < qtdComp - 1) {
                sb.append("\n");
            }
        }

        return new ResultadoAlgoritmo(true, sb.toString(), coresVertices, false);
    }
}

