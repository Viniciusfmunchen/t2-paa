package algorithms;

import model.Grafo;
import structures.NoAdjacencia;
import structures.PilhaInt;

/**
 * Implementação da Ordenação Topológica para grafos orientados acíclicos (DAG).
 * Valida a orientação e detecta ciclos no grafo antes de produzir a ordenação.
 */
public class AlgoritmoOrdenacaoTopologica {

    /**
     * Executa a Ordenação Topológica no grafo informado.
     *
     * @param grafo Grafo direcionado.
     * @return Resultado contendo a sequência topológica ou mensagem de impedimento.
     */
    public ResultadoAlgoritmo executar(Grafo grafo) {
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
     * Auxiliar recursivo para detecção de ciclo e preenchimento da pilha topológica.
     */
    private boolean possuiCicloTopologico(Grafo grafo, int u, int[] estado, PilhaInt pilha) {
        estado[u] = 1;
        NoAdjacencia atual = grafo.getAdjacencia(u).getCabeca();
        while (atual != null) {
            int v = atual.getDestino();
            if (estado[v] == 1) {
                return true;
            }
            if (estado[v] == 0 && possuiCicloTopologico(grafo, v, estado, pilha)) {
                return true;
            }
            atual = atual.getProximo();
        }
        estado[u] = 2;
        pilha.empilhar(u);
        return false;
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

