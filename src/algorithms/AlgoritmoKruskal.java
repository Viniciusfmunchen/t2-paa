package algorithms;

import model.Aresta;
import model.Grafo;
import structures.UnionFind;

/**
 * Algoritmo de Kruskal para Árvore Geradora Mínima (AGM) em grafos não-orientados.
 */
public class AlgoritmoKruskal {

    /**
     * Executa o algoritmo de Kruskal e identifica as arestas que compõem a AGM.
     */
    public ResultadoAlgoritmo executar(Grafo grafo) {
        if (grafo.isOrientado()) {
            return ResultadoAlgoritmo.erro("Algoritmo não aplicável: O algoritmo de Kruskal exige grafo não-orientado.");
        }

        grafo.limparDestaquesArestas();
        int m = grafo.getNumArestas();
        int n = grafo.getNumVertices();

        Aresta[] ordenadas = new Aresta[m];
        for (int i = 0; i < m; i++) {
            ordenadas[i] = grafo.getArestas()[i];
        }

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
        sb.append("peso total:  ").append(pesoTotal).append("\narestas:  ");
        for (int i = 0; i < qtdAgm; i++) {
            sb.append("(").append(agm[i].getU()).append(",").append(agm[i].getV()).append(")");
            if (i < qtdAgm - 1) {
                sb.append(" ");
            }
        }

        return new ResultadoAlgoritmo(true, sb.toString(), null, true);
    }
}
