package algorithms;

/**
 * Objeto de transferência de dados (DTO) que encapsula o resultado da execução
 * de um algoritmo.
 */
public class ResultadoAlgoritmo {
    private final boolean sucesso;
    private final String textoSaida;
    private final String[] coresVertices;
    private final boolean destacarArestasAgm;

    /**
     * Construtor do resultado do algoritmo.
     * 
     * @param sucesso            Indica se o algoritmo pôde ser aplicado ao grafo.
     * @param textoSaida         Texto formatado segundo o padrão exigido pelo
     *                           trabalho.
     * @param coresVertices      Cores atribuídas a cada vértice (usado em
     *                           Componentes Fortemente Conexas).
     * @param destacarArestasAgm Indica se as arestas marcadas devem ser destacadas
     *                           (usado em Kruskal).
     */
    public ResultadoAlgoritmo(boolean sucesso, String textoSaida, String[] coresVertices, boolean destacarArestasAgm) {
        this.sucesso = sucesso;
        this.textoSaida = textoSaida;
        this.coresVertices = coresVertices;
        this.destacarArestasAgm = destacarArestasAgm;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public String getTextoSaida() {
        return textoSaida;
    }

    public String[] getCoresVertices() {
        return coresVertices;
    }

    public boolean isDestacarArestasAgm() {
        return destacarArestasAgm;
    }
}