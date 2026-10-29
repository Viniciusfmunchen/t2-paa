package algorithms;

/**
 * Encapsula o resultado da execução de um algoritmo de grafo.
 */
public class ResultadoAlgoritmo {
    private final boolean sucesso;
    private final String textoSaida;
    private final String[] coresVertices;
    private final boolean destacarArestasAgm;

    public ResultadoAlgoritmo(boolean sucesso, String textoSaida, String[] coresVertices, boolean destacarArestasAgm) {
        this.sucesso = sucesso;
        this.textoSaida = textoSaida;
        this.coresVertices = coresVertices;
        this.destacarArestasAgm = destacarArestasAgm;
    }

    public static ResultadoAlgoritmo sucesso(String textoSaida) {
        return new ResultadoAlgoritmo(true, textoSaida, null, false);
    }

    public static ResultadoAlgoritmo erro(String mensagem) {
        return new ResultadoAlgoritmo(false, mensagem, null, false);
    }

    /**
     * Formata uma sequência de vértices no padrão exigido: "u - v - w".
     */
    public static String formatarSequencia(int[] vetor, int tam) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tam; i++) {
            sb.append(vetor[i]);
            if (i < tam - 1) {
                sb.append(" - ");
            }
        }
        return sb.toString();
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
