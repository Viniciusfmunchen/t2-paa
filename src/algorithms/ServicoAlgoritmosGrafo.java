package algorithms;

import model.Grafo;

/**
 * Fachada (Design Pattern Facade) que centraliza e unifica o acesso aos
 * algoritmos de grafos da aplicação.
 *
 * Cada algoritmo possui sua própria classe especializada e encapsulada no pacote
 * {@code algorithms}.
 */
public class ServicoAlgoritmosGrafo {

    private final AlgoritmoDFS algoritmoDFS;
    private final AlgoritmoBFS algoritmoBFS;
    private final AlgoritmoOrdenacaoTopologica algoritmoTopologico;
    private final AlgoritmoKosaraju algoritmoKosaraju;
    private final AlgoritmoDijkstra algoritmoDijkstra;
    private final AlgoritmoKruskal algoritmoKruskal;

    public ServicoAlgoritmosGrafo() {
        this.algoritmoDFS = new AlgoritmoDFS();
        this.algoritmoBFS = new AlgoritmoBFS();
        this.algoritmoTopologico = new AlgoritmoOrdenacaoTopologica();
        this.algoritmoKosaraju = new AlgoritmoKosaraju();
        this.algoritmoDijkstra = new AlgoritmoDijkstra();
        this.algoritmoKruskal = new AlgoritmoKruskal();
    }

    /**
     * b) Executa a Busca em Profundidade (DFS).
     */
    public ResultadoAlgoritmo executarDFS(Grafo grafo, int origem) {
        return algoritmoDFS.executar(grafo, origem);
    }

    /**
     * c) Executa a Busca em Largura (BFS).
     */
    public ResultadoAlgoritmo executarBFS(Grafo grafo, int origem) {
        return algoritmoBFS.executar(grafo, origem);
    }

    /**
     * d) Executa a Ordenação Topológica.
     */
    public ResultadoAlgoritmo executarOrdenacaoTopologica(Grafo grafo) {
        return algoritmoTopologico.executar(grafo);
    }

    /**
     * e) Executa Componentes Fortemente Conexas (Kosaraju).
     */
    public ResultadoAlgoritmo executarComponentesFortementeConexas(Grafo grafo) {
        return algoritmoKosaraju.executar(grafo);
    }

    /**
     * Valida os pré-requisitos para execução do Dijkstra.
     */
    public String validarAplicabilidadeDijkstra(Grafo grafo) {
        return algoritmoDijkstra.validarAplicabilidade(grafo);
    }

    /**
     * f) Executa o algoritmo de caminhos mínimos de Dijkstra.
     */
    public ResultadoAlgoritmo executarDijkstra(Grafo grafo, int origem) {
        return algoritmoDijkstra.executar(grafo, origem);
    }

    /**
     * g) Executa o algoritmo de Kruskal para Árvore Geradora Mínima (AGM).
     */
    public ResultadoAlgoritmo executarKruskal(Grafo grafo) {
        return algoritmoKruskal.executar(grafo);
    }
}
