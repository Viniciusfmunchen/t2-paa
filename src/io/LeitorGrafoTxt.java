package io;

import model.Grafo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Responsavel pelo carregamento e interpretacao sintatica
 * do arquivo de texto contendo a definicao de um grafo.
 */
public class LeitorGrafoTxt {

    public LeitorGrafoTxt() {
    }

    /**
     * Carrega e instancia o objeto Grafo a partir do arquivo de texto especificado.
     *
     * Pré-condição: caminhoArquivo deve apontar para um arquivo existente,
     * legivel e formatado segundo o padrao (linha 1: "orientado=sim/nao", linha 2: "V=n",
     * linhas seguintes: arestas no formato "(u,v):peso").
     * Pós-condição: Retorna o Grafo construido e populado com suas adjacencias e arestas.
     *
     * @param caminhoArquivo Caminho para o arquivo texto a ser processado.
     * @return O Grafo instanciado e populado com os dados do arquivo.
     * @throws IOException Se o arquivo estiver inacessivel, vazio ou apresentar formatacao incompativel.
     */
    public Grafo carregar(String caminhoArquivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String l1 = lerLinhaValida(br);
            if (l1 == null) {
                throw new IOException("O arquivo informado está vazio.");
            }
            boolean orientado = l1.replaceAll("\\s+", "").toLowerCase().contains("orientado=sim");

            String l2 = lerLinhaValida(br);
            if (l2 == null || !l2.contains("=")) {
                throw new IOException("A 2ª linha deve conter a definição de vértices no formato V=<n>.");
            }
            int numVertices = Integer.parseInt(l2.replaceAll("\\s+", "").split("=")[1]);

            Grafo grafo = new Grafo(numVertices, orientado);

            String linha;
            while ((linha = lerLinhaValida(br)) != null) {
                String[] partes = linha.replaceAll("[()\\s]", "").split("[:,]");
                if (partes.length == 3) {
                    try {
                        int u = Integer.parseInt(partes[0]);
                        int v = Integer.parseInt(partes[1]);
                        int peso = Integer.parseInt(partes[2]);
                        if (u >= 0 && u < numVertices && v >= 0 && v < numVertices) {
                            grafo.adicionarAresta(u, v, peso);
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            return grafo;
        }
    }

    /**
     * Le a proxima linha nao vazia do arquivo, descartando espacos em branco superfluos.
     *
     * Pré-condição: br deve ser um BufferedReader aberto e valido.
     * Pós-condição: Retorna a primeira linha util com conteudo, ou null se o fim do arquivo for atingido.
     *
     * @param br Leitor de fluxo de caracteres aberto.
     * @return A linha lida sem espacos perifericos, ou null se o final do arquivo for atingido.
     * @throws IOException Se ocorrer falha durante a leitura.
     */
    private String lerLinhaValida(BufferedReader br) throws IOException {
        String linha;
        while ((linha = br.readLine()) != null) {
            linha = linha.trim();
            if (!linha.isEmpty()) {
                return linha;
            }
        }
        return null;
    }
}
