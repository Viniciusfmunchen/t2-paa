package algorithms;

import model.Grafo;
import structures.NoAdjacencia;

/**
 * Implementação da Busca em Profundidade (DFS).
 * Garante a exploração de todas as componentes, mesmo em grafos desconexos.
 */
public class AlgoritmoDFS {

    /**
     * Executa a Busca em Profundidade a partir do vértice de origem informado.
     *
     * @param grafo  Grafo a ser percorrido.
     * @param origem Vértice inicial.
     * @return Resultado contendo a sequência de visitação formatada com hifens.
     */
    public ResultadoAlgoritmo executar(Grafo grafo, int origem) {
        int n = grafo.getNumVertices();
        if (origem < 0 || origem >= n) {
            return new ResultadoAlgoritmo(false, "Erro: O vértice de origem deve estar entre 0 e " + (n - 1) + ".",
                    null, false);
        }

        boolean[] visitado = new boolean[n];
        int[] ordem = new int[n];
        int[] contador = { 0 };

        // Explora a partir da origem
        dfsRecursivo(grafo, origem, visitado, ordem, contador);

        // Explora as demais componentes não alcançadas
        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                dfsRecursivo(grafo, i, visitado, ordem, contador);
            }
        }

        String saida = formatarSequenciaHifen(ordem, contador[0]);
        return new ResultadoAlgoritmo(true, saida, null, false);
    }

    private void dfsRecursivo(Grafo grafo, int u, boolean[] visitado, int[] ordem, int[] contador) {
        visitado[u] = true;
        ordem[contador[0]++] = u;

        NoAdjacencia atual = grafo.getAdjacencia(u).getCabeca();
        while (atual != null) {
            if (!visitado[atual.getDestino()]) {
                dfsRecursivo(grafo, atual.getDestino(), visitado, ordem, contador);
            }
            atual = atual.getProximo();
        }
    }

    private String formatarSequenciaHifen(int[] vetor, int tam) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tam; i++) {
            sb.append(vetor[i]);
            if (i < tam - 1) {
                sb.append("-");
            }
        }
        return sb.toString();
    }
}

