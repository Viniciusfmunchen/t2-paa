package algorithms;

import model.Grafo;
import structures.NoAdjacencia;

/**
 * Implementação do Algoritmo de Dijkstra para cálculo de caminhos mínimos
 * a partir de uma origem em grafos orientados sem pesos negativos.
 */
public class AlgoritmoDijkstra {

    private static final int INFINITO = Integer.MAX_VALUE;

    /**
     * Valida os pré-requisitos para execução do algoritmo de Dijkstra.
     *
     * @param grafo Grafo a ser validado.
     * @return Mensagem explicativa em caso de erro, ou null se válido.
     */
    public String validarAplicabilidade(Grafo grafo) {
        if (!grafo.isOrientado()) {
            return "Algoritmo não aplicável: A especificação exige que o grafo seja orientado para o Dijkstra.";
        }
        if (grafo.isPossuiPesoNegativo()) {
            return "Algoritmo não aplicável: O algoritmo de Dijkstra não suporta arestas com peso negativo.";
        }
        return null;
    }

    /**
     * Executa o algoritmo de Dijkstra calculando as distâncias mínimas da origem até todos os vértices.
     *
     * @param grafo  Grafo direcionado com pesos não-negativos.
     * @param origem Vértice de partida.
     * @return Resultado contendo as distâncias mínimas calculadas.
     */
    public ResultadoAlgoritmo executar(Grafo grafo, int origem) {
        String erroValidacao = validarAplicabilidade(grafo);
        if (erroValidacao != null) {
            return new ResultadoAlgoritmo(false, erroValidacao, null, false);
        }

        int n = grafo.getNumVertices();
        if (origem < 0 || origem >= n) {
            return new ResultadoAlgoritmo(false, "Erro: O vértice de origem deve estar entre 0 e " + (n - 1) + ".",
                    null, false);
        }

        int[] dist = new int[n];
        boolean[] fechado = new boolean[n];

        for (int i = 0; i < n; i++) {
            dist[i] = INFINITO;
        }
        dist[origem] = 0;

        for (int count = 0; count < n; count++) {
            int u = -1;
            int menor = INFINITO;
            for (int v = 0; v < n; v++) {
                if (!fechado[v] && dist[v] < menor) {
                    menor = dist[v];
                    u = v;
                }
            }

            if (u == -1) {
                break;
            }
            fechado[u] = true;

            NoAdjacencia atual = grafo.getAdjacencia(u).getCabeca();
            while (atual != null) {
                int v = atual.getDestino();
                int peso = atual.getPeso();
                if (!fechado[v] && dist[u] != INFINITO && dist[u] + peso < dist[v]) {
                    dist[v] = dist[u] + peso;
                }
                atual = atual.getProximo();
            }
        }

        String saida = formatarSaidaDijkstra(dist, origem, n);
        return new ResultadoAlgoritmo(true, saida, null, false);
    }

    private String formatarSaidaDijkstra(int[] dist, int origem, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(origem).append(" -> ").append(i).append(": ");
            if (dist[i] == INFINITO) {
                sb.append("INFINITO");
            } else {
                sb.append(dist[i]);
            }
            if (i < n - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}

