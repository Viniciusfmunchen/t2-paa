package algorithms;

import model.Grafo;
import structures.FilaInt;
import structures.NoAdjacencia;

/**
 * Implementacao do algoritmo de Busca em Largura (BFS - Breadth-First Search).
 * Explora os vertices em camadas a partir de um vertice de origem, cobrindo
 * todo o grafo mesmo se desconexo.
 */
public class AlgoritmoBFS {
    
    public AlgoritmoBFS() {
    }

    /**
     * Executa a Busca em Largura a partir do vertice de origem especificado.
     *
     * Pré-condição: grafo nao deve ser nulo e origem deve estar no intervalo [0, grafo.getNumVertices() - 1].
     * Pós-condição: Retorna o resultado com a ordem de visitacao formatada no padrao "u - v - w",
     * explorando a componente da origem e as eventuais componentes desconexas.
     *
     * @param grafo  Grafo a ser percorrido.
     * @param origem Vertice de inicio da exploracao.
     * @return ResultadoAlgoritmo contendo a sequencia de visitacao ou mensagem de erro se a origem for invalida.
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

    /**
     * Explora em largura uma unica componente conexa a partir de um vertice inicial usando fila circular.
     *
     * Pré-condição: grafo, visitado, ordem e fila nao devem ser nulos; inicio deve ser um vertice nao visitado.
     * Pós-condição: Todos os vertices alcancaveis a partir de inicio sao marcados como visitados e registrados em ordem.
     *
     * @param grafo    Grafo sob analise.
     * @param inicio   Vertice de partida da componente.
     * @param visitado Arranjo de sinalizacao de vertices visitados.
     * @param ordem    Arranjo de registro da ordem de visitacao.
     * @param contador Quantidade atual de vertices inseridos no vetor de ordem.
     * @param fila     Fila circular utilizada para controle da exploracao em camadas.
     * @return O novo total de vertices registrados no vetor de ordem.
     */
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
