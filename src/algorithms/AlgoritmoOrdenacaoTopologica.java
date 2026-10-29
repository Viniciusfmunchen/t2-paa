package algorithms;

import model.Grafo;
import structures.NoAdjacencia;
import structures.PilhaInt;

/**
 * Implementacao da Ordenacao Topologica para Grafos Orientados Aciclicos (DAG),
 * utilizando verificacao de ciclos baseada em coloracao de vertices por DFS.
 */
public class AlgoritmoOrdenacaoTopologica {

    public AlgoritmoOrdenacaoTopologica() {
    }

    /**
     * Executa a Ordenacao Topologica sobre o grafo informado.
     *
     * Pré-condição: grafo nao deve ser nulo, deve ser orientado e nao conter ciclos (DAG).
     * Pós-condição: Retorna a ordenacao linear dos vertices respeitando a precedencia das arestas,
     * ou mensagem de erro caso o grafo seja nao-orientado ou contenha ciclos.
     *
     * @param grafo Grafo orientado a ser ordenado topologicamente.
     * @return ResultadoAlgoritmo contendo a sequencia ordenada no formato "u - v - w" ou mensagem de restricao.
     */
    public ResultadoAlgoritmo executar(Grafo grafo) {
        if (!grafo.isOrientado()) {
            return ResultadoAlgoritmo.erro("Algoritmo não aplicável: A Ordenação Topológica exige grafo orientado.");
        }

        int n = grafo.getNumVertices();
        int[] estado = new int[n];
        PilhaInt pilha = new PilhaInt(n);

        for (int i = 0; i < n; i++) {
            if (estado[i] == 0 && possuiCiclo(grafo, i, estado, pilha)) {
                return ResultadoAlgoritmo.erro("Algoritmo não aplicável: O grafo orientado contém ciclo(s) (não é DAG).");
            }
        }

        int[] ordem = new int[n];
        int idx = 0;
        while (!pilha.estaVazia()) {
            ordem[idx++] = pilha.desempilhar();
        }

        return ResultadoAlgoritmo.sucesso(ResultadoAlgoritmo.formatarSequencia(ordem, idx));
    }

    /**
     * Detecta recursivamente a existencia de ciclos no grafo e empilha os vertices ao termino do processamento.
     *
     * Pré-condição: grafo, estado e pilha nao devem ser nulos; u e o vertice sob analise;
     * estado[u] assume 0 para nao visitado, 1 para em processamento e 2 para finalizado.
     * Pós-condição: Retorna true se encontrar aresta de retorno para no com estado 1;
     * caso contrario, marca u como finalizado (estado 2), empilha u e retorna false.
     *
     * @param grafo  Grafo sob analise.
     * @param u      Vertice atualmente explorado.
     * @param estado Vetor de estados de visitacao de cada vertice.
     * @param pilha  Pilha utilizada para armazenar a ordenacao pos-visitacao.
     * @return true se um ciclo for detectado, false caso o caminho seja aciclico.
     */
    private boolean possuiCiclo(Grafo grafo, int u, int[] estado, PilhaInt pilha) {
        estado[u] = 1;
        NoAdjacencia atual = grafo.getAdjacencia(u).getCabeca();
        while (atual != null) {
            int v = atual.getDestino();
            if (estado[v] == 1 || (estado[v] == 0 && possuiCiclo(grafo, v, estado, pilha))) {
                return true;
            }
            atual = atual.getProximo();
        }
        estado[u] = 2;
        pilha.empilhar(u);
        return false;
    }
}
