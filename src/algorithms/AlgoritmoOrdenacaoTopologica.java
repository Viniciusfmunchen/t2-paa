package algorithms;

import model.Grafo;
import structures.NoAdjacencia;
import structures.PilhaInt;

/**
 * Implementação da Ordenação Topológica para grafos orientados acíclicos (DAG).
 */
public class AlgoritmoOrdenacaoTopologica {
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
