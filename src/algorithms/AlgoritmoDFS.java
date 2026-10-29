package algorithms;

import model.Grafo;
import structures.NoAdjacencia;

/**
 * Implementação da Busca em Profundidade (DFS).
 * Explora todos os vértices do grafo, mesmo se desconexo.
 */
public class AlgoritmoDFS {

    /**
     * Executa a DFS a partir da origem informada.
     */
    public ResultadoAlgoritmo executar(Grafo grafo, int origem) {
        int n = grafo.getNumVertices();
        if (origem < 0 || origem >= n) {
            return ResultadoAlgoritmo.erro("Erro: O vértice de origem deve estar entre 0 e " + (n - 1) + ".");
        }

        boolean[] visitado = new boolean[n];
        int[] ordem = new int[n];
        int[] contador = { 0 };

        dfsRecursivo(grafo, origem, visitado, ordem, contador);

        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                dfsRecursivo(grafo, i, visitado, ordem, contador);
            }
        }

        return ResultadoAlgoritmo.sucesso(ResultadoAlgoritmo.formatarSequencia(ordem, contador[0]));
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
}
