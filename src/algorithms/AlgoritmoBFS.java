package algorithms;

import model.Grafo;
import structures.FilaInt;
import structures.NoAdjacencia;

/**
 * Implementação da Busca em Largura (BFS).
 * Explora todos os vértices do grafo em camadas, mesmo se desconexo.
 */
public class AlgoritmoBFS {

    /**
     * Executa a BFS a partir da origem informada.
     */
    public ResultadoAlgoritmo executar(Grafo grafo, int origem) {
        int n = grafo.getNumVertices();
        if (origem < 0 || origem >= n) {
            return ResultadoAlgoritmo.erro("Erro: O vértice de origem deve estar entre 0 e " + (n - 1) + ".");
        }

        boolean[] visitado = new boolean[n];
        int[] ordem = new int[n];
        int contador = 0;
        FilaInt fila = new FilaInt(n);

        contador = explorarComponente(grafo, origem, visitado, ordem, contador, fila);

        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                contador = explorarComponente(grafo, i, visitado, ordem, contador, fila);
            }
        }

        return ResultadoAlgoritmo.sucesso(ResultadoAlgoritmo.formatarSequencia(ordem, contador));
    }

    private int explorarComponente(Grafo grafo, int inicio, boolean[] visitado, int[] ordem, int contador, FilaInt fila) {
        visitado[inicio] = true;
        fila.enfileirar(inicio);

        while (!fila.estaVazia()) {
            int u = fila.desenfileirar();
            ordem[contador++] = u;

            NoAdjacencia atual = grafo.getAdjacencia(u).getCabeca();
            while (atual != null) {
                int v = atual.getDestino();
                if (!visitado[v]) {
                    visitado[v] = true;
                    fila.enfileirar(v);
                }
                atual = atual.getProximo();
            }
        }
        return contador;
    }
}
