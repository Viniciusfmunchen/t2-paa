package algorithms;

import model.Grafo;

/**
 * Fachada (Facade) que centraliza o acesso e a invocacao dos diversos
 * algoritmos implementados sobre grafos.
 */
public class ServicoAlgoritmosGrafo {

    private final AlgoritmoDFS dfs = new AlgoritmoDFS();
    private final AlgoritmoBFS bfs = new AlgoritmoBFS();
    private final AlgoritmoOrdenacaoTopologica topologico = new AlgoritmoOrdenacaoTopologica();
    private final AlgoritmoKosaraju kosaraju = new AlgoritmoKosaraju();
    private final AlgoritmoDijkstra dijkstra = new AlgoritmoDijkstra();
    private final AlgoritmoKruskal kruskal = new AlgoritmoKruskal();

    public ServicoAlgoritmosGrafo() {
    }

    /**
     * Executa a Busca em Profundidade (DFS) a partir do vertice de origem especificado.
     *
     * Pré-condição: g nao deve ser nulo e origem deve ser um vertice valido.
     * Pós-condição: Retorna o resultado da travessia em profundidade contendo a sequencia formatada.
     *
     * @param g      Grafo a ser percorrido.
     * @param origem Vertice de partida da busca.
     * @return O ResultadoAlgoritmo gerado pela DFS.
     */
    public ResultadoAlgoritmo executarDFS(Grafo g, int origem) {
        return dfs.executar(g, origem);
    }

    /**
     * Executa a Busca em Largura (BFS) a partir do vertice de origem especificado.
     *
     * Pré-condição: g nao deve ser nulo e origem deve ser um vertice valido.
     * Pós-condição: Retorna o resultado da exploracao em camadas contendo a sequencia formatada.
     *
     * @param g      Grafo a ser percorrido.
     * @param origem Vertice de partida da busca.
     * @return O ResultadoAlgoritmo gerado pela BFS.
     */
    public ResultadoAlgoritmo executarBFS(Grafo g, int origem) {
        return bfs.executar(g, origem);
    }

    /**
     * Executa a Ordenacao Topologica sobre o grafo informado.
     *
     * Pré-condição: g nao deve ser nulo.
     * Pós-condição: Retorna a sequencia topologica se o grafo for um DAG orientado, ou erro explicativo caso contrario.
     *
     * @param g Grafo a ser ordenado topologicamente.
     * @return O ResultadoAlgoritmo com a ordem linear ou mensagem de restricao.
     */
    public ResultadoAlgoritmo executarOrdenacaoTopologica(Grafo g) {
        return topologico.executar(g);
    }

    /**
     * Executa a deteccao de Componentes Fortemente Conexas (CFC) via algoritmo de Kosaraju.
     *
     * Pré-condição: g nao deve ser nulo.
     * Pós-condição: Retorna as componentes fortemente conexas particionadas e as cores visuais atribuidas.
     *
     * @param g Grafo orientado a ser analisado.
     * @return O ResultadoAlgoritmo contendo as CFCs identificadas.
     */
    public ResultadoAlgoritmo executarComponentesFortementeConexas(Grafo g) {
        return kosaraju.executar(g);
    }

    /**
     * Valida a aplicabilidade das pre-condicoes do algoritmo de Dijkstra para o grafo.
     *
     * Pré-condição: g nao deve ser nulo.
     * Pós-condição: Retorna null se o grafo atender a todos os criterios (orientado e sem pesos negativos);
     * caso contrario, retorna uma mensagem detalhando a restricao.
     *
     * @param g Grafo sob verificacao.
     * @return Mensagem de incompatibilidade ou null caso seja valido.
     */
    public String validarAplicabilidadeDijkstra(Grafo g) {
        return dijkstra.validarAplicabilidade(g);
    }

    /**
     * Executa o algoritmo de Dijkstra calculando os caminhos minimos a partir do vertice de origem.
     *
     * Pré-condição: g nao deve ser nulo, deve ser orientado sem pesos negativos e origem deve ser valido.
     * Pós-condição: Retorna as distancias minimas calculadas de origem para todos os vertices do grafo.
     *
     * @param g      Grafo sobre o qual o algoritmo e executado.
     * @param origem Vertice de origem das distancias.
     * @return O ResultadoAlgoritmo contendo o mapa de custos minimos.
     */
    public ResultadoAlgoritmo executarDijkstra(Grafo g, int origem) {
        return dijkstra.executar(g, origem);
    }

    /**
     * Executa o algoritmo de Kruskal para obter a Arvore Geradora Minima (AGM) do grafo.
     *
     * Pré-condição: g nao deve ser nulo.
     * Pós-condição: Retorna as arestas componentes da AGM e o peso total caso o grafo seja nao-orientado.
     *
     * @param g Grafo nao-orientado ponderado.
     * @return O ResultadoAlgoritmo contendo a composicao da AGM e instrucao de realce visual.
     */
    public ResultadoAlgoritmo executarKruskal(Grafo g) {
        return kruskal.executar(g);
    }
}
