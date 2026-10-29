package algorithms;

import model.Aresta;
import model.Grafo;
import structures.UnionFind;

/**
 * Algoritmo de Kruskal para determinacao da Arvore Geradora Minima (AGM)
 * em grafos conexos nao-orientados ponderados.
 */
public class AlgoritmoKruskal {

    public AlgoritmoKruskal() {
    }

    /**
     * Executa o algoritmo de Kruskal determinando as arestas que compoem a Arvore Geradora Minima.
     *
     * Pré-condição: grafo nao deve ser nulo e deve ser nao-orientado.
     * Pós-condição: Retorna o resultado com o peso total acumulado da AGM, a listagem das arestas
     * selecionadas e sinaliza no objeto a ativacao do destaque grafico das arestas.
     *
     * @param grafo Grafo nao-orientado ponderado a ser processado.
     * @return ResultadoAlgoritmo contendo o peso total, a relacao de arestas da AGM e a diretiva de destaque visual.
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
