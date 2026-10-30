import sys
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN
from pptx.enum.shapes import MSO_SHAPE

def create_deck():
    prs = Presentation()
    # 16:9 widescreen
    prs.slide_width = Inches(13.333)
    prs.slide_height = Inches(7.5)
    blank_layout = prs.slide_layouts[6]

    # Cores da Paleta
    NAVY = RGBColor(26, 54, 93)       # #1A365D - Títulos principais e capa
    BLUE = RGBColor(43, 108, 176)     # #2B6CB0 - Subtítulos e detalhes
    LIGHT_BG = RGBColor(248, 250, 252)# #F8FAFC - Fundo de cards
    BORDER = RGBColor(226, 232, 240)  # #E2E8F0 - Bordas de cards
    DARK_TEXT = RGBColor(30, 41, 59)  # #1E293B - Texto padrão
    MUTED = RGBColor(100, 116, 139)   # #64748B - Metadados
    WHITE = RGBColor(255, 255, 255)
    GREEN = RGBColor(47, 133, 90)     # #2F855A - Destaque positivo
    RED = RGBColor(197, 48, 48)       # #C53030 - Destaque Kruskal

    def add_header(slide, title_text, category_text="SEMINÁRIO DE ALGORITMOS EM GRAFOS • UNIOESTE"):
        # Header banner
        cat_box = slide.shapes.add_textbox(Inches(0.8), Inches(0.4), Inches(11.7), Inches(0.4))
        tf_cat = cat_box.text_frame
        tf_cat.word_wrap = True
        p_cat = tf_cat.paragraphs[0]
        p_cat.text = category_text.upper()
        p_cat.font.size = Pt(10)
        p_cat.font.bold = True
        p_cat.font.color.rgb = BLUE
        p_cat.font.name = 'Calibri'

        # Main Title
        title_box = slide.shapes.add_textbox(Inches(0.8), Inches(0.7), Inches(11.7), Inches(0.8))
        tf_title = title_box.text_frame
        tf_title.word_wrap = True
        p_title = tf_title.paragraphs[0]
        p_title.text = title_text
        p_title.font.size = Pt(24)
        p_title.font.bold = True
        p_title.font.color.rgb = NAVY
        p_title.font.name = 'Calibri'

        # Dividing line
        line = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0.8), Inches(1.5), Inches(11.733), Inches(0.02))
        line.fill.solid()
        line.fill.fore_color.rgb = BORDER
        line.line.color.rgb = BORDER

    def add_card(slide, left, top, width, height, title="", title_color=NAVY):
        shape = slide.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, left, top, width, height)
        shape.fill.solid()
        shape.fill.fore_color.rgb = LIGHT_BG
        shape.line.color.rgb = BORDER
        shape.line.width = Pt(1.5)
        
        if title:
            tb = slide.shapes.add_textbox(left + Inches(0.2), top + Inches(0.15), width - Inches(0.4), Inches(0.5))
            tf = tb.text_frame
            tf.word_wrap = True
            p = tf.paragraphs[0]
            p.text = title
            p.font.size = Pt(14)
            p.font.bold = True
            p.font.color.rgb = title_color
            p.font.name = 'Calibri'
        return shape

    # =========================================================================
    # SLIDE 1: CAPA
    # =========================================================================
    slide1 = prs.slides.add_slide(blank_layout)
    # Background accent banner
    bg_shape = slide1.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0), Inches(0), Inches(13.333), Inches(7.5))
    bg_shape.fill.solid()
    bg_shape.fill.fore_color.rgb = LIGHT_BG
    bg_shape.line.fill.background()

    # Top accent bar
    top_bar = slide1.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0), Inches(0), Inches(13.333), Inches(0.15))
    top_bar.fill.solid()
    top_bar.fill.fore_color.rgb = BLUE
    top_bar.line.fill.background()

    # Main Card
    main_card = slide1.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(1.5), Inches(1.0), Inches(10.333), Inches(5.5))
    main_card.fill.solid()
    main_card.fill.fore_color.rgb = WHITE
    main_card.line.color.rgb = BORDER
    main_card.line.width = Pt(1.5)

    tb = slide1.shapes.add_textbox(Inches(2.0), Inches(1.4), Inches(9.333), Inches(4.7))
    tf = tb.text_frame
    tf.word_wrap = True

    p0 = tf.paragraphs[0]
    p0.text = "UNIOESTE • UNIVERSIDADE ESTADUAL DO OESTE DO PARANÁ"
    p0.font.size = Pt(12)
    p0.font.bold = True
    p0.font.color.rgb = BLUE
    p0.font.name = 'Calibri'

    p1 = tf.add_paragraph()
    p1.text = "Ciência da Computação • Disciplina de Projeto e Análise de Algoritmos"
    p1.font.size = Pt(12)
    p1.font.color.rgb = MUTED
    p1.font.name = 'Calibri'
    p1.space_after = Pt(24)

    p2 = tf.add_paragraph()
    p2.text = "GraphLab PAA"
    p2.font.size = Pt(36)
    p2.font.bold = True
    p2.font.color.rgb = NAVY
    p2.font.name = 'Calibri'

    p3 = tf.add_paragraph()
    p3.text = "Implementação, Análise e Visualização de Algoritmos em Grafos"
    p3.font.size = Pt(18)
    p3.font.color.rgb = BLUE
    p3.font.name = 'Calibri'
    p3.space_after = Pt(28)

    p4 = tf.add_paragraph()
    p4.text = "Docentes: Prof. Dr. Rômulo & Prof. Dr. Newton Spolaôr\nAcadêmico: Vinicius Fontana Munchen\nTempo de Apresentação: 5 minutos"
    p4.font.size = Pt(13)
    p4.font.color.rgb = DARK_TEXT
    p4.font.name = 'Calibri'

    # =========================================================================
    # SLIDE 2: VISÃO GERAL E ARQUITETURA
    # =========================================================================
    slide2 = prs.slides.add_slide(blank_layout)
    add_header(slide2, "1. Visão Geral e Arquitetura do Software")

    # Coluna 1: Princípios e Edital
    add_card(slide2, Inches(0.8), Inches(1.8), Inches(5.6), Inches(5.0), "Conformidade Rigorosa com o Edital")
    tb2_1 = slide2.shapes.add_textbox(Inches(1.0), Inches(2.5), Inches(5.2), Inches(4.0))
    tf2_1 = tb2_1.text_frame
    tf2_1.word_wrap = True

    items2_1 = [
        ("Estruturas 100% Próprias:", " Proibição estrita de bibliotecas prontas do Java (como java.util.LinkedList, ArrayList, Stack, Queue) para representação e algoritmos."),
        ("Linguagem Java SE 21:", " Aplicação nativa orientada a objetos com tipagem estática e alta previsibilidade."),
        ("Arquitetura em Camadas:", " Separação limpa entre Domínio (model), Estruturas Próprias (structures), Algoritmos (algorithms), I/O (io) e Interface (ui)."),
        ("Padrão Fachada (Facade):", " Classe ServicoAlgoritmosGrafo unifica o acesso a todos os algoritmos, desacoplando a interface gráfica.")
    ]
    for i, (bold, text) in enumerate(items2_1):
        p = tf2_1.paragraphs[0] if i == 0 else tf2_1.add_paragraph()
        p.space_after = Pt(10)
        r1 = p.add_run()
        r1.text = "• " + bold
        r1.font.bold = True
        r1.font.size = Pt(12)
        r1.font.color.rgb = NAVY
        r2 = p.add_run()
        r2.text = text
        r2.font.size = Pt(12)
        r2.font.color.rgb = DARK_TEXT

    # Coluna 2: Interface e Visualização
    add_card(slide2, Inches(6.9), Inches(1.8), Inches(5.6), Inches(5.0), "Visualização Gráfica e Feedback")
    tb2_2 = slide2.shapes.add_textbox(Inches(7.1), Inches(2.5), Inches(5.2), Inches(4.0))
    tf2_2 = tb2_2.text_frame
    tf2_2.word_wrap = True

    items2_2 = [
        ("Biblioteca JGraphX:", " Permissão explícita do edital (Item 2.a) para ferramentas de desenho como Graphviz/JGraphX. Biblioteca Java pura e multiplataforma."),
        ("Minimização de Cruzamentos:", " Layout orgânico baseado em Recozimento Simulado (Simulated Annealing) com penalização de cruzamento de arestas."),
        ("Espelhamento de Saídas:", " Exibição simultânea no console gráfico (Swing) e na saída padrão (System.out), identificada com títulos."),
        ("Suporte Multiplataforma:", " Execução testada e homologada em Windows, Linux e macOS com scripts dedicados.")
    ]
    for i, (bold, text) in enumerate(items2_2):
        p = tf2_2.paragraphs[0] if i == 0 else tf2_2.add_paragraph()
        p.space_after = Pt(10)
        r1 = p.add_run()
        r1.text = "• " + bold
        r1.font.bold = True
        r1.font.size = Pt(12)
        r1.font.color.rgb = BLUE
        r2 = p.add_run()
        r2.text = text
        r2.font.size = Pt(12)
        r2.font.color.rgb = DARK_TEXT

    # =========================================================================
    # SLIDE 3: ESTRUTURAS DE DADOS PRÓPRIAS
    # =========================================================================
    slide3 = prs.slides.add_slide(blank_layout)
    add_header(slide3, "2. Estruturas de Dados Desenvolvidas do Zero")

    cards3 = [
        (Inches(0.8), Inches(1.8), Inches(5.6), Inches(2.35),
         "Lista de Adjacências Ordenada", NAVY,
         "Lista simplesmente encadeada (NoAdjacencia e ListaAdjacencia).\n"
         "• Inserção ordenada crescente: O(grau(u))\n"
         "• Atende estritamente à regra da página 6 do edital: vizinhos mantidos em ordem crescente sem usar sorts prontos."),
        
        (Inches(6.9), Inches(1.8), Inches(5.6), Inches(2.35),
         "Fila Circular Manual (FilaInt)", BLUE,
         "Estrutura FIFO baseada em array circular com capacidade V.\n"
         "• Enfileirar e Desenfileirar: O(1) estrito\n"
         "• Evita realocação de memória e movimentação de elementos; usada exclusivamente na Busca em Largura (BFS)."),
        
        (Inches(0.8), Inches(4.45), Inches(5.6), Inches(2.35),
         "Pilha Estática (PilhaInt)", BLUE,
         "Estrutura LIFO baseada em array com ponteiro de topo.\n"
         "• Empilhar e Desempilhar: O(1) estrito\n"
         "• Utilizada para controlar o tempo de término na Ordenação Topológica e nas duas passadas do algoritmo de Kosaraju."),
        
        (Inches(6.9), Inches(4.45), Inches(5.6), Inches(2.35),
         "Union-Find / Conjuntos Disjuntos", GREEN,
         "Particionamento de conjuntos com duas otimizações teóricas:\n"
         "• Compressão de caminho na busca (encontrar): achata a árvore\n"
         "• União por rank (unir): pendura árvore menor sob a maior\n"
         "• Complexidade quase-linear O(alpha(V)) para o algoritmo de Kruskal.")
    ]

    for left, top, w, h, title, col, desc in cards3:
        add_card(slide3, left, top, w, h, title, col)
        tb_c = slide3.shapes.add_textbox(left + Inches(0.2), top + Inches(0.65), w - Inches(0.4), h - Inches(0.8))
        tf_c = tb_c.text_frame
        tf_c.word_wrap = True
        p_c = tf_c.paragraphs[0]
        p_c.text = desc
        p_c.font.size = Pt(11)
        p_c.font.color.rgb = DARK_TEXT
        p_c.font.name = 'Calibri'

    # =========================================================================
    # SLIDE 4: DESAFIOS ENFRENTADOS
    # =========================================================================
    slide4 = prs.slides.add_slide(blank_layout)
    add_header(slide4, "3. Desafios Enfrentados no Desenvolvimento")

    desafios = [
        ("1. Inserção Ordenada em Lista Encadeada",
         "O edital exige que as listas de adjacência estejam sempre em ordem crescente. Como não era permitido usar Collections.sort(), implementamos a lógica de ponteiros que localiza o ponto exato de inserção em tempo O(grau(u)), garantindo o critério sem consumo de vetores extras."),
        
        ("2. Detecção Precisa de Ciclos na Ordenação Topológica",
         "A ordenação topológica exige estritamente um DAG (grafo orientado acíclico). Implementamos coloração em 3 estados durante a DFS (0: não visitado, 1: em exploração na pilha, 2: finalizado). Se um vizinho em estado 1 for encontrado, detecta-se aresta de retorno (back-edge) e o software bloqueia a execução com mensagem clara."),
        
        ("3. Kosaraju: Construção do Grafo Transposto em Memória",
         "Para isolar as Componentes Fortemente Conexas sem bibliotecas, criamos a rotina que constrói o grafo transposto G^T (invertendo todas as arestas u -> v para v -> u) e realizamos a ordenação decrescente de tamanho das componentes com desempate determinístico."),
        
        ("4. Layout Visual de Grafos Desconexos sem Sobreposição",
         "Em grafos com componentes desconexas (como a Figura 3), o layout padrão aproximava nós independentes. Desenvolvemos o algoritmo de bounding-box com Union-Find que calcula a escala proporcional e empacota as componentes lado a lado sem sobreposição na tela.")
    ]

    for i, (title, text) in enumerate(desafios):
        top_pos = Inches(1.8 + i * 1.25)
        add_card(slide4, Inches(0.8), top_pos, Inches(11.733), Inches(1.1), title, NAVY)
        tb_d = slide4.shapes.add_textbox(Inches(1.0), top_pos + Inches(0.42), Inches(11.3), Inches(0.6))
        tf_d = tb_d.text_frame
        tf_d.word_wrap = True
        p_d = tf_d.paragraphs[0]
        p_d.text = text
        p_d.font.size = Pt(11)
        p_d.font.color.rgb = DARK_TEXT
        p_d.font.name = 'Calibri'

    # =========================================================================
    # SLIDE 5: APRENDIZADOS E COMPLEXIDADE
    # =========================================================================
    slide5 = prs.slides.add_slide(blank_layout)
    add_header(slide5, "4. Principais Aprendizados e Análise de Complexidade")

    add_card(slide5, Inches(0.8), Inches(1.8), Inches(5.6), Inches(5.0), "Complexidade Assintótica na Prática")
    tb5_1 = slide5.shapes.add_textbox(Inches(1.0), Inches(2.5), Inches(5.2), Inches(4.0))
    tf5_1 = tb5_1.text_frame
    tf5_1.word_wrap = True

    items5_1 = [
        ("DFS e BFS em O(V + E):", " Constatamos na prática a superioridade da lista de adjacências sobre a matriz O(V^2), especialmente em grafos esparsos como os do trabalho."),
        ("Kruskal O(E log E + E alpha(V)):", " O gargalo do algoritmo é a ordenação das arestas por peso; as operações de conjunto pelo Union-Find são quase instantâneas."),
        ("Dijkstra O(V^2):", " Busca linear pelo menor vértice não fechado; extremamente eficiente para a ordem de grandeza exigida e dispensando estruturas prontas complexas."),
        ("Kosaraju O(V + E):", " Duas passadas completas de DFS e uma inversão de arestas resultam em tempo estritamente linear.")
    ]
    for i, (bold, text) in enumerate(items5_1):
        p = tf5_1.paragraphs[0] if i == 0 else tf5_1.add_paragraph()
        p.space_after = Pt(10)
        r1 = p.add_run()
        r1.text = "• " + bold
        r1.font.bold = True
        r1.font.size = Pt(12)
        r1.font.color.rgb = NAVY
        r2 = p.add_run()
        r2.text = text
        r2.font.size = Pt(12)
        r2.font.color.rgb = DARK_TEXT

    add_card(slide5, Inches(6.9), Inches(1.8), Inches(5.6), Inches(5.0), "Engenharia de Software e Fundamentos")
    tb5_2 = slide5.shapes.add_textbox(Inches(7.1), Inches(2.5), Inches(5.2), Inches(4.0))
    tf5_2 = tb5_2.text_frame
    tf5_2.word_wrap = True

    items5_2 = [
        ("Premissa Gulosa do Dijkstra:", " Compreensão profunda de por que pesos negativos violam o relaxamento guloso definitivo, exigindo validação defensiva prévia."),
        ("Refatoração e Código Enxuto:", " Redução de 27% das linhas de código (523 linhas eliminadas) através de centralização de estilos e reutilização inteligente."),
        ("Conformidade Estrita de Saída:", " Formatação de espaços e hifens idêntica à especificação ('3 - 0 - 2...'), garantindo facilidade de correção automática."),
        ("Tratamento de Grafos Desconexos:", " Garantia de que buscas continuem em todas as componentes não alcançadas na primeira chamada.")
    ]
    for i, (bold, text) in enumerate(items5_2):
        p = tf5_2.paragraphs[0] if i == 0 else tf5_2.add_paragraph()
        p.space_after = Pt(10)
        r1 = p.add_run()
        r1.text = "• " + bold
        r1.font.bold = True
        r1.font.size = Pt(12)
        r1.font.color.rgb = BLUE
        r2 = p.add_run()
        r2.text = text
        r2.font.size = Pt(12)
        r2.font.color.rgb = DARK_TEXT

    # =========================================================================
    # SLIDE 6: CONTRIBUIÇÃO DA EQUIPE
    # =========================================================================
    slide6 = prs.slides.add_slide(blank_layout)
    add_header(slide6, "5. Divisão de Contribuição no Projeto")

    add_card(slide6, Inches(0.8), Inches(1.8), Inches(5.6), Inches(5.0), "Estruturas Fundamentais e Algoritmos de Busca", NAVY)
    tb6_1 = slide6.shapes.add_textbox(Inches(1.0), Inches(2.5), Inches(5.2), Inches(4.0))
    tf6_1 = tb6_1.text_frame
    tf6_1.word_wrap = True

    items6_1 = [
        ("Estruturas de Dados Manuais:", " Concepção e codificação da ListaAdjacencia ordenada, NoAdjacencia, FilaInt circular e PilhaInt estática."),
        ("Algoritmos de Travessia:", " Implementação dos algoritmos de Busca em Profundidade (DFS) e Busca em Largura (BFS) com suporte a grafos desconexos."),
        ("Ordenação Topológica:", " Implementação da detecção de ciclos por coloração de estados e geração da ordem topológica em DAGs."),
        ("Parser e Leitura I/O:", " Construção do LeitorGrafoTxt para validação de sintaxe, leitura de orientação, vértices e pesos.")
    ]
    for i, (bold, text) in enumerate(items6_1):
        p = tf6_1.paragraphs[0] if i == 0 else tf6_1.add_paragraph()
        p.space_after = Pt(10)
        r1 = p.add_run()
        r1.text = "• " + bold
        r1.font.bold = True
        r1.font.size = Pt(12)
        r1.font.color.rgb = NAVY
        r2 = p.add_run()
        r2.text = text
        r2.font.size = Pt(12)
        r2.font.color.rgb = DARK_TEXT

    add_card(slide6, Inches(6.9), Inches(1.8), Inches(5.6), Inches(5.0), "Algoritmos Avançados, Interface e Validação", BLUE)
    tb6_2 = slide6.shapes.add_textbox(Inches(7.1), Inches(2.5), Inches(5.2), Inches(4.0))
    tf6_2 = tb6_2.text_frame
    tf6_2.word_wrap = True

    items6_2 = [
        ("Union-Find e Kruskal:", " Codificação da estrutura de conjuntos disjuntos com rank e compressão de caminho; algoritmo da Árvore Geradora Mínima."),
        ("Kosaraju e Dijkstra:", " Implementação da inversão do grafo para CFCs e relaxamento de caminhos mínimos com rejeição de pesos negativos."),
        ("Interface Gráfica e Layout:", " Construção da JanelaPrincipal (Swing) e PainelVisualizacaoGrafo (JGraphX) com recozimento simulado e empacotamento."),
        ("Documentação e Testes:", " Elaboração do relatório técnico (README.doc / README.md), guia teórico de estudos e bateria de testes com as Figuras 1, 2 e 3.")
    ]
    for i, (bold, text) in enumerate(items6_2):
        p = tf6_2.paragraphs[0] if i == 0 else tf6_2.add_paragraph()
        p.space_after = Pt(10)
        r1 = p.add_run()
        r1.text = "• " + bold
        r1.font.bold = True
        r1.font.size = Pt(12)
        r1.font.color.rgb = BLUE
        r2 = p.add_run()
        r2.text = text
        r2.font.size = Pt(12)
        r2.font.color.rgb = DARK_TEXT

    # =========================================================================
    # SLIDE 7: ROTEIRO DA DEMONSTRAÇÃO PRÁTICA
    # =========================================================================
    slide7 = prs.slides.add_slide(blank_layout)
    add_header(slide7, "6. Demonstração Prática do Software (Ao Vivo)")

    add_card(slide7, Inches(0.8), Inches(1.8), Inches(11.733), Inches(5.0), "Roteiro de Demonstração em 1 Minuto (Grafo da Figura 2)", NAVY)
    tb7 = slide7.shapes.add_textbox(Inches(1.1), Inches(2.5), Inches(11.1), Inches(4.0))
    tf7 = tb7.text_frame
    tf7.word_wrap = True

    demo_steps = [
        ("Passo 1: Carregar Grafo", " Clicar em 'Carregar Grafo (.txt)'. O seletor abre automaticamente na pasta exemplos/. Selecionar fig2.txt (grafo não-orientado ponderado com 7 vértices e 11 arestas)."),
        ("Passo 2: Desenho Automático", " Observar o grafo desenhado instantaneamente na área central: vértices rotulados de 0 a 6, arestas ponderadas, nós distribuídos sem sobreposição via recozimento simulado."),
        ("Passo 3: Busca em Profundidade (DFS)", " Clicar em 'Busca em Profundidade (DFS)'. Informar a origem 3 na caixa de diálogo.\n   -> Resultado impresso no console e terminal: 3 - 0 - 2 - 4 - 1 - 5 - 6 (idêntico à página 6 do edital)."),
        ("Passo 4: Algoritmo de Kruskal (AGM)", " Clicar em 'Algoritmo de Kruskal (AGM)'. O software executa a ordenação e o Union-Find.\n   -> Saída: peso total: 21 | arestas: (4,5) (1,3) (2,4) (0,3) (4,6) (2,3)\n   -> Visualização: As 6 arestas da AGM são destacadas imediatamente em VERMELHO e traço grosso."),
        ("Passo 5 (Bônus): Kosaraju na Figura 1", " Carregar fig1.txt e clicar em 'Comp. Fortemente Conexas'. As CFCs {0,1,3} e {2,4} aparecem coloridas com cores diferentes na tela.")
    ]
    for i, (bold, text) in enumerate(demo_steps):
        p = tf7.paragraphs[0] if i == 0 else tf7.add_paragraph()
        p.space_after = Pt(8)
        r1 = p.add_run()
        r1.text = bold + ":"
        r1.font.bold = True
        r1.font.size = Pt(11.5)
        r1.font.color.rgb = BLUE
        r2 = p.add_run()
        r2.text = text
        r2.font.size = Pt(11.5)
        r2.font.color.rgb = DARK_TEXT

    # =========================================================================
    # SLIDE 8: CONCLUSÃO E ARGUIÇÃO
    # =========================================================================
    slide8 = prs.slides.add_slide(blank_layout)
    add_header(slide8, "Conclusão e Abertura para Perguntas da Banca")

    add_card(slide8, Inches(0.8), Inches(1.8), Inches(11.733), Inches(5.0), "Síntese dos Resultados Alcançados", NAVY)
    tb8 = slide8.shapes.add_textbox(Inches(1.1), Inches(2.6), Inches(11.1), Inches(4.0))
    tf8 = tb8.text_frame
    tf8.word_wrap = True

    concl_items = [
        ("Todos os 7 Algoritmos Operacionais:", " Desenho com layout orgânico, DFS, BFS, Ordenação Topológica, Kosaraju, Dijkstra e Kruskal validados com as Figuras 1, 2 e 3."),
        ("Estruturas de Dados 100% Próprias:", " Nenhuma coleção ou biblioteca pronta utilizada para os grafos ou algoritmos, respeitando o objetivo pedagógico."),
        ("Código Enxuto e Modular:", " Redução de 27% no volume de código, eliminando redundâncias e facilitando a manutenção."),
        ("Documentação Técnica Completa:", " Relatório README.doc e README.md com citação de código, tabela de funções em português e declaração de IA."),
        ("Respeito Rigoroso ao Limite de Tempo:", " Apresentação estruturada e cronometrada para concluir dentro do teto de 5 minutos.")
    ]
    for i, (bold, text) in enumerate(concl_items):
        p = tf8.paragraphs[0] if i == 0 else tf8.add_paragraph()
        p.space_after = Pt(12)
        r1 = p.add_run()
        r1.text = "✅ " + bold
        r1.font.bold = True
        r1.font.size = Pt(13)
        r1.font.color.rgb = GREEN
        r2 = p.add_run()
        r2.text = text
        r2.font.size = Pt(13)
        r2.font.color.rgb = DARK_TEXT

    p_final = tf8.add_paragraph()
    p_final.space_before = Pt(16)
    p_final.text = "Obrigado pela atenção dos Professores Rômulo e Newton Spolaôr!\nEstamos à disposição para as perguntas teóricas e práticas da banca."
    p_final.font.size = Pt(14)
    p_final.font.bold = True
    p_final.font.color.rgb = NAVY

    import os
    out_dir = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "apresentacao")
    os.makedirs(out_dir, exist_ok=True)
    out_path = os.path.join(out_dir, "apresentacao.pptx")
    prs.save(out_path)
    print(f"Apresentação PPTX gerada com sucesso em {out_path}")

if __name__ == '__main__':
    create_deck()
