package algorithms;

import model.Aresta;
import model.Grafo;
import structures.UnionFind;

/**
 * Implementação do Algoritmo de Kruskal para Árvore Geradora Mínima (AGM)
 * em grafos não-orientados, utilizando Union-Find com compressão de caminho e rank.
 */
public class AlgoritmoKruskal {

    /**
     * Executa o algoritmo de Kruskal e identifica as arestas que compõem a AGM.
     *
     * @param grafo Grafo não-orientado.
     * @return Resultado contendo o peso total, a lista de arestas e flag para destaque visual.
     */
    public ResultadoAlgoritmo executar(Grafo grafo) {
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
        ordenarArestasPorPeso(ordenadas, m);

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

        String saida = formatarSaidaKruskal(pesoTotal, agm, qtdAgm);
        return new ResultadoAlgoritmo(true, saida, null, true);
    }

    /**
     * Ordena as arestas por peso em ordem não-decrescente.
     */
    private void ordenarArestasPorPeso(Aresta[] arestas, int total) {
        for (int i = 0; i < total - 1; i++) {
            for (int j = 0; j < total - 1 - i; j++) {
                if (arestas[j].getPeso() > arestas[j + 1].getPeso()) {
                    Aresta tmp = arestas[j];
                    arestas[j] = arestas[j + 1];
                    arestas[j + 1] = tmp;
                }
            }
        }
    }

    private String formatarSaidaKruskal(int pesoTotal, Aresta[] agm, int qtdAgm) {
        StringBuilder sb = new StringBuilder();
        sb.append("peso total: ").append(pesoTotal).append("\n");
        sb.append("arestas: ");
        for (int i = 0; i < qtdAgm; i++) {
            sb.append("(").append(agm[i].getU()).append(",").append(agm[i].getV()).append(")");
            if (i < qtdAgm - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}

