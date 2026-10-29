package algorithms;

import model.Grafo;
import structures.NoAdjacencia;
import structures.PilhaInt;

/**
 * Algoritmo de Kosaraju para deteccao de Componentes Fortemente Conexas (CFC)
 * em grafos orientados.
 */
public class AlgoritmoKosaraju {

    private static final String[] PALETA_CORES_HEX = {
            "#FFB3BA", "#BAE1FF", "#BAFFC9", "#FFFFBA", "#FFDFBA",
            "#E0BBE4", "#957DAD", "#D291BC", "#FEC8D8", "#FFDFD3"
    };

    public AlgoritmoKosaraju() {
    }

    /**
     * Executa o algoritmo de Kosaraju identificando todas as componentes fortemente conexas do grafo.
     *
     * Pré-condição: grafo nao deve ser nulo e deve ser orientado.
     * Pós-condição: As componentes sao detectadas, ordenadas de forma decrescente pelo tamanho
     * (desempatando pelo menor indice de vertice inicial), mapeadas com cores para representacao visual
     * e formatadas no relatorio textual retornado.
     *
     * @param grafo Grafo orientado a ser particionado em componentes fortemente conexas.
     * @return ResultadoAlgoritmo contendo as componentes formatadas e o mapeamento de cores dos vertices.
     */
    public ResultadoAlgoritmo executar(Grafo grafo) {
        if (!grafo.isOrientado()) {
            return ResultadoAlgoritmo.erro("Algoritmo não aplicável: Componentes Fortemente Conexas exigem grafo orientado.");
        }

        int n = grafo.getNumVertices();
        boolean[] visitado = new boolean[n];
        PilhaInt pilha = new PilhaInt(n);

        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                dfsTermino(grafo, i, visitado, pilha);
            }
        }

        Grafo transposto = construirTransposto(grafo);
        for (int i = 0; i < n; i++) {
            visitado[i] = false;
        }

        int[][] componentes = new int[n][n];
        int[] tamanhos = new int[n];
        int qtdComp = 0;

        while (!pilha.estaVazia()) {
            int u = pilha.desempilhar();
            if (!visitado[u]) {
                int[] tam = { 0 };
                dfsColeta(transposto, u, visitado, componentes[qtdComp], tam);
                tamanhos[qtdComp] = tam[0];
                ordenarVetor(componentes[qtdComp], tam[0]);
                qtdComp++;
            }
        }

        for (int i = 0; i < qtdComp - 1; i++) {
            for (int j = 0; j < qtdComp - 1 - i; j++) {
                if (tamanhos[j] < tamanhos[j + 1] ||
                        (tamanhos[j] == tamanhos[j + 1] && componentes[j][0] > componentes[j + 1][0])) {
                    int tmpT = tamanhos[j];
                    tamanhos[j] = tamanhos[j + 1];
                    tamanhos[j + 1] = tmpT;
                    int[] tmpC = componentes[j];
                    componentes[j] = componentes[j + 1];
                    componentes[j + 1] = tmpC;
                }
            }
        }

        String[] coresVertices = new String[n];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < qtdComp; i++) {
            sb.append("Componente ").append(i + 1).append(":  ");
            String cor = PALETA_CORES_HEX[i % PALETA_CORES_HEX.length];
            for (int j = 0; j < tamanhos[i]; j++) {
                int v = componentes[i][j];
                coresVertices[v] = cor;
                sb.append(v).append(j < tamanhos[i] - 1 ? ", " : "");
            }
            if (i < qtdComp - 1) {
                sb.append("\n");
            }
        }

        return new ResultadoAlgoritmo(true, sb.toString(), coresVertices, false);
    }

    /**
     * Realiza a primeira etapa de busca em profundidade no grafo original, empilhando
     * os vertices na ordem inversa dos seus tempos de finalizacao.
     *
     * Pré-condição: g, visitado e pilha nao devem ser nulos; u deve ser um vertice ainda nao visitado.
     * Pós-condição: Todos os vertices acessiveis a partir de u sao visitados e u e empilhado apos a conclusao dos descendentes.
     *
     * @param g        Grafo original.
     * @param u        Vertice a ser explorado.
     * @param visitado Arranjo booleano para marcacao de vertices visitados.
     * @param pilha    Pilha na qual os vertices finalizados sao inseridos.
     */
    private void dfsTermino(Grafo g, int u, boolean[] visitado, PilhaInt pilha) {
        visitado[u] = true;
        for (NoAdjacencia at = g.getAdjacencia(u).getCabeca(); at != null; at = at.getProximo()) {
            if (!visitado[at.getDestino()]) {
                dfsTermino(g, at.getDestino(), visitado, pilha);
            }
        }
        pilha.empilhar(u);
    }

    /**
     * Constroi o grafo transposto invertendo o sentido de todas as arestas do grafo original.
     *
     * Pré-condição: g nao deve ser nulo e deve ser orientado.
     * Pós-condição: Retorna um novo grafo orientado com as mesmas vertices e todas as arestas invertidas.
     *
     * @param g Grafo direcionado original.
     * @return O grafo transposto correspondente.
     */
    private Grafo construirTransposto(Grafo g) {
        Grafo gT = new Grafo(g.getNumVertices(), true);
        for (int u = 0; u < g.getNumVertices(); u++) {
            for (NoAdjacencia at = g.getAdjacencia(u).getCabeca(); at != null; at = at.getProximo()) {
                gT.adicionarAresta(at.getDestino(), u, at.getPeso());
            }
        }
        return gT;
    }

    /**
     * Realiza a segunda etapa de busca em profundidade sobre o grafo transposto, coletando
     * os nos que integram a componente conexa atual.
     *
     * Pré-condição: gT, visitado, comp e tam nao devem ser nulos; u deve ser um vertice ainda nao visitado no transposto.
     * Pós-condição: Todos os vertices alcancaveis em gT a partir de u sao marcados e adicionados ao vetor comp.
     *
     * @param gT       Grafo transposto.
     * @param u        Vertice inicial da componente no grafo transposto.
     * @param visitado Arranjo de controle de visitacao.
     * @param comp     Vetor para insercao dos vertices da componente.
     * @param tam      Vetor unielementar acumulando o numero de vertices da componente.
     */
    private void dfsColeta(Grafo gT, int u, boolean[] visitado, int[] comp, int[] tam) {
        visitado[u] = true;
        comp[tam[0]++] = u;
        for (NoAdjacencia at = gT.getAdjacencia(u).getCabeca(); at != null; at = at.getProximo()) {
            if (!visitado[at.getDestino()]) {
                dfsColeta(gT, at.getDestino(), visitado, comp, tam);
            }
        }
    }

    /**
     * Ordena em ordem crescente os primeiros elementos de um arranjo de inteiros via algoritmo da bolha.
     *
     * Pré-condição: vet nao deve ser nulo e tam deve pertencer ao intervalo [0, vet.length].
     * Pós-condição: Os primeiros tam elementos de vet ficam ordenados de forma crescente in-place.
     *
     * @param vet Arranjo de inteiros a ser ordenado.
     * @param tam Quantidade de posicoes do vetor que serao ordenadas.
     */
    private void ordenarVetor(int[] vet, int tam) {
        for (int i = 0; i < tam - 1; i++) {
            for (int j = 0; j < tam - 1 - i; j++) {
                if (vet[j] > vet[j + 1]) {
                    int t = vet[j];
                    vet[j] = vet[j + 1];
                    vet[j + 1] = t;
                }
            }
        }
    }
}
