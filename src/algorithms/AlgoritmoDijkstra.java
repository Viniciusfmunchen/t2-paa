package algorithms;

import model.Grafo;
import structures.NoAdjacencia;

/**
 * Algoritmo de Dijkstra para cálculo de caminhos mínimos em grafos orientados
 * sem pesos negativos.
 */
public class AlgoritmoDijkstra {

    private static final int INFINITO = Integer.MAX_VALUE;

    public String validarAplicabilidade(Grafo grafo) {
        if (!grafo.isOrientado()) {
            return "Algoritmo não aplicável: O algoritmo de Dijkstra exige grafo orientado.";
        }
        if (grafo.isPossuiPesoNegativo()) {
            return "Algoritmo não aplicável: O algoritmo de Dijkstra não suporta arestas com peso negativo.";
        }
        return null;
    }

    public ResultadoAlgoritmo executar(Grafo grafo, int origem) {
        String erro = validarAplicabilidade(grafo);
        if (erro != null) {
            return ResultadoAlgoritmo.erro(erro);
        }

        int n = grafo.getNumVertices();
        if (origem < 0 || origem >= n) {
            return ResultadoAlgoritmo.erro("Erro: O vértice de origem deve estar entre 0 e " + (n - 1) + ".");
        }

        int[] dist = new int[n];
        boolean[] fechado = new boolean[n];
        for (int i = 0; i < n; i++) {
            dist[i] = INFINITO;
        }
        dist[origem] = 0;

        for (int count = 0; count < n; count++) {
            int u = -1, menor = INFINITO;
            for (int v = 0; v < n; v++) {
                if (!fechado[v] && dist[v] < menor) {
                    menor = dist[v];
                    u = v;
                }
            }
            if (u == -1) break;
            fechado[u] = true;

            for (NoAdjacencia at = grafo.getAdjacencia(u).getCabeca(); at != null; at = at.getProximo()) {
                int v = at.getDestino();
                if (!fechado[v] && dist[u] != INFINITO && dist[u] + at.getPeso() < dist[v]) {
                    dist[v] = dist[u] + at.getPeso();
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(origem).append(" -> ").append(i).append(": ")
              .append(dist[i] == INFINITO ? "INFINITO" : dist[i]);
            if (i < n - 1) sb.append("\n");
        }
        return ResultadoAlgoritmo.sucesso(sb.toString());
    }
}
