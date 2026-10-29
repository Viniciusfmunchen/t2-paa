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
 * Janela grafica principal da aplicacao (desenvolvida com Java Swing),
 * fornecendo controles interativos para carregamento de arquivos de grafo,
 * execucao de algoritmos e visualizacao espacial e textual dos resultados.
 */
public class JanelaPrincipal extends JFrame {

    private Grafo grafoAtual;
    private final LeitorGrafoTxt leitorGrafo = new LeitorGrafoTxt();
    private final ServicoAlgoritmosGrafo servico = new ServicoAlgoritmosGrafo();

    private JLabel lblStatus;
    private JTextArea areaConsole;
    private PainelVisualizacaoGrafo painelGrafo;
    private final List<JButton> botoesAlgoritmos = new ArrayList<>();

    /**
     * Construtor da janela principal da aplicacao.
     *
     * Pré-condição: Nenhuma.
     * Pós-condição: A janela principal e instanciada, dimensionada, centralizada na tela
     * e seus componentes graficos sao inicializados.
     */
    public JanelaPrincipal() {
        super("UNIOESTE - Projeto e Análise de Algoritmos - Algoritmos em Grafos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        inicializarComponentes();
    }

    /**
     * Instancia, estiliza e posiciona os componentes da interface grafica na janela.
     *
     * Pré-condição: A janela deve estar em processo de construcao.
     * Pós-condição: Os paineis superior, de menu lateral, de desenho e de console sao adicionados ao container.
     */
    private void inicializarComponentes() {
        JPanel painelTopo = new JPanel(new BorderLayout(10, 10));
        painelTopo.setBorder(new EmptyBorder(10, 10, 0, 10));

        JButton btnCarregar = new JButton("Carregar Grafo (.txt)");
        btnCarregar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnCarregar.addActionListener(e -> acaoCarregarGrafo());

        lblStatus = new JLabel("Nenhum grafo carregado. Carregue um arquivo para habilitar os algoritmos.");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 13));

        painelTopo.add(btnCarregar, BorderLayout.WEST);
        painelTopo.add(lblStatus, BorderLayout.CENTER);
        add(painelTopo, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(7, 1, 8, 8));
        menu.setBorder(new TitledBorder("Algoritmos Disponíveis"));
        menu.setPreferredSize(new Dimension(280, 0));

        menu.add(criarBotao("Desenhar grafo", e -> acaoDesenharPadrao()));
        menu.add(criarBotao("Busca em Profundidade (DFS)", e -> acaoBusca(true)));
        menu.add(criarBotao("Busca em Largura (BFS)", e -> acaoBusca(false)));
        menu.add(criarBotao("Ordenação Topológica",
                e -> tratarResultado("Ordenação Topológica", servico.executarOrdenacaoTopologica(grafoAtual), false)));
        menu.add(criarBotao("Comp. Fortemente Conexas",
                e -> tratarResultado("Componentes Fortemente Conexas", servico.executarComponentesFortementeConexas(grafoAtual), true)));
        menu.add(criarBotao("Algoritmo de Dijkstra", e -> acaoDijkstra()));
        menu.add(criarBotao("Algoritmo de Kruskal (AGM)",
                e -> tratarResultado("Algoritmo de Kruskal (AGM)", servico.executarKruskal(grafoAtual), true)));
        add(menu, BorderLayout.WEST);

        painelGrafo = new PainelVisualizacaoGrafo();
        painelGrafo.setBorder(new TitledBorder("Visualização Gráfica do Grafo"));
        add(painelGrafo, BorderLayout.CENTER);

        areaConsole = new JTextArea(8, 50);
        areaConsole.setEditable(false);
        areaConsole.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollConsole = new JScrollPane(areaConsole);
        scrollConsole.setBorder(new TitledBorder("Saída dos Algoritmos (Espelhado no System.out)"));
        add(scrollConsole, BorderLayout.SOUTH);
    }

    /**
     * Cria e padroniza um botao de comando para a lista de algoritmos.
     *
     * Pré-condição: texto e listener nao devem ser nulos.
     * Pós-condição: Retorna o botao configurado, inicialmente desabilitado e registrado na lista de botoes.
     *
     * @param texto    Rotulo a ser exibido no botao.
     * @param listener Acao disparada quando o usuario acionar o botao.
     * @return O JButton configurado e adicionado a colecao de botoes.
     */
    private JButton criarBotao(String texto, ActionListener listener) {
        JButton btn = new JButton(texto);
        btn.setEnabled(false);
        btn.setFocusPainted(false);
        btn.addActionListener(listener);
        botoesAlgoritmos.add(btn);
        return btn;
    }

    /**
     * Manipula o evento de selecao e leitura de arquivo texto contendo o grafo.
     *
     * Pré-condição: O usuario acionou a acao de carregar arquivo.
     * Pós-condição: Se o arquivo for valido, o grafo e carregado na memoria,
     * a barra de status e atualizada, os botoes sao habilitados e o grafo e renderizado.
     */
    private void acaoCarregarGrafo() {
        File pastaInicial = new File("exemplos").isDirectory() ? new File("exemplos") : new File(".");
        JFileChooser chooser = new JFileChooser(pastaInicial);
        chooser.setDialogTitle("Selecione o arquivo de definição do grafo");
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File arq = chooser.getSelectedFile();
            try {
                grafoAtual = leitorGrafo.carregar(arq.getAbsolutePath());
                lblStatus.setText(String.format("Arquivo: %s | Vértices: %d | Arestas: %d | Orientado: %s",
                        arq.getName(), grafoAtual.getNumVertices(), grafoAtual.getNumArestas(),
                        grafoAtual.isOrientado() ? "Sim" : "Não"));

                botoesAlgoritmos.forEach(b -> b.setEnabled(true));
                painelGrafo.renderizarGrafo(grafoAtual, null, false);
                registrarSaida("=== Carregamento de Grafo ===\nGrafo carregado com sucesso: " + arq.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Carregar Grafo", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Restaura a visualizacao grafica padrao do grafo carregado, limpando coloracoes e destaques de arestas.
     *
     * Pré-condição: Deve haver um grafo carregado atualmente (grafoAtual != null).
     * Pós-condição: O painel grafico e redesenhado sem coloracoes ou destaques de algoritmos.
     */
    private void acaoDesenharPadrao() {
        grafoAtual.limparDestaquesArestas();
        painelGrafo.renderizarGrafo(grafoAtual, null, false);
        registrarSaida("=== Desenho de Grafo ===\nDesenho padrão do grafo atualizado na tela.");
    }

    /**
     * Executa o algoritmo de busca (DFS ou BFS) apos solicitar o vertice de origem ao usuario.
     *
     * Pré-condição: grafoAtual nao deve ser nulo.
     * Pós-condição: Se a origem informada for valida, o algoritmo correspondente e executado e a saida exibida no console.
     *
     * @param isDfs true para executar Busca em Profundidade (DFS), false para Busca em Largura (BFS).
     */
    private void acaoBusca(boolean isDfs) {
        Integer origem = solicitarOrigem();
        if (origem != null) {
            String nome = isDfs ? "Busca em Profundidade (DFS)" : "Busca em Largura (BFS)";
            ResultadoAlgoritmo res = isDfs ? servico.executarDFS(grafoAtual, origem) : servico.executarBFS(grafoAtual, origem);
            tratarResultado(nome, res, false);
        }
    }

    /**
     * Executa o algoritmo de Dijkstra apos validacao de compatibilidade e solicitacao do vertice de partida.
     *
     * Pré-condição: grafoAtual nao deve ser nulo.
     * Pós-condição: Se aplicavel e com origem valida, executa o calculo de caminhos minimos e exibe o relatorio.
     */
    private void acaoDijkstra() {
        String erro = servico.validarAplicabilidadeDijkstra(grafoAtual);
        if (erro != null) {
            tratarResultado("Algoritmo de Dijkstra", ResultadoAlgoritmo.erro(erro), false);
            return;
        }
        Integer origem = solicitarOrigem();
        if (origem != null) {
            tratarResultado("Algoritmo de Dijkstra", servico.executarDijkstra(grafoAtual, origem), false);
        }
    }

    /**
     * Apresenta uma caixa de dialogo solicitando o vertice de origem ao usuario.
     *
     * Pré-condição: grafoAtual deve estar instanciado.
     * Pós-condição: Retorna o valor inteiro digitado pelo usuario ou null caso a operacao seja cancelada ou invalida.
     *
     * @return O vertice de origem informado pelo usuario, ou null caso cancelado ou invalido.
     */
    private Integer solicitarOrigem() {
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
     * Processa e direciona o resultado de um algoritmo, emitindo saidas no console e alertas ou redesenho visual.
     *
     * Pré-condição: nomeAlgoritmo e res nao devem ser nulos.
     * Pós-condição: A saida e registrada no console e no terminal; se houver erro, exibe dialogo de aviso;
     * se houver sucesso e solicitacao de atualizacao visual, re-renderiza o painel com as novas cores/destaques.
     *
     * @param nomeAlgoritmo    Titulo descritivo do algoritmo executado.
     * @param res              Instancia contendo os dados do resultado produzido.
     * @param atualizarDesenho true se o grafo visual deve ser atualizado na interface, false caso contrario.
     */
    private void tratarResultado(String nomeAlgoritmo, ResultadoAlgoritmo res, boolean atualizarDesenho) {
        String cabecalho = "=== " + nomeAlgoritmo + " ===";
        registrarSaida(cabecalho + "\n" + res.getTextoSaida());
        if (!res.isSucesso()) {
            JOptionPane.showMessageDialog(this, res.getTextoSaida(), "Algoritmo Não Aplicável", JOptionPane.WARNING_MESSAGE);
        } else if (atualizarDesenho) {
            painelGrafo.renderizarGrafo(grafoAtual, res.getCoresVertices(), res.isDestacarArestasAgm());
        }
    }

    /**
     * Registra simultaneamente uma mensagem de texto na saida padrao do terminal e na area de console da interface Swing.
     *
     * Pré-condição: msg nao deve ser nula.
     * Pós-condição: A mensagem e enviada ao System.out e anexada ao console grafico com rolagem automatica.
     *
     * @param msg Texto a ser registrado.
     */
    private void registrarSaida(String msg) {
        System.out.println(msg);
        areaConsole.append(msg + "\n--------------------------------------------------\n");
        areaConsole.setCaretPosition(areaConsole.getDocument().getLength());
    }
}
