package structures;

/**
 * Estrutura Union-Find (Conjuntos Disjuntos) com compressão de caminho e união
 * por rank.
 */
public class UnionFind {
    private final int[] pai;
    private final int[] rank;

    /**
     * Inicializa N conjuntos disjuntos unitários.
     * 
     * @param n Quantidade de vértices do grafo.
     */
    public UnionFind(int n) {
        this.pai = new int[n];
        this.rank = new int[n];
        for (int i = 0; i < n; i++) {
            pai[i] = i;
            rank[i] = 0;
        }
    }

    /**
     * Localiza o representante canônico do conjunto que contém o elemento
     * informado.
     * 
     * @param i Vértice consultado.
     * @return Representante do conjunto.
     */
    public int encontrar(int i) {
        if (pai[i] != i) {
            pai[i] = encontrar(pai[i]);
        }
        return pai[i];
    }

    /**
     * Une os conjuntos de dois vértices caso pertençam a componentes distintas.
     * 
     * @param x Primeiro vértice.
     * @param y Segundo vértice.
     * @return true se a união ocorreu, false se já pertenciam ao mesmo conjunto.
     */
    public boolean unir(int x, int y) {
        int raizX = encontrar(x);
        int raizY = encontrar(y);
        if (raizX == raizY)
            return false;

        if (rank[raizX] < rank[raizY]) {
            pai[raizX] = raizY;
        } else if (rank[raizX] > rank[raizY]) {
            pai[raizY] = raizX;
        } else {
            pai[raizY] = raizX;
            rank[raizX]++;
        }
        return true;
    }
}