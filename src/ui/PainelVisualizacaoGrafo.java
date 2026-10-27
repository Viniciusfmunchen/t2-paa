package ui;

import model.Aresta;
import model.Grafo;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class PainelVisualizacaoGrafo extends JPanel {
    private mxGraphComponent graphComponent;

    public PainelVisualizacaoGrafo() {
        setLayout(new BorderLayout());
    }

    public void renderizarGrafo(Grafo grafo, String[] coresVertices, boolean destacarArestas) {
        removeAll();

        mxGraph mxGrafo = new mxGraph() {
            @Override
            public boolean isCellEditable(Object cell) {
                return false; // Impede que o usuário digite texto dentro dos nós
            }
        };

        Object parent = mxGrafo.getDefaultParent();
        mxGrafo.getModel().beginUpdate();

        try {
            int n = grafo.getNumVertices();
            Object[] verticesVisuais = new Object[n];

            // 1. Cria os vértices no (0,0).
            for (int i = 0; i < n; i++) {
                String corPreenchimento = (coresVertices != null && coresVertices[i] != null)
                        ? coresVertices[i]
                        : "#E8EEF5";

                String estiloVertice = mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_ELLIPSE + ";"
                        + mxConstants.STYLE_FILLCOLOR + "=" + corPreenchimento + ";"
                        + mxConstants.STYLE_STROKECOLOR + "=#2B4C7E;"
                        + mxConstants.STYLE_STROKEWIDTH + "=2;"
                        + mxConstants.STYLE_FONTCOLOR + "=#000000;"
                        + mxConstants.STYLE_FONTSIZE + "=14;"
                        + mxConstants.STYLE_FONTSTYLE + "=" + mxConstants.FONT_BOLD;

                verticesVisuais[i] = mxGrafo.insertVertex(parent, null, String.valueOf(i), 0, 0, 42, 42, estiloVertice);
            }

            // 2. Cria as arestas
            String setaFinal = grafo.isOrientado() ? mxConstants.ARROW_CLASSIC : mxConstants.NONE;
            for (int i = 0; i < grafo.getNumArestas(); i++) {
                Aresta a = grafo.getArestas()[i];
                boolean destacada = destacarArestas && a.isDestacada();

                String corAresta = destacada ? "#D92B2B" : "#555555";
                String espessura = destacada ? "3.5" : "1.5";

                String estiloAresta = mxConstants.STYLE_ENDARROW + "=" + setaFinal + ";"
                        + mxConstants.STYLE_STROKECOLOR + "=" + corAresta + ";"
                        + mxConstants.STYLE_STROKEWIDTH + "=" + espessura + ";"
                        + mxConstants.STYLE_FONTCOLOR + "=" + (destacada ? "#D92B2B" : "#111111") + ";"
                        + mxConstants.STYLE_FONTSIZE + "=13;"
                        + mxConstants.STYLE_FONTSTYLE + "=" + mxConstants.FONT_BOLD + ";"
                        + mxConstants.STYLE_LABEL_BACKGROUNDCOLOR + "=#FFFFFF";

                mxGrafo.insertEdge(parent, null, String.valueOf(a.getPeso()),
                        verticesVisuais[a.getU()], verticesVisuais[a.getV()], estiloAresta);
            }

            // 3. Aplica a Força Direcionada (Orgânico)
            mxFastOrganicLayout layoutOrganico = new mxFastOrganicLayout(mxGrafo);
            layoutOrganico.setForceConstant(100); // Reduzido um pouco para manter a Fig 2 unida
            layoutOrganico.execute(parent);

            // --- INÍCIO DO AJUSTE GEOMÉTRICO (Bordas e Grafos Desconexos) ---
            Object[] vertices = mxGrafo.getChildVertices(parent);
            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
            double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;

            // Varre todos os nós para descobrir o tamanho real do estrago gerado pela
            // repulsão
            for (Object v : vertices) {
                com.mxgraph.model.mxGeometry geo = mxGrafo.getModel().getGeometry(v);
                if (geo != null) {
                    minX = Math.min(minX, geo.getX());
                    minY = Math.min(minY, geo.getY());
                    maxX = Math.max(maxX, geo.getX());
                    maxY = Math.max(maxY, geo.getY());
                }
            }

            // Calcula a largura e altura do grafo espalhado
            double largura = maxX - minX;
            double altura = maxY - minY;

            // Se os subgrafos foram empurrados além de 700 pixels (ex: Fig 3), criamos uma
            // escala de compressão
            double limiteEspaco = 700.0;
            double escala = 1.0;
            if (largura > limiteEspaco || altura > limiteEspaco) {
                escala = Math.min(limiteEspaco / largura, limiteEspaco / altura);
            }

            // Aplica as correções: Traz para uma margem de 60px (afasta da borda) e
            // comprime se necessário
            for (Object v : vertices) {
                com.mxgraph.model.mxGeometry geo = mxGrafo.getModel().getGeometry(v);
                if (geo != null) {
                    double novoX = 60 + (geo.getX() - minX) * escala;
                    double novoY = 60 + (geo.getY() - minY) * escala;
                    geo.setX(novoX);
                    geo.setY(novoY);
                }
            }
            // --- FIM DO AJUSTE GEOMÉTRICO ---

            // 4. Aplica o layout paralelo para curvar setas de ida e volta (como na Fig 1
            // entre 2 e 4)
            mxParallelEdgeLayout layoutParalelas = new mxParallelEdgeLayout(mxGrafo);
            layoutParalelas.execute(parent);

        } finally {
            mxGrafo.getModel().endUpdate();
        }

        graphComponent = new mxGraphComponent(mxGrafo);
        graphComponent.setConnectable(false);
        graphComponent.getViewport().setOpaque(true);
        graphComponent.getViewport().setBackground(Color.WHITE);
        graphComponent.setBorder(null);

        // Garante que o painel interno tenha espaço suficiente para a nossa Bounding
        // Box
        graphComponent.setPreferredSize(new Dimension(800, 600));

        add(graphComponent, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}