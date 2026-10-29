package algorithms;

import model.Grafo;

/**
 * Fachada (Facade) que centraliza e unifica o acesso aos algoritmos de grafos.
 */
public class ServicoAlgoritmosGrafo {

    private final AlgoritmoDFS dfs = new AlgoritmoDFS();
    private final AlgoritmoBFS bfs = new AlgoritmoBFS();
    private final AlgoritmoOrdenacaoTopologica topologico = new AlgoritmoOrdenacaoTopologica();
    private final AlgoritmoKosaraju kosaraju = new AlgoritmoKosaraju();
    private final AlgoritmoDijkstra dijkstra = new AlgoritmoDijkstra();
    private final AlgoritmoKruskal kruskal = new AlgoritmoKruskal();

    public ResultadoAlgoritmo executarDFS(Grafo g, int origem) {
        return dfs.executar(g, origem);
    }

    public ResultadoAlgoritmo executarBFS(Grafo g, int origem) {
        return bfs.executar(g, origem);
    }

    public ResultadoAlgoritmo executarOrdenacaoTopologica(Grafo g) {
        return topologico.executar(g);
    }

    public ResultadoAlgoritmo executarComponentesFortementeConexas(Grafo g) {
        return kosaraju.executar(g);
    }

    public String validarAplicabilidadeDijkstra(Grafo g) {
        return dijkstra.validarAplicabilidade(g);
    }

    public ResultadoAlgoritmo executarDijkstra(Grafo g, int origem) {
        return dijkstra.executar(g, origem);
    }

    public ResultadoAlgoritmo executarKruskal(Grafo g) {
        return kruskal.executar(g);
    }
}
