package algorithms;

import model.Grafo;
import structures.NoAdjacencia;

/**
 * Algoritmo de Dijkstra para calculo dos caminhos minimos de fonte unica
 * em grafos orientados ponderados com pesos nao-negativos.
 */
public class AlgoritmoDijkstra {

    private static final int INFINITO = Integer.MAX_VALUE;

    public AlgoritmoDijkstra() {
    }

    /**
     * Verifica as pre-condicoes teoricas para a aplicacao do algoritmo de Dijkstra sobre o grafo.
     *
     * Pré-condição: grafo nao deve ser nulo.
     * Pós-condição: Retorna null se o grafo for compativel (orientado e sem pesos negativos);
     * caso contrario, retorna a mensagem explicativa do impedimento.
     *
     * @param grafo Grafo a ser avaliado.
     * @return Mensagem de erro descrevendo a incompatibilidade, ou null se for aplicavel.
     */
    public String validarAplicabilidade(Grafo grafo) {
        if (!grafo.isOrientado()) {
            return "Algoritmo não aplicável: O algoritmo de Dijkstra exige grafo orientado.";
        }
        if (grafo.isPossuiPesoNegativo()) {
            return "Algoritmo não aplicável: O algoritmo de Dijkstra não suporta arestas com peso negativo.";
        }
        return null;
    }

    /**
     * Executa o algoritmo de Dijkstra calculando os custos minimos da origem para todos os demais vertices.
     *
     * Pré-condição: grafo deve ser orientado, sem pesos negativos e origem deve estar no intervalo [0, grafo.getNumVertices() - 1].
     * Pós-condição: Retorna o resultado com as distancias minimas calculadas no formato "origem -> destino: distancia",
     * indicando INFINITO para vertices inalcancaveis a partir da origem.
     *
     * @param grafo  Grafo orientado ponderado sem pesos negativos.
     * @param origem Vertice de partida para calculo dos caminhos minimos.
     * @return ResultadoAlgoritmo contendo as distancias minimas ou mensagem de restricao.
     */
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
            if (i < n - 1) {
                sb.append("\n");
            }
        }
        return ResultadoAlgoritmo.sucesso(sb.toString());
    }
}
