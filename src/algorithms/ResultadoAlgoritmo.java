package algorithms;

/**
 * Encapsula o resultado da execucao de um algoritmo sobre grafos, contendo
 * o status de sucesso, o texto de resposta para exibicao e configuracoes
 * visuais (como paleta de cores para vertices e destaque de arestas da AGM).
 */
public class ResultadoAlgoritmo {
    private final boolean sucesso;
    private final String textoSaida;
    private final String[] coresVertices;
    private final boolean destacarArestasAgm;

    /**
     * Construtor completo do resultado do algoritmo.
     *
     * Pré-condição: textoSaida nao deve ser nulo.
     * Pós-condição: Uma nova instancia de ResultadoAlgoritmo e criada com os atributos fornecidos.
     *
     * @param sucesso            Indica se a execucao do algoritmo ocorreu com exito.
     * @param textoSaida         Texto formatado contendo o resultado textual ou mensagem explicativa.
     * @param coresVertices      Vetor com strings de cores hexadecimais para coloracao dos vertices, ou null.
     * @param destacarArestasAgm Flag que determina se as arestas selecionadas devem receber destaque visual.
     */
    public ResultadoAlgoritmo(boolean sucesso, String textoSaida, String[] coresVertices, boolean destacarArestasAgm) {
        this.sucesso = sucesso;
        this.textoSaida = textoSaida;
        this.coresVertices = coresVertices;
        this.destacarArestasAgm = destacarArestasAgm;
    }

    /**
     * Cria uma instancia de resultado representando uma operacao finalizada com sucesso.
     *
     * Pré-condição: textoSaida nao deve ser nulo.
     * Pós-condição: Retorna um objeto ResultadoAlgoritmo com status positivo e texto de saida atribuido.
     *
     * @param textoSaida Texto do resultado a ser exibido.
     * @return Instancia configurada com sucesso.
     */
    public static ResultadoAlgoritmo sucesso(String textoSaida) {
        return new ResultadoAlgoritmo(true, textoSaida, null, false);
    }

    /**
     * Cria uma instancia de resultado representando uma falha ou restricao de aplicabilidade de um algoritmo.
     *
     * Pré-condição: mensagem nao deve ser nula.
     * Pós-condição: Retorna um objeto ResultadoAlgoritmo com status de insucesso e a mensagem de erro.
     *
     * @param mensagem Mensagem com a causa do impedimento da execucao.
     * @return Instancia configurada com falha.
     */
    public static ResultadoAlgoritmo erro(String mensagem) {
        return new ResultadoAlgoritmo(false, mensagem, null, false);
    }

    /**
     * Formata uma sequencia de vertices no padrao exigido pelo trabalho (exemplo: "u - v - w").
     *
     * Pré-condição: vetor nao deve ser nulo e tam deve estar no intervalo [0, vetor.length].
     * Pós-condição: Retorna os primeiros tam elementos do arranjo concatenados e separados por " - ".
     *
     * @param vetor Arranjo contendo a ordem de vertices.
     * @param tam   Quantidade de elementos validos no vetor a serem formatados.
     * @return String formatada contendo a sequencia de visitacao.
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

    /**
     * Indica se a operacao foi executada com exito.
     *
     * Pré-condição: A instancia deve estar criada.
     * Pós-condição: Retorna o estado booleano de sucesso.
     *
     * @return true se o algoritmo obteve sucesso, false caso contrario.
     */
    public boolean isSucesso() {
        return sucesso;
    }

    /**
     * Retorna o texto formatado produzido pelo algoritmo ou a mensagem explicativa.
     *
     * Pré-condição: A instancia deve estar criada.
     * Pós-condição: Retorna o texto de saida sem modificar o objeto.
     *
     * @return O texto de saida.
     */
    public String getTextoSaida() {
        return textoSaida;
    }

    /**
     * Retorna o vetor contendo os codigos hexadecimais de cores atribuidas aos vertices.
     *
     * Pré-condição: A instancia deve estar criada.
     * Pós-condição: Retorna o arranjo de cores por vertice ou null caso nao haja coloracao.
     *
     * @return Arranjo de cores em hexadecimal ou null.
     */
    public String[] getCoresVertices() {
        return coresVertices;
    }

    /**
     * Informa se as arestas da Arvore Geradora Minima (AGM) devem ser destacadas visualmente.
     *
     * Pré-condição: A instancia deve estar criada.
     * Pós-condição: Retorna a flag de destaque de arestas da AGM.
     *
     * @return true se as arestas da AGM devem ser destacadas, false caso contrario.
     */
    public boolean isDestacarArestasAgm() {
        return destacarArestasAgm;
    }
}
