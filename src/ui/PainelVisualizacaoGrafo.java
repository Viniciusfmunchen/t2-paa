package ui;

import model.Aresta;
import model.Grafo;
import structures.UnionFind;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Painel Swing dedicado a renderizacao grafica e espacial de grafos atraves da biblioteca JGraphX,
 * suportando coloracao customizada de vertices e destaque de arestas da AGM.
 */
public class PainelVisualizacaoGrafo extends JPanel {

    private static final String COR_VERTICE_PADRAO = "#E8EEF5";
    private static final String COR_VERTICE_BORDA = "#2B4C7E";
    private static final String COR_ARESTA_PADRAO = "#555555";
    private static final String COR_ARESTA_DESTAQUE = "#D92B2B";
    private static final int TAMANHO_VERTICE = 42;

    private static final double DISTANCIA_ALVO_ARESTA = 125.0;
    private static final double LIMITE_LARGURA = 700.0;
    private static final double LIMITE_ALTURA = 420.0;
    private static final double MARGEM = 35.0;
    private static final double ESPACAMENTO = 60.0;

    /**
     * Construtor do painel de visualizacao grafica do grafo.
     *
     * Pré-condição: Nenhuma.
     * Pós-condição: O painel Swing e instanciado com layout BorderLayout.
     */
    public PainelVisualizacaoGrafo() {
        setLayout(new BorderLayout());
    }

    /**
     * Constroi e renderiza a representacao visual do grafo com calculo de layout organico e empacotamento espacial.
     *
     * Pré-condição: grafo nao deve ser nulo.
     * Pós-condição: Os elementos graficos anteriores sao removidos e a nova representacao do grafo e montada e exibida.
     *
     * @param grafo           Instancia do grafo a ser renderizado visualmente.
     * @param coresVertices   Vetor com as cores hexadecimais para coloracao individual de vertices, ou null.
     * @param destacarArestas true se as arestas selecionadas da AGM devem ser desenhadas com destaque visual.
     */
    public void renderizarGrafo(Grafo grafo, String[] coresVertices, boolean destacarArestas) {
        removeAll();

        mxGraph mxGrafo = new mxGraph();
        configurarEstilos(mxGrafo);
        Object parent = mxGrafo.getDefaultParent();

        mxGrafo.getModel().beginUpdate();
        try {
            Object[] vertices = inserirVertices(mxGrafo, parent, grafo, coresVertices);
            inserirArestas(mxGrafo, parent, grafo, vertices, destacarArestas);

            new mxCircleLayout(mxGrafo).execute(parent);

            mxOrganicLayout layout = new mxOrganicLayout(mxGrafo);
            layout.setOptimizeEdgeCrossing(true);
            layout.setEdgeCrossingCostFactor(1_000_000.0);
            layout.setOptimizeEdgeDistance(true);
            layout.setOptimizeNodeDistribution(true);
            layout.setOptimizeBorderLine(true);
            layout.setFineTuning(true);
            layout.setMaxIterations(2000);
            layout.execute(parent);

            ajustarComponentes(mxGrafo, grafo, vertices);

            new mxParallelEdgeLayout(mxGrafo, 30).execute(parent);

            mxGrafo.setCellsLocked(true);
        } finally {
            mxGrafo.getModel().endUpdate();
        }

        add(criarComponente(mxGrafo), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
     * Configura o conjunto padrao de estilos visuais de vertices e arestas na folha de estilos do grafo.
     *
     * Pré-condição: graph nao deve ser nulo.
     * Pós-condição: Os estilos visuais padrao de vertices e arestas sao registrados na folha de estilos do grafo.
     *
     * @param graph Objeto do grafo JGraphX cuja folha de estilos sera configurada.
     */
    private void configurarEstilos(mxGraph graph) {
        mxStylesheet sheet = graph.getStylesheet();

        Map<String, Object> vStyle = sheet.getDefaultVertexStyle();
        vStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vStyle.put(mxConstants.STYLE_FILLCOLOR, COR_VERTICE_PADRAO);
        vStyle.put(mxConstants.STYLE_STROKECOLOR, COR_VERTICE_BORDA);
        vStyle.put(mxConstants.STYLE_STROKEWIDTH, 2);
        vStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        vStyle.put(mxConstants.STYLE_FONTSIZE, 14);
        vStyle.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);

        Map<String, Object> eStyle = sheet.getDefaultEdgeStyle();
        eStyle.put(mxConstants.STYLE_STROKECOLOR, COR_ARESTA_PADRAO);
        eStyle.put(mxConstants.STYLE_STROKEWIDTH, "1.5");
        eStyle.put(mxConstants.STYLE_FONTCOLOR, "#111111");
        eStyle.put(mxConstants.STYLE_FONTSIZE, 13);
        eStyle.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
        eStyle.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#FFFFFF");
    }

    /**
     * Insere as celulas visuais dos vertices no modelo do grafo JGraphX.
     *
     * Pré-condição: graph, parent e grafo nao devem ser nulos.
     * Pós-condição: Retorna um vetor contendo as referencias para os vertices inseridos no modelo.
     *
     * @param graph  Grafo JGraphX receptor.
     * @param parent Celula raiz pai do JGraphX.
     * @param grafo  Grafo de dominio contendo a contagem de vertices.
     * @param cores  Vetor de cores customizadas por vertice ou null.
     * @return Vetor de objetos representando as celulas dos vertices criados.
     */
    private Object[] inserirVertices(mxGraph graph, Object parent, Grafo grafo, String[] cores) {
        int n = grafo.getNumVertices();
        Object[] vertices = new Object[n];
        for (int i = 0; i < n; i++) {
            String estilo = (cores != null && cores[i] != null) ? (mxConstants.STYLE_FILLCOLOR + "=" + cores[i]) : null;
            vertices[i] = graph.insertVertex(parent, null, String.valueOf(i), 0, 0, TAMANHO_VERTICE, TAMANHO_VERTICE,
                    estilo);
        }
        return vertices;
    }

    /**
     * Insere as conexoes visuais de arestas no modelo do grafo JGraphX.
     *
     * Pré-condição: graph, parent, grafo e vertices nao devem ser nulos.
     * Pós-condição: As arestas sao criadas no modelo com os pesos formatados e estilos apropriados.
     *
     * @param graph    Grafo JGraphX receptor.
     * @param parent   Celula raiz pai do JGraphX.
     * @param grafo    Grafo de dominio contendo a colecao de arestas.
     * @param vertices Vetor com as referencias para os vertices previamente inseridos.
     * @param destacar true se as arestas selecionadas devem receber destaque visual.
     */
    private void inserirArestas(mxGraph graph, Object parent, Grafo grafo, Object[] vertices, boolean destacar) {
        String seta = grafo.isOrientado() ? mxConstants.ARROW_CLASSIC : mxConstants.NONE;
        for (int i = 0; i < grafo.getNumArestas(); i++) {
            Aresta a = grafo.getAresta(i);
            boolean dest = destacar && a.isDestacada();
            String estilo = mxConstants.STYLE_ENDARROW + "=" + seta;
            if (dest) {
                estilo += ";" + mxConstants.STYLE_STROKECOLOR + "=" + COR_ARESTA_DESTAQUE
                        + ";" + mxConstants.STYLE_STROKEWIDTH + "=3.5"
                        + ";" + mxConstants.STYLE_FONTCOLOR + "=" + COR_ARESTA_DESTAQUE;
            }
            graph.insertEdge(parent, null, String.valueOf(a.getPeso()),
                    vertices[a.getU()], vertices[a.getV()], estilo);
        }
    }

    /**
     * Ajusta o posicionamento geometrico relativo das componentes conexas do grafo na tela,
     * organizando-as em grade compacta para prevenir sobreposicoes espaciais.
     *
     * Pré-condição: graph, grafo e vertices nao devem ser nulos.
     * Pós-condição: As coordenadas espaciais das celulas dos vertices no modelo JGraphX sao reposicionadas adequadamente.
     *
     * @param graph    Grafo JGraphX contendo as geometrias dos vertices.
     * @param grafo    Grafo de dominio contendo a topologia de conexoes.
     * @param vertices Arranjo das celulas dos vertices no JGraphX.
     */
    private void ajustarComponentes(mxGraph graph, Grafo grafo, Object[] vertices) {
        int n = grafo.getNumVertices();
        if (n == 0) {
            return;
        }

        double menorDist = Double.MAX_VALUE;
        for (int i = 0; i < grafo.getNumArestas(); i++) {
            Aresta a = grafo.getAresta(i);
            mxGeometry u = graph.getModel().getGeometry(vertices[a.getU()]);
            mxGeometry v = graph.getModel().getGeometry(vertices[a.getV()]);
            menorDist = Math.min(menorDist, Math.hypot(u.getX() - v.getX(), u.getY() - v.getY()));
        }
        double fator = (menorDist > 0 && menorDist < DISTANCIA_ALVO_ARESTA) ? (DISTANCIA_ALVO_ARESTA / menorDist) : 1.0;

        UnionFind uf = new UnionFind(n);
        for (int i = 0; i < grafo.getNumArestas(); i++) {
            Aresta a = grafo.getAresta(i);
            uf.unir(a.getU(), a.getV());
        }

        Map<Integer, List<Integer>> mapa = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            mapa.computeIfAbsent(uf.encontrar(i), k -> new ArrayList<>()).add(i);
        }

        List<Comp> comps = new ArrayList<>();
        for (List<Integer> nos : mapa.values()) {
            Comp c = new Comp(nos);
            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
            double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
            for (int v : nos) {
                mxGeometry g = graph.getModel().getGeometry(vertices[v]);
                minX = Math.min(minX, g.getX());
                minY = Math.min(minY, g.getY());
                maxX = Math.max(maxX, g.getX() + g.getWidth());
                maxY = Math.max(maxY, g.getY() + g.getHeight());
            }
            c.minX = minX;
            c.minY = minY;
            c.w = Math.max(TAMANHO_VERTICE, (maxX - minX) * fator);
            c.h = Math.max(TAMANHO_VERTICE, (maxY - minY) * fator);
            comps.add(c);
        }
        comps.sort((c1, c2) -> Integer.compare(c2.nos.size(), c1.nos.size()));

        double cursorX = 0, cursorY = 0, linhaH = 0, totalW = 0;
        for (Comp c : comps) {
            if (cursorX > 0 && cursorX + c.w > LIMITE_LARGURA) {
                cursorX = 0;
                cursorY += linhaH + ESPACAMENTO;
                linhaH = 0;
            }
            cursorX += c.w + ESPACAMENTO;
            linhaH = Math.max(linhaH, c.h);
            totalW = Math.max(totalW, cursorX - ESPACAMENTO);
        }
        double totalH = cursorY + linhaH;
        double redutor = Math.min(1.0,
                Math.min(LIMITE_LARGURA / Math.max(1, totalW), LIMITE_ALTURA / Math.max(1, totalH)));
        double escalaFinal = fator * redutor;

        double offX = MARGEM + Math.max(0, (LIMITE_LARGURA - totalW * redutor) / 2.0);
        double offY = MARGEM + Math.max(0, (LIMITE_ALTURA - totalH * redutor) / 2.0);

        cursorX = 0;
        cursorY = 0;
        linhaH = 0;
        for (Comp c : comps) {
            double cW = c.w * redutor;
            double cH = c.h * redutor;
            if (cursorX > 0 && cursorX + cW > LIMITE_LARGURA) {
                cursorX = 0;
                cursorY += linhaH + ESPACAMENTO;
                linhaH = 0;
            }
            for (int v : c.nos) {
                mxGeometry g = graph.getModel().getGeometry(vertices[v]);
                g.setX(offX + cursorX + (g.getX() - c.minX) * escalaFinal);
                g.setY(offY + cursorY + (g.getY() - c.minY) * escalaFinal);
            }
            cursorX += cW + ESPACAMENTO;
            linhaH = Math.max(linhaH, cH);
        }
    }

    /**
     * Cria e encapsula o grafo renderizado em um componente visual mxGraphComponent.
     *
     * Pré-condição: graph nao deve ser nulo.
     * Pós-condição: Retorna o componente visual configurado com fundo branco e dimensoes adequadas.
     *
     * @param graph Objeto do grafo JGraphX a ser encapsulado no componente Swing.
     * @return O componente visual mxGraphComponent pronto para insercao no painel da interface.
     */
    private mxGraphComponent criarComponente(mxGraph graph) {
        mxGraphComponent comp = new mxGraphComponent(graph);
        comp.setConnectable(false);
        comp.setEnabled(false);
        comp.getViewport().setOpaque(true);
        comp.getViewport().setBackground(Color.WHITE);
        comp.setBorder(null);
        comp.setPreferredSize(new Dimension(760, 470));
        return comp;
    }

    /**
     * Estrutura auxiliar interna para representar e calcular os limites geometricos de uma componente conexa.
     */
    private static class Comp {
        final List<Integer> nos;
        double minX, minY, w, h;

        /**
         * Construtor da estrutura de dados da componente visual.
         *
         * Pré-condição: nos nao deve ser nulo.
         * Pós-condição: Uma nova instancia descritora de componente e criada com a lista de nos associada.
         *
         * @param nos Lista de identificadores de vertices pertencentes a componente.
         */
        Comp(List<Integer> nos) {
            this.nos = nos;
        }
    }
}
