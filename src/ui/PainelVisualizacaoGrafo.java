package ui;

import model.Aresta;
import model.Grafo;
import structures.UnionFind;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Painel Swing responsável pela renderização e visualização gráfica interativa
 * do grafo utilizando a biblioteca JGraphX.
 * 
 * Implementa minimização de cruzamento de arestas através de layout orgânico
 * avançado com recozimento simulado, expansão de arestas para espaçamento legível
 * e empacotamento harmônico de componentes conexas.
 */
public class PainelVisualizacaoGrafo extends JPanel {

    // --- Constantes Visuais e de Layout ---
    private static final String COR_VERTICE_PADRAO = "#E8EEF5";
    private static final String COR_VERTICE_BORDA = "#2B4C7E";
    private static final String COR_ARESTA_PADRAO = "#555555";
    private static final String COR_ARESTA_DESTAQUE = "#D92B2B";
    private static final int TAMANHO_VERTICE = 42;

    // Configurações de otimização de arestas
    private static final double FATOR_CUSTO_CRUZAMENTO_ARESTAS = 1_000_000.0;
    private static final int MAX_ITERACOES_ORGANICO = 2000;

    // Distância confortável entre centros de nós conectados (deixa ~83px de linha visível além dos nós)
    private static final double DISTANCIA_ALVO_ARESTA = 125.0;

    // Dimensões da área útil de exibição na janela padrão sem necessidade de scroll
    private static final double LIMITE_ESPACO_LARGURA = 700.0;
    private static final double LIMITE_ESPACO_ALTURA = 420.0;
    private static final double MARGEM_VISUAL = 35.0;
    private static final double ESPACAMENTO_COMPONENTES = 60.0;

    private mxGraphComponent graphComponent;

    public PainelVisualizacaoGrafo() {
        setLayout(new BorderLayout());
    }

    /**
     * Renderiza o grafo no painel, aplicando cores aos nós, destaques nas arestas,
     * minimização de cruzamentos e espaçamento confortável entre os vértices.
     *
     * @param grafo           Instância do grafo a ser exibido.
     * @param coresVertices   Cores hexadecimais para cada vértice (opcional).
     * @param destacarArestas Se verdadeiro, destaca visualmente as arestas marcadas.
     */
    public void renderizarGrafo(Grafo grafo, String[] coresVertices, boolean destacarArestas) {
        removeAll();

        mxGraph mxGrafo = criarGrafoNaoEditavel();
        Object parent = mxGrafo.getDefaultParent();

        mxGrafo.getModel().beginUpdate();
        try {
            Object[] verticesVisuais = inserirVerticesVisuais(mxGrafo, parent, grafo, coresVertices);
            inserirArestasVisuais(mxGrafo, parent, grafo, verticesVisuais, destacarArestas);

            // 1. Distribuição inicial circular: desembaraça nós coincidentes na origem (0,0)
            mxCircleLayout circleLayout = new mxCircleLayout(mxGrafo);
            circleLayout.execute(parent);

            // 2. Layout Orgânico com recozimento simulado focado em eliminar cruzamento de arestas
            mxOrganicLayout layoutOrganico = new mxOrganicLayout(mxGrafo);
            layoutOrganico.setOptimizeEdgeCrossing(true);
            layoutOrganico.setEdgeCrossingCostFactor(FATOR_CUSTO_CRUZAMENTO_ARESTAS);
            layoutOrganico.setOptimizeEdgeDistance(true);
            layoutOrganico.setOptimizeNodeDistribution(true);
            layoutOrganico.setOptimizeEdgeLength(true);
            layoutOrganico.setOptimizeBorderLine(true);
            layoutOrganico.setFineTuning(true);
            layoutOrganico.setMaxIterations(MAX_ITERACOES_ORGANICO);
            layoutOrganico.execute(parent);

            // 3. Expande as arestas para distância legível e empacota componentes conexas lado a lado
            organizarComponentesConexas(mxGrafo, parent, grafo, verticesVisuais);

            // 4. Curva arestas paralelas bidirecionais com espaçamento visível
            mxParallelEdgeLayout layoutParalelas = new mxParallelEdgeLayout(mxGrafo, 30);
            layoutParalelas.execute(parent);

            // 5. Trava nós, arestas e rótulos contra movimentação após o término dos layouts
            travarElementosGrafo(mxGrafo);
        } finally {
            mxGrafo.getModel().endUpdate();
        }

        graphComponent = criarComponenteGrafico(mxGrafo);
        add(graphComponent, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
     * Cria e retorna um mxGraph configurado para não permitir edição textual direta nos nós.
     */
    private mxGraph criarGrafoNaoEditavel() {
        return new mxGraph() {
            @Override
            public boolean isCellEditable(Object cell) {
                return false;
            }
        };
    }

    /**
     * Trava completamente todos os elementos do grafo (vértices, arestas e rótulos)
     * após a conclusão do cálculo dos algoritmos de layout.
     */
    private void travarElementosGrafo(mxGraph graph) {
        graph.setCellsMovable(false);
        graph.setCellsResizable(false);
        graph.setCellsEditable(false);
        graph.setCellsDeletable(false);
        graph.setCellsBendable(false);
        graph.setCellsDisconnectable(false);
        graph.setCellsCloneable(false);
        graph.setDropEnabled(false);
        graph.setEdgeLabelsMovable(false);
        graph.setVertexLabelsMovable(false);
        graph.setCellsLocked(true);
    }

    /**
     * Insere os nós do grafo no componente gráfico com os estilos e cores definidos.
     */
    private Object[] inserirVerticesVisuais(mxGraph mxGrafo, Object parent, Grafo grafo, String[] coresVertices) {
        int n = grafo.getNumVertices();
        Object[] verticesVisuais = new Object[n];

        for (int i = 0; i < n; i++) {
            String corPreenchimento = (coresVertices != null && coresVertices[i] != null)
                    ? coresVertices[i]
                    : COR_VERTICE_PADRAO;

            String estiloVertice = mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_ELLIPSE + ";"
                    + mxConstants.STYLE_FILLCOLOR + "=" + corPreenchimento + ";"
                    + mxConstants.STYLE_STROKECOLOR + "=" + COR_VERTICE_BORDA + ";"
                    + mxConstants.STYLE_STROKEWIDTH + "=2;"
                    + mxConstants.STYLE_FONTCOLOR + "=#000000;"
                    + mxConstants.STYLE_FONTSIZE + "=14;"
                    + mxConstants.STYLE_FONTSTYLE + "=" + mxConstants.FONT_BOLD;

            verticesVisuais[i] = mxGrafo.insertVertex(parent, null, String.valueOf(i),
                    0, 0, TAMANHO_VERTICE, TAMANHO_VERTICE, estiloVertice);
        }
        return verticesVisuais;
    }

    /**
     * Insere as arestas com formatação de setas, espessuras e cores de destaque.
     */
    private void inserirArestasVisuais(mxGraph mxGrafo, Object parent, Grafo grafo,
                                       Object[] verticesVisuais, boolean destacarArestas) {
        String setaFinal = grafo.isOrientado() ? mxConstants.ARROW_CLASSIC : mxConstants.NONE;

        for (int i = 0; i < grafo.getNumArestas(); i++) {
            Aresta a = grafo.getAresta(i);
            boolean destacada = destacarArestas && a.isDestacada();

            String corAresta = destacada ? COR_ARESTA_DESTAQUE : COR_ARESTA_PADRAO;
            String espessura = destacada ? "3.5" : "1.5";

            String estiloAresta = mxConstants.STYLE_ENDARROW + "=" + setaFinal + ";"
                    + mxConstants.STYLE_STROKECOLOR + "=" + corAresta + ";"
                    + mxConstants.STYLE_STROKEWIDTH + "=" + espessura + ";"
                    + mxConstants.STYLE_FONTCOLOR + "=" + (destacada ? COR_ARESTA_DESTAQUE : "#111111") + ";"
                    + mxConstants.STYLE_FONTSIZE + "=13;"
                    + mxConstants.STYLE_FONTSTYLE + "=" + mxConstants.FONT_BOLD + ";"
                    + mxConstants.STYLE_LABEL_BACKGROUNDCOLOR + "=#FFFFFF";

            mxGrafo.insertEdge(parent, null, String.valueOf(a.getPeso()),
                    verticesVisuais[a.getU()], verticesVisuais[a.getV()], estiloAresta);
        }
    }

    /**
     * Estrutura interna para encapsular a caixa delimitadora (bounding box) de cada componente.
     */
    private static class InfoComponente {
        final List<Integer> vertices;
        double minX, minY, w, h;

        InfoComponente(List<Integer> vertices) {
            this.vertices = vertices;
        }
    }

    /**
     * Identifica as componentes conexas do grafo, expande as arestas para um comprimento
     * confortável e as organiza lado a lado de forma centralizada e sem scroll.
     */
    private void organizarComponentesConexas(mxGraph mxGrafo, Object parent, Grafo grafo, Object[] verticesVisuais) {
        int n = grafo.getNumVertices();
        if (n == 0) {
            return;
        }

        // 1. Calcula a menor distância entre vértices conectados pelo layout inicial
        double menorDistAresta = Double.MAX_VALUE;
        for (int i = 0; i < grafo.getNumArestas(); i++) {
            Aresta a = grafo.getAresta(i);
            com.mxgraph.model.mxGeometry gU = mxGrafo.getModel().getGeometry(verticesVisuais[a.getU()]);
            com.mxgraph.model.mxGeometry gV = mxGrafo.getModel().getGeometry(verticesVisuais[a.getV()]);
            double dx = gU.getX() - gV.getX();
            double dy = gU.getY() - gV.getY();
            double d = Math.sqrt(dx * dx + dy * dy);
            menorDistAresta = Math.min(menorDistAresta, d);
        }

        // Fator de escala desejado para afastar os vértices e dar visibilidade às arestas
        double fatorDesejado = 1.0;
        if (menorDistAresta < DISTANCIA_ALVO_ARESTA && menorDistAresta > 0) {
            fatorDesejado = DISTANCIA_ALVO_ARESTA / menorDistAresta;
        }

        // 2. Agrupa os nós por componente conexa (considerando conexões visuais)
        UnionFind uf = new UnionFind(n);
        for (int i = 0; i < grafo.getNumArestas(); i++) {
            Aresta a = grafo.getAresta(i);
            uf.unir(a.getU(), a.getV());
        }

        Map<Integer, List<Integer>> mapaComponentes = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            int raiz = uf.encontrar(i);
            mapaComponentes.computeIfAbsent(raiz, k -> new ArrayList<>()).add(i);
        }

        // 3. Mede a bounding box individual de cada componente já considerando a expansão desejada
        List<InfoComponente> componentes = new ArrayList<>();
        for (List<Integer> listaVertices : mapaComponentes.values()) {
            InfoComponente info = new InfoComponente(listaVertices);
            double cMinX = Double.MAX_VALUE;
            double cMinY = Double.MAX_VALUE;
            double cMaxX = -Double.MAX_VALUE;
            double cMaxY = -Double.MAX_VALUE;

            for (int v : listaVertices) {
                com.mxgraph.model.mxGeometry geo = mxGrafo.getModel().getGeometry(verticesVisuais[v]);
                cMinX = Math.min(cMinX, geo.getX());
                cMinY = Math.min(cMinY, geo.getY());
                cMaxX = Math.max(cMaxX, geo.getX() + geo.getWidth());
                cMaxY = Math.max(cMaxY, geo.getY() + geo.getHeight());
            }

            info.minX = cMinX;
            info.minY = cMinY;
            info.w = Math.max(TAMANHO_VERTICE, (cMaxX - cMinX) * fatorDesejado);
            info.h = Math.max(TAMANHO_VERTICE, (cMaxY - cMinY) * fatorDesejado);
            componentes.add(info);
        }

        // Ordena para que componentes maiores fiquem dispostas à esquerda
        componentes.sort((c1, c2) -> Integer.compare(c2.vertices.size(), c1.vertices.size()));

        // 4. 1ª passada: simula a disposição em linha / quebra para medir as dimensões necessárias
        double cursorX = 0;
        double cursorY = 0;
        double linhaMaxH = 0;
        double totalW = 0;

        for (InfoComponente comp : componentes) {
            if (cursorX > 0 && cursorX + comp.w > LIMITE_ESPACO_LARGURA) {
                cursorX = 0;
                cursorY += linhaMaxH + ESPACAMENTO_COMPONENTES;
                linhaMaxH = 0;
            }
            cursorX += comp.w + ESPACAMENTO_COMPONENTES;
            linhaMaxH = Math.max(linhaMaxH, comp.h);
            totalW = Math.max(totalW, cursorX - ESPACAMENTO_COMPONENTES);
        }
        double totalH = cursorY + linhaMaxH;

        // Fator de escala final: garante que a expansão não ultrapasse a área útil da janela
        double escalaFinal = fatorDesejado;
        if (totalW > LIMITE_ESPACO_LARGURA || totalH > LIMITE_ESPACO_ALTURA) {
            double redutor = Math.min(LIMITE_ESPACO_LARGURA / totalW, LIMITE_ESPACO_ALTURA / totalH);
            escalaFinal = fatorDesejado * redutor;
            totalW *= redutor;
            totalH *= redutor;
        }

        // Centraliza o bloco do grafo no espaço útil
        double espacoRestanteX = Math.max(0, LIMITE_ESPACO_LARGURA - totalW);
        double espacoRestanteY = Math.max(0, LIMITE_ESPACO_ALTURA - totalH);
        double offsetX = MARGEM_VISUAL + (espacoRestanteX / 2.0);
        double offsetY = MARGEM_VISUAL + (espacoRestanteY / 2.0);

        // 5. 2ª passada: posiciona os nós com suas coordenadas expandidas e ajustadas
        cursorX = 0;
        cursorY = 0;
        linhaMaxH = 0;

        for (InfoComponente comp : componentes) {
            double compW_real = comp.w * (escalaFinal / fatorDesejado);
            double compH_real = comp.h * (escalaFinal / fatorDesejado);

            if (cursorX > 0 && cursorX + compW_real > LIMITE_ESPACO_LARGURA) {
                cursorX = 0;
                cursorY += linhaMaxH + ESPACAMENTO_COMPONENTES;
                linhaMaxH = 0;
            }

            for (int v : comp.vertices) {
                com.mxgraph.model.mxGeometry geo = mxGrafo.getModel().getGeometry(verticesVisuais[v]);
                double localX = (geo.getX() - comp.minX) * escalaFinal;
                double localY = (geo.getY() - comp.minY) * escalaFinal;

                geo.setX(offsetX + cursorX + localX);
                geo.setY(offsetY + cursorY + localY);
            }

            cursorX += compW_real + ESPACAMENTO_COMPONENTES;
            linhaMaxH = Math.max(linhaMaxH, compH_real);
        }
    }

    /**
     * Cria e configura o componente interativo de exibição do mxGraph.
     */
    private mxGraphComponent criarComponenteGrafico(mxGraph mxGrafo) {
        mxGraphComponent comp = new mxGraphComponent(mxGrafo);
        comp.setConnectable(false);
        comp.setDragEnabled(false);
        if (comp.getGraphHandler() != null) {
            comp.getGraphHandler().setEnabled(false);
        }
        if (comp.getSelectionCellsHandler() != null) {
            comp.getSelectionCellsHandler().setEnabled(false);
        }
        comp.getViewport().setOpaque(true);
        comp.getViewport().setBackground(Color.WHITE);
        comp.setBorder(null);
        comp.setPreferredSize(new Dimension(760, 470));
        return comp;
    }
}
