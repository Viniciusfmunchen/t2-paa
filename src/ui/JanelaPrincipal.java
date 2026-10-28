package ui;

import algorithms.ResultadoAlgoritmo;
import algorithms.ServicoAlgoritmosGrafo;
import io.LeitorGrafoTxt;
import model.Grafo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Janela principal da aplicação (Java Swing) que coordena a interação do
 * usuário.
 */
public class JanelaPrincipal extends JFrame {

    private Grafo grafoAtual;
    private final LeitorGrafoTxt leitorGrafo;
    private final ServicoAlgoritmosGrafo servicoAlgoritmos;

    private JLabel lblStatusArquivo;
    private JTextArea areaConsole;
    private PainelVisualizacaoGrafo painelGrafo;
    private final List<JButton> botoesAlgoritmos = new ArrayList<>();

    public JanelaPrincipal() {
        super("UNIOESTE - Projeto e Análise de Algoritmos - Algoritmos em Grafos");
        this.leitorGrafo = new LeitorGrafoTxt();
        this.servicoAlgoritmos = new ServicoAlgoritmosGrafo();

        configurarJanela();
        inicializarComponentes();
    }

    /**
     * Configura propriedades da janela JFrame.
     */
    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
    }

    /**
     * Cria e distribui os painéis principais na janela.
     */
    private void inicializarComponentes() {
        add(criarPainelSuperior(), BorderLayout.NORTH);
        add(criarPainelMenu(), BorderLayout.WEST);

        painelGrafo = new PainelVisualizacaoGrafo();
        painelGrafo.setBorder(new TitledBorder("Visualização Gráfica do Grafo"));
        add(painelGrafo, BorderLayout.CENTER);

        add(criarPainelConsole(), BorderLayout.SOUTH);
    }

    /**
     * Constrói a barra superior contendo botão de carregar e label de status.
     */
    private JPanel criarPainelSuperior() {
        JPanel painelTopo = new JPanel(new BorderLayout(10, 10));
        painelTopo.setBorder(new EmptyBorder(10, 10, 0, 10));

        JButton btnCarregar = new JButton("Carregar Grafo (.txt)");
        btnCarregar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnCarregar.addActionListener(e -> acaoCarregarGrafo());

        lblStatusArquivo = new JLabel(
                "Nenhum grafo carregado. Por favor, carregue um arquivo texto para habilitar os algoritmos.");
        lblStatusArquivo.setFont(new Font("SansSerif", Font.PLAIN, 13));

        painelTopo.add(btnCarregar, BorderLayout.WEST);
        painelTopo.add(lblStatusArquivo, BorderLayout.CENTER);
        return painelTopo;
    }

    /**
     * Constrói o menu lateral esquerdo contendo os botões dos algoritmos.
     */
    private JPanel criarPainelMenu() {
        JPanel painelMenu = new JPanel(new GridLayout(7, 1, 8, 8));
        painelMenu.setBorder(new TitledBorder("Algoritmos Disponíveis"));
        painelMenu.setPreferredSize(new Dimension(280, 0));

        painelMenu.add(criarBotaoAlgoritmo("Desenhar grafo", e -> acaoDesenharGrafoPadrao()));
        painelMenu.add(criarBotaoAlgoritmo("Busca em Profundidade (DFS)", e -> acaoExecutarBusca(true)));
        painelMenu.add(criarBotaoAlgoritmo("Busca em Largura (BFS)", e -> acaoExecutarBusca(false)));
        painelMenu.add(criarBotaoAlgoritmo("Ordenação Topológica", e -> acaoOrdenacaoTopologica()));
        painelMenu.add(criarBotaoAlgoritmo("Comp. Fortemente Conexas", e -> acaoComponentesFortementeConexas()));
        painelMenu.add(criarBotaoAlgoritmo("Algoritmo de Dijkstra", e -> acaoDijkstra()));
        painelMenu.add(criarBotaoAlgoritmo("Algoritmo de Kruskal (AGM)", e -> acaoKruskal()));

        return painelMenu;
    }

    /**
     * Fábrica de botões para os algoritmos com estilo e estado inicial
     * desabilitado.
     */
    private JButton criarBotaoAlgoritmo(String texto, ActionListener listener) {
        JButton btn = new JButton(texto);
        btn.setEnabled(false);
        btn.setFocusPainted(false);
        btn.addActionListener(listener);
        botoesAlgoritmos.add(btn);
        return btn;
    }

    /**
     * Constrói a área inferior para exibição de texto dos resultados.
     */
    private JScrollPane criarPainelConsole() {
        areaConsole = new JTextArea(8, 50);
        areaConsole.setEditable(false);
        areaConsole.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollConsole = new JScrollPane(areaConsole);
        scrollConsole.setBorder(new TitledBorder("Saída dos Algoritmos (Espelhado no System.out)"));
        return scrollConsole;
    }

    /**
     * Habilita ou desabilita todos os botões de algoritmos.
     */
    private void setBotoesAlgoritmosHabilitados(boolean habilitado) {
        for (JButton btn : botoesAlgoritmos) {
            btn.setEnabled(habilitado);
        }
    }

    // =========================================================================
    // Ações dos Componentes e Execução dos Algoritmos
    // =========================================================================

    /**
     * Abre o seletor de arquivos e carrega o grafo a partir do arquivo selecionado.
     */
    private void acaoCarregarGrafo() {
        JFileChooser fileChooser = new JFileChooser(new File("."));
        fileChooser.setDialogTitle("Selecione o arquivo texto de definição do grafo");
        int retorno = fileChooser.showOpenDialog(this);

        if (retorno == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();
            try {
                grafoAtual = leitorGrafo.carregar(arquivo.getAbsolutePath());
                lblStatusArquivo.setText(String.format("Arquivo: %s | Vértices: %d | Arestas: %d | Orientado: %s",
                        arquivo.getName(), grafoAtual.getNumVertices(), grafoAtual.getNumArestas(),
                        grafoAtual.isOrientado() ? "Sim" : "Não"));

                setBotoesAlgoritmosHabilitados(true);
                painelGrafo.renderizarGrafo(grafoAtual, null, false);
                registrarSaida("Grafo carregado com sucesso: " + arquivo.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Carregar Grafo",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * a) Redesenha o grafo em seu estado original.
     */
    private void acaoDesenharGrafoPadrao() {
        grafoAtual.limparDestaquesArestas();
        painelGrafo.renderizarGrafo(grafoAtual, null, false);
        registrarSaida("Desenho padrão do grafo atualizado na tela.");
    }

    /**
     * b) e c) Solicita o vértice de origem e executa DFS ou BFS.
     *
     * @param isDfs true para Busca em Profundidade, false para Busca em Largura.
     */
    private void acaoExecutarBusca(boolean isDfs) {
        Integer origem = solicitarVerticeOrigem();
        if (origem == null) {
            return;
        }

        ResultadoAlgoritmo res = isDfs
                ? servicoAlgoritmos.executarDFS(grafoAtual, origem)
                : servicoAlgoritmos.executarBFS(grafoAtual, origem);

        tratarResultado(res, false);
    }

    /**
     * d) Executa a Ordenação Topológica.
     */
    private void acaoOrdenacaoTopologica() {
        ResultadoAlgoritmo res = servicoAlgoritmos.executarOrdenacaoTopologica(grafoAtual);
        tratarResultado(res, false);
    }

    /**
     * e) Executa Componentes Fortemente Conexas e colore os vértices no painel
     * gráfico.
     */
    private void acaoComponentesFortementeConexas() {
        ResultadoAlgoritmo res = servicoAlgoritmos.executarComponentesFortementeConexas(grafoAtual);
        tratarResultado(res, true);
    }

    /**
     * f) Valida aplicabilidade e executa o algoritmo de Dijkstra.
     */
    private void acaoDijkstra() {
        String erroValidacao = servicoAlgoritmos.validarAplicabilidadeDijkstra(grafoAtual);
        if (erroValidacao != null) {
            tratarResultado(new ResultadoAlgoritmo(false, erroValidacao, null, false), false);
            return;
        }

        Integer origem = solicitarVerticeOrigem();
        if (origem == null) {
            return;
        }

        ResultadoAlgoritmo res = servicoAlgoritmos.executarDijkstra(grafoAtual, origem);
        tratarResultado(res, false);
    }

    /**
     * g) Executa o algoritmo de Kruskal e destaca em vermelho as arestas da AGM.
     */
    private void acaoKruskal() {
        ResultadoAlgoritmo res = servicoAlgoritmos.executarKruskal(grafoAtual);
        tratarResultado(res, true);
    }

    /**
     * Exibe caixa de diálogo para o usuário informar o vértice inicial.
     *
     * @return Número do vértice válido ou null se cancelado/inválido.
     */
    private Integer solicitarVerticeOrigem() {
        String entrada = JOptionPane.showInputDialog(this,
                "Informe o vértice de origem (0 a " + (grafoAtual.getNumVertices() - 1) + "):",
                "Entrada de Parâmetro", JOptionPane.QUESTION_MESSAGE);

        if (entrada == null) {
            return null;
        }
        try {
            return Integer.parseInt(entrada.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite um número inteiro válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Processa o retorno do algoritmo, imprimindo na saída padrão, no painel
     * inferior e atualizando o desenho.
     *
     * @param res              Objeto de resultado.
     * @param atualizarDesenho Indica se o algoritmo altera as cores de vértices ou
     *                         arestas.
     */
    private void tratarResultado(ResultadoAlgoritmo res, boolean atualizarDesenho) {
        registrarSaida(res.getTextoSaida());
        if (!res.isSucesso()) {
            JOptionPane.showMessageDialog(this, res.getTextoSaida(), "Algoritmo Não Aplicável",
                    JOptionPane.WARNING_MESSAGE);
        } else if (atualizarDesenho) {
            painelGrafo.renderizarGrafo(grafoAtual, res.getCoresVertices(), res.isDestacarArestasAgm());
        }
    }

    /**
     * Imprime simultaneamente na saída padrão (System.out) conforme exigido e na
     * área de texto da GUI.
     *
     * @param mensagem Texto a ser registrado.
     */
    private void registrarSaida(String mensagem) {
        System.out.println(mensagem);
        areaConsole.append(mensagem + "\n--------------------------------------------------\n");
        areaConsole.setCaretPosition(areaConsole.getDocument().getLength());
    }
}
