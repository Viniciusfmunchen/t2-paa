package structures;

/**
 * Estrutura de dados para Conjuntos Disjuntos (Union-Find),
 * implementada com compressao de caminho e uniao por rank,
 * empregada no algoritmo de Kruskal.
 */
public class UnionFind {
    private final int[] pai;
    private final int[] rank;

    /**
     * Construtor da estrutura Union-Find.
     *
     * Pré-condição: n deve ser um inteiro maior que zero correspondente ao total de elementos.
     * Pós-condição: Sao inicializados n conjuntos disjuntos unitarios com rank 0 onde cada elemento e seu proprio pai.
     *
     * @param n Quantidade total de elementos ou vertices.
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
     * Localiza o representante canonico do conjunto contendo o elemento especificado,
     * aplicando compressao de caminho.
     *
     * Pré-condição: i deve ser um indice valido no intervalo [0, n - 1].
     * Pós-condição: Retorna a raiz do conjunto e atualiza os ponteiros do caminho diretamente para a raiz.
     *
     * @param i Identificador do elemento a ser consultado.
     * @return O identificador da raiz representativa do conjunto.
     */
    public int encontrar(int i) {
        if (pai[i] != i) {
            pai[i] = encontrar(pai[i]);
        }
        return pai[i];
    }

    /**
     * Realiza a uniao entre os conjuntos disjuntos contendo os elementos informados,
     * baseando-se no rank das raizes.
     *
     * Pré-condição: x e y devem ser indices validos no intervalo [0, n - 1].
     * Pós-condição: Se pertencerem a conjuntos distintos, as raizes sao unificadas pelo rank e retorna true;
     * caso ja pertencam ao mesmo conjunto, nada e alterado e retorna false.
     *
     * @param x Primeiro elemento.
     * @param y Segundo elemento.
     * @return true se dois conjuntos distintos foram unidos, false se ja pertenciam ao mesmo conjunto.
     */
    public boolean unir(int x, int y) {
        int raizX = encontrar(x);
        int raizY = encontrar(y);
        if (raizX == raizY) {
            return false;
        }

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
