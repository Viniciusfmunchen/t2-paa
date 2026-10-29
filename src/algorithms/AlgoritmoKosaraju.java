package algorithms;

import model.Grafo;
import structures.NoAdjacencia;
import structures.PilhaInt;

/**
 * Algoritmo de Kosaraju para detecção de Componentes Fortemente Conexas (CFC).
 */
public class AlgoritmoKosaraju {

    private static final String[] PALETA_CORES_HEX = {
            "#FFB3BA", "#BAE1FF", "#BAFFC9", "#FFFFBA", "#FFDFBA",
            "#E0BBE4", "#957DAD", "#D291BC", "#FEC8D8", "#FFDFD3"
    };

    /**
     * Identifica as componentes fortemente conexas ordenadas por tamanho decrescente.
     */
    public ResultadoAlgoritmo executar(Grafo grafo) {
        if (!grafo.isOrientado()) {
            return ResultadoAlgoritmo.erro("Algoritmo não aplicável: Componentes Fortemente Conexas exigem grafo orientado.");
        }

        int n = grafo.getNumVertices();
        boolean[] visitado = new boolean[n];
        PilhaInt pilha = new PilhaInt(n);

        // 1. DFS para ordenação por tempo de término
        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                dfsTermino(grafo, i, visitado, pilha);
            }
        }

        // 2. Construção do grafo transposto
        Grafo transposto = construirTransposto(grafo);
        for (int i = 0; i < n; i++) {
            visitado[i] = false;
        }

        // 3. Coleta de componentes no transposto
        int[][] componentes = new int[n][n];
        int[] tamanhos = new int[n];
        int qtdComp = 0;

        while (!pilha.estaVazia()) {
            int u = pilha.desempilhar();
            if (!visitado[u]) {
                int[] tam = { 0 };
                dfsColeta(transposto, u, visitado, componentes[qtdComp], tam);
                tamanhos[qtdComp] = tam[0];
                ordenarVetor(componentes[qtdComp], tam[0]);
                qtdComp++;
            }
        }

        // 4. Ordenar componentes por tamanho decrescente (desempate por menor índice inicial)
        for (int i = 0; i < qtdComp - 1; i++) {
            for (int j = 0; j < qtdComp - 1 - i; j++) {
                if (tamanhos[j] < tamanhos[j + 1] ||
                        (tamanhos[j] == tamanhos[j + 1] && componentes[j][0] > componentes[j + 1][0])) {
                    int tmpT = tamanhos[j];
                    tamanhos[j] = tamanhos[j + 1];
                    tamanhos[j + 1] = tmpT;
                    int[] tmpC = componentes[j];
                    componentes[j] = componentes[j + 1];
                    componentes[j + 1] = tmpC;
                }
            }
        }

        // 5. Formatação do resultado e atribuição de cores distintas
        String[] coresVertices = new String[n];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < qtdComp; i++) {
            sb.append("Componente ").append(i + 1).append(":  ");
            String cor = PALETA_CORES_HEX[i % PALETA_CORES_HEX.length];
            for (int j = 0; j < tamanhos[i]; j++) {
                int v = componentes[i][j];
                coresVertices[v] = cor;
                sb.append(v).append(j < tamanhos[i] - 1 ? ", " : "");
            }
            if (i < qtdComp - 1) {
                sb.append("\n");
            }
        }

        return new ResultadoAlgoritmo(true, sb.toString(), coresVertices, false);
    }

    private void dfsTermino(Grafo g, int u, boolean[] visitado, PilhaInt pilha) {
        visitado[u] = true;
        for (NoAdjacencia at = g.getAdjacencia(u).getCabeca(); at != null; at = at.getProximo()) {
            if (!visitado[at.getDestino()]) {
                dfsTermino(g, at.getDestino(), visitado, pilha);
            }
        }
        pilha.empilhar(u);
    }

    private Grafo construirTransposto(Grafo g) {
        Grafo gT = new Grafo(g.getNumVertices(), true);
        for (int u = 0; u < g.getNumVertices(); u++) {
            for (NoAdjacencia at = g.getAdjacencia(u).getCabeca(); at != null; at = at.getProximo()) {
                gT.adicionarAresta(at.getDestino(), u, at.getPeso());
            }
        }
        return gT;
    }

    private void dfsColeta(Grafo gT, int u, boolean[] visitado, int[] comp, int[] tam) {
        visitado[u] = true;
        comp[tam[0]++] = u;
        for (NoAdjacencia at = gT.getAdjacencia(u).getCabeca(); at != null; at = at.getProximo()) {
            if (!visitado[at.getDestino()]) {
                dfsColeta(gT, at.getDestino(), visitado, comp, tam);
            }
        }
    }

    private void ordenarVetor(int[] vet, int tam) {
        for (int i = 0; i < tam - 1; i++) {
            for (int j = 0; j < tam - 1 - i; j++) {
                if (vet[j] > vet[j + 1]) {
                    int t = vet[j];
                    vet[j] = vet[j + 1];
                    vet[j + 1] = t;
                }
            }
        }
    }
}
