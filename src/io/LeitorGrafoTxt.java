package io;

import model.Grafo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe responsável exclusivamente pela leitura e interpretação (parsing) do
 * arquivo texto do grafo.
 */
public class LeitorGrafoTxt {

    public Grafo carregar(String caminhoArquivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linhaOrientado = lerProximaLinhaValida(br);
            if (linhaOrientado == null) {
                throw new IOException("O arquivo informado está vazio.");
            }

            String limpoOrientado = linhaOrientado.replaceAll("\\s+", "").toLowerCase();
            boolean orientado;
            if (limpoOrientado.contains("orientado=sim") || limpoOrientado.contains("orientadosim")) {
                orientado = true;
            } else if (limpoOrientado.contains("orientado=nao") || limpoOrientado.contains("orientado=não")
                    || limpoOrientado.contains("orientadonao")) {
                orientado = false;
            } else {
                throw new IOException("Sintaxe inválida na 1ª linha. Esperado: 'orientado=sim' ou 'orientado=nao'.");
            }

            String linhaVertices = lerProximaLinhaValida(br);
            if (linhaVertices == null) {
                throw new IOException("A 2ª linha com a definição de vértices (V=<n>) não foi encontrada.");
            }

            String limpoVertices = linhaVertices.replaceAll("\\s+", "");
            int posIgual = limpoVertices.indexOf('=');
            if (posIgual == -1) {
                throw new IOException("Sintaxe inválida na 2ª linha. Esperado: 'V=<n>'.");
            }
            int numVertices = Integer.parseInt(limpoVertices.substring(posIgual + 1));

            Grafo grafo = new Grafo(numVertices, orientado);

            String linhaAresta;
            int numLinha = 2; // Inicia o tracking visual das linhas após V=<n>
            while ((linhaAresta = lerProximaLinhaValida(br)) != null) {
                numLinha++;
                String limpaAresta = linhaAresta.replaceAll("\\s+", "");
                int abrePar = limpaAresta.indexOf('(');
                int virgula = limpaAresta.indexOf(',');
                int fechaPar = limpaAresta.indexOf(')');
                int doisPontos = limpaAresta.indexOf(':');

                if (abrePar != -1 && virgula != -1 && fechaPar != -1 && doisPontos != -1) {
                    try {
                        int u = Integer.parseInt(limpaAresta.substring(abrePar + 1, virgula));
                        int v = Integer.parseInt(limpaAresta.substring(virgula + 1, fechaPar));
                        int peso = Integer.parseInt(limpaAresta.substring(doisPontos + 1));

                        // Validação caso o TXT exceda os limites do número de nós especificado
                        if (u >= numVertices || v >= numVertices || u < 0 || v < 0) {
                            System.out.println("⚠️ AVISO: Ignorando aresta " + linhaAresta
                                    + " pois referencia um nó fora do intervalo (0 a " + (numVertices - 1) + ").");
                        } else {
                            grafo.adicionarAresta(u, v, peso);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(
                                "⚠️ AVISO: Erro ao interpretar número na linha " + numLinha + ": " + linhaAresta);
                    }
                } else {
                    System.out.println("⚠️ AVISO: Linha " + numLinha
                            + " ignorada por formato incorreto (deve ser (u,v):peso). Conteúdo lido: " + linhaAresta);
                }
            }
            return grafo;
        } catch (NumberFormatException e) {
            throw new IOException("Erro de conversão numérica ao ler vértices ou pesos no arquivo.", e);
        }
    }

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