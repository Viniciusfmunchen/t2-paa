package algorithms;

import model.Aresta;
import model.Grafo;
import structures.FilaInt;
import structures.NoAdjacencia;
import structures.PilhaInt;
import structures.UnionFind;

/**
 * Serviço responsável pela validação de aplicabilidade e execução dos
 * algoritmos em grafos.
 */
public class ServicoAlgoritmosGrafo {

    /**
     * b) Executa a Busca em Profundidade (DFS) garantindo exploração total mesmo em
     * grafos desconexos.
     * 
     * @param grafo  Grafo carregado.
     * @param origem Vértice inicial da busca.
     * @return Resultado contendo a sequência de visitação.
     */
    public ResultadoAlgoritmo executarDFS(Grafo grafo, int origem) {
        int n = grafo.getNumVertices();
        if (origem < 0 || origem >= n) {
            return new ResultadoAlgoritmo(false, "Erro: O vértice de origem deve estar entre 0 e " + (n - 1) + ".",
                    null, false);
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

        String saida = formatarSequenciaHifen(ordem, contador[0]);
        return new ResultadoAlgoritmo(true, saida, null, false);
    }

    /**
     * Passo recursivo da Busca em Profundidade.
     * 
     * @param grafo    Instância do grafo.
     * @param u        Vértice em visitação.
     * @param visitado Vetor de controle de vértices visitados.
     * @param ordem    Vetor de saída.
     * @param contador Índice atual no vetor de saída.
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

    /**
     * c) Executa a Busca em Largura (BFS) garantindo exploração total mesmo em
     * grafos desconexos.
     * 
     * @param grafo  Grafo carregado.
     * @param origem Vértice inicial da busca.
     * @return Resultado contendo a sequência de visitação.
     */
    public ResultadoAlgoritmo executarBFS(Grafo grafo, int origem) {
        int n = grafo.getNumVertices();
        if (origem < 0 || origem >= n) {
            return new ResultadoAlgoritmo(false, "Erro: O vértice de origem deve estar entre 0 e " + (n - 1) + ".",
                    null, false);
        }

        boolean[] visitado = new boolean[n];
        int[] ordem = new int[n];
        int contador = 0;
        FilaInt fila = new FilaInt(n);

        visitado[origem] = true;
        fila.enfileirar(origem);

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

        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                visitado[i] = true;
                fila.enfileirar(i);
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
            }
        }

        String saida = formatarSequenciaHifen(ordem, contador);
        return new ResultadoAlgoritmo(true, saida, null, false);
    }

    /**
     * d) Executa a Ordenação Topológica em grafos orientados acíclicos (DAG).
     * 
     * @param grafo Grafo carregado.
     * @return Resultado contendo a ordem topológica ou mensagem de impedimento.
     */
    public ResultadoAlgoritmo executarOrdenacaoTopologica(Grafo grafo) {
        if (!grafo.isOrientado()) {
            return new ResultadoAlgoritmo(false,
                    "Algoritmo não aplicável: A Ordenação Topológica exige que o grafo seja orientado.", null, false);
        }

        int n = grafo.getNumVertices();
        int[] estado = new int[n]; // 0 = não visitado, 1 = em pilha de recursão, 2 = finalizado
        PilhaInt pilha = new PilhaInt(n);

        for (int i = 0; i < n; i++) {
            if (estado[i] == 0) {
                if (possuiCicloTopologico(grafo, i, estado, pilha)) {
                    return new ResultadoAlgoritmo(false,
                            "Algoritmo não aplicável: O grafo orientado contém ciclo(s), não sendo um DAG.", null,
                            false);
                }
            }
        }

        int[] ordem = new int[n];
        int idx = 0;
        while (!pilha.estaVazia()) {
            ordem[idx++] = pilha.desempilhar();
        }

        return new ResultadoAlgoritmo(true, formatarSequenciaHifen(ordem, idx), null, false);
    }

    /**
     * Auxiliar recursivo para detecção de ciclo e preenchimento da pilha
     * topológica.
     * 
     * @param grafo  Grafo analisado.
     * @param u      Vértice atual.
     * @param estado Vetor de cores/estados.
     * @param pilha  Pilha de finalização.
     * @return true se encontrar aresta de retorno (ciclo), false caso contrário.
     */
    private boolean possuiCicloTopologico(Grafo grafo, int u, int[] estado, PilhaInt pilha) {
        estado[u] = 1;
        NoAdjacencia atual = grafo.getAdjacencia(u).getCabeca();
        while (atual != null) {
            int v = atual.getDestino();
            if (estado[v] == 1)
                return true;
            if (estado[v] == 0 && possuiCicloTopologico(grafo, v, estado, pilha)) {
                return true;
            }
            atual = atual.getProximo();
        }
        estado[u] = 2;
        pilha.empilhar(u);
        return false;
    }

    /**
     * e) Calcula as Componentes Fortemente Conexas (Kosaraju) em ordem decrescente
     * de tamanho.
     * 
     * @param grafo Grafo orientado carregado.
     * @return Resultado textual e mapa hexadecimal de cores para os vértices de
     *         cada componente.
     */
    public ResultadoAlgoritmo executarComponentesFortementeConexas(Grafo grafo) {
        if (!grafo.isOrientado()) {
            return new ResultadoAlgoritmo(false,
                    "Algoritmo não aplicável: Componentes Fortemente Conexas exigem um grafo orientado.", null, false);
        }

        int n = grafo.getNumVertices();
        boolean[] visitado = new boolean[n];
        PilhaInt pilha = new PilhaInt(n);

        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                empilharPorOrdemDeTermino(grafo, i, visitado, pilha);
            }
        }

        Grafo transposto = construirGrafoTransposto(grafo);
        for (int i = 0; i < n; i++)
            visitado[i] = false;

        int[][] componentes = new int[n][n];
        int[] tamanhos = new int[n];
        int qtdComp = 0;

        while (!pilha.estaVazia()) {
            int u = pilha.desempilhar();
            if (!visitado[u]) {
                int[] tamAtual = { 0 };
                coletarComponenteTransposto(transposto, u, visitado, componentes[qtdComp], tamAtual);
                tamanhos[qtdComp] = tamAtual[0];
                ordenarCrescente(componentes[qtdComp], tamanhos[qtdComp]);
                qtdComp++;
            }
        }

        // Ordena as componentes em ordem decrescente de quantidade de vértices
        for (int i = 0; i < qtdComp - 1; i++) {
            for (int j = 0; j < qtdComp - 1 - i; j++) {
                if (tamanhos[j] < tamanhos[j + 1] ||
                        (tamanhos[j] == tamanhos[j + 1] && componentes[j][0] > componentes[j + 1][0])) {
                    int tempTam = tamanhos[j];
                    tamanhos[j] = tamanhos[j + 1];
                    tamanhos[j + 1] = tempTam;

                    int[] tempComp = componentes[j];
                    componentes[j] = componentes[j + 1];
                    componentes[j + 1] = tempComp;
                }
            }
        }

        String[] paletaHex = {
                "#FFB3BA", "#BAE1FF", "#BAFFC9", "#FFFFBA", "#FFDFBA",
                "#E0BBE4", "#957DAD", "#D291BC", "#FEC8D8", "#FFDFD3"
        };
        String[] coresVertices = new String[n];
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < qtdComp; i++) {
            sb.append("Componente ").append(i + 1).append(": ");
            String cor = paletaHex[i % paletaHex.length];
            for (int j = 0; j < tamanhos[i]; j++) {
                int v = componentes[i][j];
                coresVertices[v] = cor;
                sb.append(v);
                if (j < tamanhos[i] - 1)
                    sb.append(", ");
            }
            if (i < qtdComp - 1)
                sb.append("\n");
        }

        return new ResultadoAlgoritmo(true, sb.toString(), coresVertices, false);
    }

    /**
     * Passo 1 de Kosaraju: empilha vértices conforme término da exploração.
     */
    private void empilharPorOrdemDeTermino(Grafo g, int u, boolean[] visitado, PilhaInt pilha) {
        visitado[u] = true;
        NoAdjacencia atual = g.getAdjacencia(u).getCabeca();
        while (atual != null) {
            if (!visitado[atual.getDestino()]) {
                empilharPorOrdemDeTermino(g, atual.getDestino(), visitado, pilha);
            }
            atual = atual.getProximo();
        }
        pilha.empilhar(u);
    }

    /**
     * Passo 2 de Kosaraju: gera o grafo com orientação das arestas invertida.
     */
    private Grafo construirGrafoTransposto(Grafo original) {
        Grafo gT = new Grafo(original.getNumVertices(), true);
        for (int u = 0; u < original.getNumVertices(); u++) {
            NoAdjacencia atual = original.getAdjacencia(u).getCabeca();
            while (atual != null) {
                gT.adicionarAresta(atual.getDestino(), u, atual.getPeso());
                atual = atual.getProximo();
            }
        }
        return gT;
    }

    /**
     * Passo 3 de Kosaraju: coleta todos os vértices atingíveis no grafo transposto.
     */
    private void coletarComponenteTransposto(Grafo gT, int u, boolean[] visitado, int[] comp, int[] tam) {
        visitado[u] = true;
        comp[tam[0]++] = u;
        NoAdjacencia atual = gT.getAdjacencia(u).getCabeca();
        while (atual != null) {
            if (!visitado[atual.getDestino()]) {
                coletarComponenteTransposto(gT, atual.getDestino(), visitado, comp, tam);
            }
            atual = atual.getProximo();
        }
    }

    /**
     * f) Executa o algoritmo de Dijkstra para caminhos mínimos a partir de uma
     * origem em grafo orientado.
     * 
     * @param grafo  Grafo orientado sem pesos negativos.
     * @param origem Vértice inicial.
     * @return Distâncias mínimas da origem até cada vértice.
     */
    public ResultadoAlgoritmo executarDijkstra(Grafo grafo, int origem) {
        if (!grafo.isOrientado()) {
            return new ResultadoAlgoritmo(false,
                    "Algoritmo não aplicável: A especificação exige que o grafo seja orientado para o Dijkstra.", null,
                    false);
        }
        if (grafo.isPossuiPesoNegativo()) {
            return new ResultadoAlgoritmo(false,
                    "Algoritmo não aplicável: O algoritmo de Dijkstra não suporta arestas com peso negativo.", null,
                    false);
        }
        int n = grafo.getNumVertices();
        if (origem < 0 || origem >= n) {
            return new ResultadoAlgoritmo(false, "Erro: O vértice de origem deve estar entre 0 e " + (n - 1) + ".",
                    null, false);
        }

        int INFINITO = Integer.MAX_VALUE;
        int[] dist = new int[n];
        boolean[] fechado = new boolean[n];

        for (int i = 0; i < n; i++)
            dist[i] = INFINITO;
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

            if (u == -1)
                break;
            fechado[u] = true;

            NoAdjacencia atual = grafo.getAdjacencia(u).getCabeca();
            while (atual != null) {
                int v = atual.getDestino();
                int peso = atual.getPeso();
                if (!fechado[v] && dist[u] != INFINITO && dist[u] + peso < dist[v]) {
                    dist[v] = dist[u] + peso;
                }
                atual = atual.getProximo();
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(origem).append(" -> ").append(i).append(": ");
            if (dist[i] == INFINITO) {
                sb.append("INFINITO");
            } else {
                sb.append(dist[i]);
            }
            if (i < n - 1)
                sb.append("\n");
        }

        return new ResultadoAlgoritmo(true, sb.toString(), null, false);
    }

    /**
     * g) Executa o algoritmo de Kruskal para Árvore Geradora Mínima e marca as
     * arestas pertencentes.
     * 
     * @param grafo Grafo não-orientado carregado.
     * @return Peso total, lista de arestas e flag de destaque visual.
     */
    public ResultadoAlgoritmo executarKruskal(Grafo grafo) {
        if (grafo.isOrientado()) {
            return new ResultadoAlgoritmo(false,
                    "Algoritmo não aplicável: O algoritmo de Kruskal exige um grafo não-orientado.", null, false);
        }

        grafo.limparDestaquesArestas();
        int m = grafo.getNumArestas();
        int n = grafo.getNumVertices();

        Aresta[] ordenadas = new Aresta[m];
        for (int i = 0; i < m; i++) {
            ordenadas[i] = grafo.getArestas()[i];
        }

        // Ordena as arestas por peso em ordem não-decrescente
        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < m - 1 - i; j++) {
                if (ordenadas[j].getPeso() > ordenadas[j + 1].getPeso()) {
                    Aresta tmp = ordenadas[j];
                    ordenadas[j] = ordenadas[j + 1];
                    ordenadas[j + 1] = tmp;
                }
            }
        }

        UnionFind uf = new UnionFind(n);
        Aresta[] agm = new Aresta[n];
        int qtdAgm = 0;
        int pesoTotal = 0;

        for (int i = 0; i < m && qtdAgm < n - 1; i++) {
            Aresta a = ordenadas[i];
            if (uf.unir(a.getU(), a.getV())) {
                a.setDestacada(true);
                agm[qtdAgm++] = a;
                pesoTotal += a.getPeso();
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("peso total: ").append(pesoTotal).append("\n");
        sb.append("arestas: ");
        for (int i = 0; i < qtdAgm; i++) {
            sb.append("(").append(agm[i].getU()).append(",").append(agm[i].getV()).append(")");
            if (i < qtdAgm - 1)
                sb.append(" ");
        }

        return new ResultadoAlgoritmo(true, sb.toString(), null, true);
    }

    /**
     * Ordena de forma crescente um vetor de inteiros.
     */
    private void ordenarCrescente(int[] vetor, int tam) {
        for (int i = 0; i < tam - 1; i++) {
            for (int j = 0; j < tam - 1 - i; j++) {
                if (vetor[j] > vetor[j + 1]) {
                    int tmp = vetor[j];
                    vetor[j] = vetor[j + 1];
                    vetor[j + 1] = tmp;
                }
            }
        }
    }

    /**
     * Formata os vértices de um vetor separando-os por hífen (ex: 3-0-2-4-1-5-6).
     */
    private String formatarSequenciaHifen(int[] vetor, int tam) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tam; i++) {
            sb.append(vetor[i]);
            if (i < tam - 1)
                sb.append("-");
        }
        return sb.toString();
    }
}