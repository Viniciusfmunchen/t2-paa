package algorithms;

import model.Grafo;
import structures.NoAdjacencia;

/**
 * Implementacao do algoritmo de Busca em Profundidade (DFS - Depth-First Search).
 * Explora os caminhos do grafo recursivamente a partir de um vertice inicial,
 * cobrindo todo o grafo mesmo se desconexo.
 */
public class AlgoritmoDFS {

    public AlgoritmoDFS() {
    }

    /**
     * Executa a Busca em Profundidade a partir do vertice de origem informado.
     *
     * Pré-condição: grafo nao deve ser nulo e origem deve estar no intervalo [0, grafo.getNumVertices() - 1].
     * Pós-condição: Retorna o resultado com a ordem de visitacao formatada no padrao "u - v - w",
     * cobrindo a componente da origem e as demais componentes desconexas.
     *
     * @param grafo  Grafo a ser percorrido.
     * @param origem Vertice inicial da busca.
     * @return ResultadoAlgoritmo com a sequencia de visitacao ou mensagem de erro se a origem for invalida.
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

    /**
     * Realiza a exploracao recursiva em profundidade a partir do vertice u.
     *
     * Pré-condição: grafo, visitado, ordem e contador nao devem ser nulos; u deve ser um vertice valido.
     * Pós-condição: O vertice u e seus descendentes nao visitados sao marcados e registrados no arranjo ordem.
     *
     * @param grafo    Grafo sob analise.
     * @param u        Vertice atualmente explorado.
     * @param visitado Arranjo de sinalizacao de vertices visitados.
     * @param ordem    Arranjo de registro da sequencia de visitacao.
     * @param contador Vetor mantendo a quantidade e posicao corrente no vetor de ordem.
     */
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
