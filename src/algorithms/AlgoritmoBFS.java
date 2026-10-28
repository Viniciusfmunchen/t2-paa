package algorithms;

import model.Grafo;
import structures.FilaInt;
import structures.NoAdjacencia;

/**
 * Implementação da Busca em Largura (BFS).
 * Garante a exploração em camadas e a cobertura de todas as componentes conexas.
 */
public class AlgoritmoBFS {

    /**
     * Executa a Busca em Largura a partir do vértice de origem informado.
     *
     * @param grafo  Grafo a ser percorrido.
     * @param origem Vértice inicial.
     * @return Resultado contendo a sequência de visitação formatada com hifens.
     */
    public ResultadoAlgoritmo executar(Grafo grafo, int origem) {
        int n = grafo.getNumVertices();
        if (origem < 0 || origem >= n) {
            return new ResultadoAlgoritmo(false, "Erro: O vértice de origem deve estar entre 0 e " + (n - 1) + ".",
                    null, false);
        }

        boolean[] visitado = new boolean[n];
        int[] ordem = new int[n];
        int contador = 0;
        FilaInt fila = new FilaInt(n);

        // Explora a componente conectada a partir da origem informada
        contador = explorarComponenteBFS(grafo, origem, visitado, ordem, contador, fila);

        // Garante a exploração de eventuais componentes desconexas
        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                contador = explorarComponenteBFS(grafo, i, visitado, ordem, contador, fila);
            }
        }

        String saida = formatarSequenciaHifen(ordem, contador);
        return new ResultadoAlgoritmo(true, saida, null, false);
    }

    /**
     * Explora em largura uma única componente conexa a partir de um vértice inicial.
     */
    private int explorarComponenteBFS(Grafo grafo, int inicio, boolean[] visitado, int[] ordem, int contador, FilaInt fila) {
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

    private String formatarSequenciaHifen(int[] vetor, int tam) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tam; i++) {
            sb.append(vetor[i]);
            if (i < tam - 1) {
                sb.append("-");
            }
        }
        return sb.toString();
    }
}

