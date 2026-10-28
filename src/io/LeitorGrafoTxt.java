package io;

import model.Grafo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe responsável pela leitura e interpretação (parsing) do arquivo texto de definição do grafo.
 */
public class LeitorGrafoTxt {

    /**
     * Lê o arquivo no formato especificado e instancia o Grafo correspondente.
     *
     * @param caminhoArquivo Caminho completo ou relativo do arquivo .txt.
     * @return Grafo populado com vértices e arestas.
     * @throws IOException Em caso de falhas de I/O ou erro sintático no cabeçalho.
     */
    public Grafo carregar(String caminhoArquivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            // Leitura e validação da 1ª linha (orientação)
            String linhaOrientado = lerProximaLinhaValida(br);
            if (linhaOrientado == null) {
                throw new IOException("O arquivo informado está vazio.");
            }
            boolean orientado = lerOrientacao(linhaOrientado);

            // Leitura e validação da 2ª linha (quantidade de vértices)
            String linhaVertices = lerProximaLinhaValida(br);
            if (linhaVertices == null) {
                throw new IOException("A 2ª linha com a definição de vértices (V=<n>) não foi encontrada.");
            }
            int numVertices = lerNumVertices(linhaVertices);

            Grafo grafo = new Grafo(numVertices, orientado);

            // Leitura e processamento das arestas
            String linhaAresta;
            int numLinha = 2;
            while ((linhaAresta = lerProximaLinhaValida(br)) != null) {
                numLinha++;
                processarLinhaAresta(linhaAresta, numLinha, numVertices, grafo);
            }

            return grafo;
        }
    }

    /**
     * Interpreta a declaração de orientação do grafo.
     */
    private boolean lerOrientacao(String linha) throws IOException {
        String limpo = linha.replaceAll("\\s+", "").toLowerCase();
        if (limpo.contains("orientado=sim") || limpo.contains("orientadosim")) {
            return true;
        }
        if (limpo.contains("orientado=nao") || limpo.contains("orientado=não") || limpo.contains("orientadonao")) {
            return false;
        }
        throw new IOException("Sintaxe inválida na 1ª linha. Esperado: 'orientado=sim' ou 'orientado=nao'.");
    }

    /**
     * Extrai a quantidade de vértices da linha no formato 'V=<n>'.
     */
    private int lerNumVertices(String linha) throws IOException {
        String limpo = linha.replaceAll("\\s+", "");
        int posIgual = limpo.indexOf('=');
        if (posIgual == -1) {
            throw new IOException("Sintaxe inválida na 2ª linha. Esperado: 'V=<n>'.");
        }
        try {
            return Integer.parseInt(limpo.substring(posIgual + 1));
        } catch (NumberFormatException e) {
            throw new IOException("Erro de conversão numérica ao ler número de vértices: " + limpo.substring(posIgual + 1), e);
        }
    }

    /**
     * Interpreta uma linha de aresta no formato '(u,v):peso' e a adiciona ao grafo se válida.
     */
    private void processarLinhaAresta(String linhaAresta, int numLinha, int numVertices, Grafo grafo) {
        String limpaAresta = linhaAresta.replaceAll("\\s+", "");
        int abrePar = limpaAresta.indexOf('(');
        int virgula = limpaAresta.indexOf(',');
        int fechaPar = limpaAresta.indexOf(')');
        int doisPontos = limpaAresta.indexOf(':');

        if (abrePar == -1 || virgula == -1 || fechaPar == -1 || doisPontos == -1) {
            System.out.println("⚠️ AVISO: Linha " + numLinha
                    + " ignorada por formato incorreto (deve ser (u,v):peso). Conteúdo lido: " + linhaAresta);
            return;
        }

        try {
            int u = Integer.parseInt(limpaAresta.substring(abrePar + 1, virgula));
            int v = Integer.parseInt(limpaAresta.substring(virgula + 1, fechaPar));
            int peso = Integer.parseInt(limpaAresta.substring(doisPontos + 1));

            if (u < 0 || u >= numVertices || v < 0 || v >= numVertices) {
                System.out.println("Ignorando aresta " + linhaAresta
                        + " pois referencia um nó fora do intervalo (0 a " + (numVertices - 1) + ").");
            } else {
                grafo.adicionarAresta(u, v, peso);
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro ao interpretar número na linha " + numLinha + ": " + linhaAresta);
        }
    }

    /**
     * Lê a próxima linha não vazia descartando espaços em branco nas extremidades.
     */
    private String lerProximaLinhaValida(BufferedReader br) throws IOException {
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
