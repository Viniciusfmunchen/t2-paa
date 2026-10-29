package io;

import model.Grafo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Leitura e interpretação (parsing) do arquivo texto de definição do grafo.
 */
public class LeitorGrafoTxt {

    /**
     * Carrega e instancia o Grafo a partir do arquivo texto.
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
