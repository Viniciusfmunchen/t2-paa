import os
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN
from pptx.enum.shapes import MSO_SHAPE

def build_presentation():
    prs = Presentation()
    prs.slide_width = Inches(13.333)
    prs.slide_height = Inches(7.5)
    blank_layout = prs.slide_layouts[6]

    # Paleta limpa e minimalista
    BG_COLOR = RGBColor(247, 247, 247)      # #F7F7F7
    TEXT_DARK = RGBColor(38, 38, 38)        # #262626
    TEXT_MUTED = RGBColor(115, 115, 115)    # #737373
    LINE_COLOR = RGBColor(210, 210, 210)    # #D2D2D2
    CARD_BG = RGBColor(255, 255, 255)       # #FFFFFF
    CARD_BORDER = RGBColor(225, 225, 225)   # #E1E1E1
    ACCENT_BLUE = RGBColor(37, 99, 235)     # #2563EB
    ACCENT_GREEN = RGBColor(22, 101, 52)    # #166534

    def apply_background(slide):
        bg = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(7.5))
        bg.fill.solid()
        bg.fill.fore_color.rgb = BG_COLOR
        bg.line.fill.background()
        return bg

    def add_top_header(slide):
        # Header text: Unioeste | Algoritmos em Grafos - GraphLab PAA | Vinicius F. Munchen
        tb_l = slide.shapes.add_textbox(Inches(0.8), Inches(0.4), Inches(3.0), Inches(0.4))
        p_l = tb_l.text_frame.paragraphs[0]
        p_l.text = "Unioeste"
        p_l.font.size = Pt(11)
        p_l.font.color.rgb = TEXT_MUTED
        p_l.font.name = 'Arial'

        tb_c = slide.shapes.add_textbox(Inches(3.8), Inches(0.4), Inches(5.7), Inches(0.4))
        p_c = tb_c.text_frame.paragraphs[0]
        p_c.alignment = PP_ALIGN.CENTER
        p_c.text = "Algoritmos em Grafos - GraphLab PAA"
        p_c.font.size = Pt(11)
        p_c.font.color.rgb = TEXT_MUTED
        p_c.font.name = 'Arial'

        tb_r = slide.shapes.add_textbox(Inches(9.5), Inches(0.4), Inches(3.0), Inches(0.4))
        p_r = tb_r.text_frame.paragraphs[0]
        p_r.alignment = PP_ALIGN.RIGHT
        p_r.text = "Vinicius F. Munchen"
        p_r.font.size = Pt(11)
        p_r.font.color.rgb = TEXT_MUTED
        p_r.font.name = 'Arial'

        line = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0.8), Inches(0.82), Inches(11.733), Inches(0.015))
        line.fill.solid()
        line.fill.fore_color.rgb = LINE_COLOR
        line.line.fill.background()

    def add_slide_title(slide, title_text):
        tb = slide.shapes.add_textbox(Inches(0.8), Inches(1.2), Inches(11.733), Inches(0.8))
        tf = tb.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = title_text
        p.font.size = Pt(28)
        p.font.bold = True
        p.font.color.rgb = TEXT_DARK
        p.font.name = 'Arial'

    def create_card(slide, left, top, width, height, title=None, title_color=TEXT_DARK):
        card = slide.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, left, top, width, height)
        card.fill.solid()
        card.fill.fore_color.rgb = CARD_BG
        card.line.color.rgb = CARD_BORDER
        card.line.width = Pt(1)

        if title:
            tb = slide.shapes.add_textbox(left + Inches(0.25), top + Inches(0.2), width - Inches(0.5), Inches(0.4))
            tf = tb.text_frame
            tf.word_wrap = True
            p = tf.paragraphs[0]
            p.text = title
            p.font.size = Pt(14)
            p.font.bold = True
            p.font.color.rgb = title_color
            p.font.name = 'Arial'
        return card

    # =========================================================================
    # SLIDE 1: CAPA
    # =========================================================================
    s1 = prs.slides.add_slide(blank_layout)
    apply_background(s1)
    add_top_header(s1)

    c1 = create_card(s1, Inches(0.8), Inches(1.3), Inches(11.733), Inches(5.6))
    tb1 = s1.shapes.add_textbox(Inches(1.2), Inches(1.7), Inches(10.9), Inches(4.8))
    tf1 = tb1.text_frame
    tf1.word_wrap = True

    p0 = tf1.paragraphs[0]
    p0.text = "UNIVERSIDADE ESTADUAL DO OESTE DO PARANÁ"
    p0.font.size = Pt(12)
    p0.font.bold = True
    p0.font.color.rgb = TEXT_MUTED
    p0.font.name = 'Arial'

    p1 = tf1.add_paragraph()
    p1.text = "Ciência da Computação • Disciplina de Projeto e Análise de Algoritmos"
    p1.font.size = Pt(12)
    p1.font.color.rgb = TEXT_MUTED
    p1.font.name = 'Arial'
    p1.space_after = Pt(26)

    p2 = tf1.add_paragraph()
    p2.text = "GraphLab PAA"
    p2.font.size = Pt(40)
    p2.font.bold = True
    p2.font.color.rgb = TEXT_DARK
    p2.font.name = 'Arial'

    p3 = tf1.add_paragraph()
    p3.text = "Implementação e Demonstração dos 7 Algoritmos em Grafos"
    p3.font.size = Pt(20)
    p3.font.color.rgb = ACCENT_BLUE
    p3.font.name = 'Arial'
    p3.space_after = Pt(36)

    p4 = tf1.add_paragraph()
    p4.text = "Professores: Prof. Dr. Rômulo & Prof. Dr. Newton Spolaôr\nAluno: Vinicius Fontana Munchen\nTempo de Apresentação: 5 minutos"
    p4.font.size = Pt(13)
    p4.font.color.rgb = TEXT_DARK
    p4.font.name = 'Arial'

    # =========================================================================
    # SLIDE 2: SUMÁRIO
    # =========================================================================
    s2 = prs.slides.add_slide(blank_layout)
    apply_background(s2)
    add_top_header(s2)
    add_slide_title(s2, "Sumário")

    agenda_items = [
        ("1", "Visão Geral e Arquitetura", 0, 0),
        ("2", "Estruturas de Dados", 0, 1),
        ("3", "Aprendizado e Desafios", 0, 2),
        ("4", "Demonstração", 1, 0),
        ("5", "Conclusão", 1, 1),
        ("6", "Referências", 1, 2)
    ]
    col_xs = [Inches(0.8), Inches(6.8)]
    row_ys = [Inches(2.5), Inches(3.8), Inches(5.1)]

    for num, label, col_idx, row_idx in agenda_items:
        x_base = col_xs[col_idx]
        y_base = row_ys[row_idx]

        num_box = s2.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, x_base, y_base, Inches(0.7), Inches(0.7))
        num_box.fill.solid()
        num_box.fill.fore_color.rgb = CARD_BG
        num_box.line.color.rgb = LINE_COLOR
        num_box.line.width = Pt(1.5)
        tf_num = num_box.text_frame
        tf_num.word_wrap = False
        p_num = tf_num.paragraphs[0]
        p_num.alignment = PP_ALIGN.CENTER
        p_num.text = num
        p_num.font.size = Pt(18)
        p_num.font.bold = True
        p_num.font.color.rgb = TEXT_DARK
        p_num.font.name = 'Arial'

        tb_lbl = s2.shapes.add_textbox(x_base + Inches(0.9), y_base + Inches(0.1), Inches(4.8), Inches(0.6))
        tf_lbl = tb_lbl.text_frame
        tf_lbl.word_wrap = True
        p_lbl = tf_lbl.paragraphs[0]
        p_lbl.text = label
        p_lbl.font.size = Pt(18)
        p_lbl.font.color.rgb = TEXT_DARK
        p_lbl.font.name = 'Arial'

    # =========================================================================
    # SLIDE 3: 1. VISÃO GERAL E ARQUITETURA
    # =========================================================================
    s3 = prs.slides.add_slide(blank_layout)
    apply_background(s3)
    add_top_header(s3)
    add_slide_title(s3, "1. Visão Geral e Arquitetura")

    create_card(s3, Inches(0.8), Inches(2.1), Inches(5.6), Inches(4.8), "O que o software faz")
    tb3_1 = s3.shapes.add_textbox(Inches(1.05), Inches(2.8), Inches(5.1), Inches(3.9))
    tf3_1 = tb3_1.text_frame
    tf3_1.word_wrap = True

    pts3_1 = [
        ("Leitura de Arquivo .txt:", " Lê arquivos com a sintaxe oficial (orientado=sim/nao, V=n e arestas (u,v):peso)."),
        ("Desenho Gráfico na Tela:", " Renderiza o grafo visualmente com nós rotulados e pesos nas linhas usando JGraphX."),
        ("Execução dos 7 Algoritmos:", " Permite rodar DFS, BFS, Ordenação Topológica, Kosaraju, Dijkstra e Kruskal sobre o mesmo grafo carregado."),
        ("Saída Dupla:", " Mostra o resultado na tela da interface e imprime no terminal ao mesmo tempo, com título em cada algoritmo.")
    ]
    for i, (b, t) in enumerate(pts3_1):
        p = tf3_1.paragraphs[0] if i == 0 else tf3_1.add_paragraph()
        p.space_after = Pt(14)
        r1 = p.add_run()
        r1.text = "• " + b
        r1.font.bold = True
        r1.font.size = Pt(12)
        r1.font.color.rgb = TEXT_DARK
        r2 = p.add_run()
        r2.text = t
        r2.font.size = Pt(12)
        r2.font.color.rgb = TEXT_MUTED

    create_card(s3, Inches(6.9), Inches(2.1), Inches(5.6), Inches(4.8), "Como organizamos o código")
    tb3_2 = s3.shapes.add_textbox(Inches(7.15), Inches(2.8), Inches(5.1), Inches(3.9))
    tf3_2 = tb3_2.text_frame
    tf3_2.word_wrap = True

    pts3_2 = [
        ("Tudo em Java Puro:", " Feito em Java 21, sem frameworks pesados, rodando em qualquer sistema operacional."),
        ("Divisão em Pastas:", " Separamos o código para não virar bagunça: structures (estruturas), algorithms (algoritmos), model (grafo) e ui (telas)."),
        ("Sem Bibliotecas Prontas:", " Respeitamos a regra do edital: nenhuma estrutura pronta (como ArrayList ou LinkedList) foi usada nos grafos."),
        ("Scripts de 1 Clique:", " Criamos scripts prontos (.bat para Windows e .sh para Linux/Mac) para compilar e rodar direto.")
    ]
    for i, (b, t) in enumerate(pts3_2):
        p = tf3_2.paragraphs[0] if i == 0 else tf3_2.add_paragraph()
        p.space_after = Pt(14)
        r1 = p.add_run()
        r1.text = "• " + b
        r1.font.bold = True
        r1.font.size = Pt(12)
        r1.font.color.rgb = ACCENT_BLUE
        r2 = p.add_run()
        r2.text = t
        r2.font.size = Pt(12)
        r2.font.color.rgb = TEXT_MUTED

    # =========================================================================
    # SLIDE 4: 2. ESTRUTURAS DE DADOS PRÓPRIAS
    # =========================================================================
    s4 = prs.slides.add_slide(blank_layout)
    apply_background(s4)
    add_top_header(s4)
    add_slide_title(s4, "2. Estruturas de Dados Feitas na Mão")

    structs = [
        (Inches(0.8), Inches(2.1), Inches(5.6), Inches(2.3),
         "Lista de Adjacência Ordenada",
         "Lista simplesmente encadeada (nó aponta pro próximo).\n"
         "• Por que fizemos assim: O edital exigia que os vizinhos ficassem em ordem crescente (0, 1, 2...).\n"
         "• Como funciona: Quando lê uma aresta, já encaixa ela na posição certa por ponteiros, sem precisar usar sort depois."),
        
        (Inches(6.9), Inches(2.1), Inches(5.6), Inches(2.3),
         "Fila Simples (FilaInt)",
         "Fila normal feita com vetor e tamanho fixo.\n"
         "• Onde é usada: Na Busca em Largura (BFS).\n"
         "• Como funciona: O primeiro vértice que entra na fila é o primeiro a ser tirado e explorado (FIFO)."),
        
        (Inches(0.8), Inches(4.6), Inches(5.6), Inches(2.3),
         "Pilha Simples (PilhaInt)",
         "Pilha feita com vetor e controle de topo.\n"
         "• Onde é usada: Na Ordenação Topológica e no Kosaraju.\n"
         "• Como funciona: O último que entra é o primeiro a sair. Guarda quem terminou de ser visitado por último."),
        
        (Inches(6.9), Inches(4.6), Inches(5.6), Inches(2.3),
         "Union-Find (Grupos de Vértices)",
         "Estrutura para controlar grupos separados de nós.\n"
         "• Onde é usada: No algoritmo de Kruskal (AGM).\n"
         "• Como funciona: Diz na hora se dois vértices já estão conectados para não colocar aresta repetida e não criar ciclo.")
    ]
    for left, top, w, h, title, desc in structs:
        create_card(s4, left, top, w, h, title, ACCENT_BLUE)
        tb_s = s4.shapes.add_textbox(left + Inches(0.25), top + Inches(0.65), w - Inches(0.5), h - Inches(0.75))
        tf_s = tb_s.text_frame
        tf_s.word_wrap = True
        p_s = tf_s.paragraphs[0]
        p_s.text = desc
        p_s.font.size = Pt(11)
        p_s.font.color.rgb = TEXT_DARK
        p_s.font.name = 'Arial'

    # =========================================================================
    # SLIDE 5: 3. APRENDIZADO E DESAFIOS (PARTE 1: O QUE DEU TRABALHO)
    # =========================================================================
    s5 = prs.slides.add_slide(blank_layout)
    apply_background(s5)
    add_top_header(s5)
    add_slide_title(s5, "3. Desafios que Enfrentamos Programando")

    desafios_list = [
        ("1. Inserir em ordem na lista encadeada",
         "Não dava pra simplesmente jogar a aresta no final e dar sort(). Tivemos que fazer o algoritmo andar nó por nó comparando os números e acertar os ponteiros para não perder o resto da lista."),
        
        ("2. Fazer o leitor de texto não quebrar",
         "O arquivo de entrada mistura parênteses, vírgula, dois pontos e pesos negativos (ex: (0,2):-4). Tivemos que tratar os caracteres com cuidado para pegar os números certos sem dar erro de conversão."),
        
        ("3. Não deixar a Ordenação Topológica travar em loop",
         "Se o grafo tiver um ciclo (A aponta pra B e B aponta pra A), a ordenação topológica não existe. Tivemos que programar uma checagem que marca nós em visitação para detectar o ciclo e avisar o usuário com mensagem amigável."),
        
        ("4. Montar o grafo invertido no Kosaraju",
         "Para achar as componentes fortemente conexas, o algoritmo precisa de um segundo grafo com todas as flechas ao contrário. Criar esse grafo transposto na memória foi um dos passos mais trabalhosos.")
    ]
    for i, (t, d) in enumerate(desafios_list):
        top_pos = Inches(2.1 + i * 1.22)
        create_card(s5, Inches(0.8), top_pos, Inches(11.733), Inches(1.08), t, TEXT_DARK)
        tb_d = s5.shapes.add_textbox(Inches(1.05), top_pos + Inches(0.38), Inches(11.2), Inches(0.6))
        tf_d = tb_d.text_frame
        tf_d.word_wrap = True
        p_d = tf_d.paragraphs[0]
        p_d.text = d
        p_d.font.size = Pt(11)
        p_d.font.color.rgb = TEXT_MUTED
        p_d.font.name = 'Arial'

    # =========================================================================
    # SLIDE 6: 3. APRENDIZADO E DESAFIOS (PARTE 2: O QUE A GENTE APRENDEU)
    # =========================================================================
    s6 = prs.slides.add_slide(blank_layout)
    apply_background(s6)
    add_top_header(s6)
    add_slide_title(s6, "3. O que Aprendemos na Prática")

    create_card(s6, Inches(0.8), Inches(2.1), Inches(5.6), Inches(4.8), "Entendendo os Algoritmos na Realidade")
    tb6_1 = s6.shapes.add_textbox(Inches(1.05), Inches(2.8), Inches(5.1), Inches(3.9))
    tf6_1 = tb6_1.text_frame
    tf6_1.word_wrap = True

    pts6_1 = [
        ("Ver o algoritmo na tela ajuda muito:", " Olhar a busca visitando os nós e o Kruskal pintando a árvore de vermelho fez a teoria da lousa finalmente fazer sentido prático."),
        ("Por que o Dijkstra rejeita peso negativo:", " Entendemos na prática que como o Dijkstra escolhe o menor caminho e 'dá por encerrado', uma aresta negativa lá na frente estragaria a conta. Por isso colocamos a checagem que avisa o usuário."),
        ("Por que a lista é melhor que matriz:", " Em grafos com poucas arestas, a matriz gasta um monte de espaço vazio. A lista encadeada só guarda quem realmente é vizinho.")
    ]
    for i, (b, t) in enumerate(pts6_1):
        p = tf6_1.paragraphs[0] if i == 0 else tf6_1.add_paragraph()
        p.space_after = Pt(14)
        r1 = p.add_run()
        r1.text = "• " + b
        r1.font.bold = True
        r1.font.size = Pt(12)
        r1.font.color.rgb = TEXT_DARK
        r2 = p.add_run()
        r2.text = t
        r2.font.size = Pt(12)
        r2.font.color.rgb = TEXT_MUTED

    create_card(s6, Inches(6.9), Inches(2.1), Inches(5.6), Inches(4.8), "Aprendizados de Programação")
    tb6_2 = s6.shapes.add_textbox(Inches(7.15), Inches(2.8), Inches(5.1), Inches(3.9))
    tf6_2 = tb6_2.text_frame
    tf6_2.word_wrap = True

    pts6_2 = [
        ("Código organizado salva tempo:", " Separar em pacotes pequenos fez com que achar erros fosse muito mais rápido do que se tivesse tudo em um arquivo só."),
        ("Tratar entradas antes de rodar:", " Validar se o vértice existe (ex: de 0 a V-1) e se o arquivo está no formato certo evita que a aplicação feche do nada."),
        ("Limpeza de código redundante:", " Refatoramos o código para tirar repetições de formatação e de estilo, deixando tudo mais enxuto e fácil de explicar no seminário.")
    ]
    for i, (b, t) in enumerate(pts6_2):
        p = tf6_2.paragraphs[0] if i == 0 else tf6_2.add_paragraph()
        p.space_after = Pt(14)
        r1 = p.add_run()
        r1.text = "• " + b
        r1.font.bold = True
        r1.font.size = Pt(12)
        r1.font.color.rgb = ACCENT_BLUE
        r2 = p.add_run()
        r2.text = t
        r2.font.size = Pt(12)
        r2.font.color.rgb = TEXT_MUTED

    # =========================================================================
    # SLIDE 7: 4. DEMONSTRAÇÃO PRÁTICA (AO VIVO)
    # =========================================================================
    s7 = prs.slides.add_slide(blank_layout)
    apply_background(s7)
    add_top_header(s7)
    add_slide_title(s7, "4. Demonstração Prática (Ao Vivo)")

    create_card(s7, Inches(0.8), Inches(2.1), Inches(11.733), Inches(4.8), "Roteiro da Demonstração (Grafo da Figura 2 do Edital)")
    tb7 = s7.shapes.add_textbox(Inches(1.1), Inches(2.75), Inches(11.1), Inches(3.9))
    tf7 = tb7.text_frame
    tf7.word_wrap = True

    demo_steps = [
        ("1. Carregar o arquivo:", " Clicar em 'Carregar Grafo (.txt)' e abrir fig2.txt (pasta exemplos/). O sistema lê os 7 vértices e 11 arestas."),
        ("2. Ver o grafo desenhado:", " Mostrar o grafo desenhado na tela com os números dos nós e os pesos nas linhas, sem nós amontoados."),
        ("3. Rodar a Busca em Profundidade (DFS):", " Escolher a origem 3.\n   -> Resultado na tela: 3 - 0 - 2 - 4 - 1 - 5 - 6 (igualzinho ao exemplo da página 6 do edital)."),
        ("4. Rodar o Kruskal (AGM):", " Clicar no botão do Kruskal.\n   -> Resultado: peso total = 21.\n   -> Na tela: As 6 arestas escolhidas ficam vermelhas e com traço grosso."),
        ("5. Se der tempo (Kosaraju):", " Carregar fig1.txt e mostrar as componentes fortemente conexas pintadas de cores diferentes.")
    ]
    for i, (b, t) in enumerate(demo_steps):
        p = tf7.paragraphs[0] if i == 0 else tf7.add_paragraph()
        p.space_after = Pt(12)
        r1 = p.add_run()
        r1.text = b
        r1.font.bold = True
        r1.font.size = Pt(12)
        r1.font.color.rgb = ACCENT_BLUE
        r2 = p.add_run()
        r2.text = t
        r2.font.size = Pt(12)
        r2.font.color.rgb = TEXT_DARK

    # =========================================================================
    # SLIDE 8: 5. CONCLUSÃO
    # =========================================================================
    s8 = prs.slides.add_slide(blank_layout)
    apply_background(s8)
    add_top_header(s8)
    add_slide_title(s8, "5. Conclusão")

    create_card(s8, Inches(0.8), Inches(2.1), Inches(11.733), Inches(4.8), "O que foi entregue no trabalho")
    tb8 = s8.shapes.add_textbox(Inches(1.1), Inches(2.8), Inches(11.1), Inches(3.9))
    tf8 = tb8.text_frame
    tf8.word_wrap = True

    concl_items = [
        ("Todos os 7 algoritmos funcionando:", " Desenho do grafo, DFS, BFS, Ordenação Topológica, Kosaraju, Dijkstra e Kruskal testados com os arquivos do edital."),
        ("Estruturas feitas 100% na mão:", " Nenhuma lista ou fila pronta do Java foi usada, cumprindo o objetivo da matéria."),
        ("Programa fácil de usar:", " Interface limpa com botões, mensagens claras de erro quando o grafo não permite o algoritmo, e saídas formatadas com hifens."),
        ("Apresentação no tempo certo:", " Todo o roteiro planejado para caber com folga dentro dos 5 minutos pedidos.")
    ]
    for i, (b, t) in enumerate(concl_items):
        p = tf8.paragraphs[0] if i == 0 else tf8.add_paragraph()
        p.space_after = Pt(14)
        r1 = p.add_run()
        r1.text = "✅ " + b
        r1.font.bold = True
        r1.font.size = Pt(13)
        r1.font.color.rgb = ACCENT_GREEN
        r2 = p.add_run()
        r2.text = t
        r2.font.size = Pt(13)
        r2.font.color.rgb = TEXT_DARK

    p_agradece = tf8.add_paragraph()
    p_agradece.space_before = Pt(22)
    p_agradece.text = "Obrigado aos professores Rômulo e Newton pela paciência e pelas aulas!\nEstou à disposição para responder às perguntas sobre o código."
    p_agradece.font.size = Pt(14)
    p_agradece.font.bold = True
    p_agradece.font.color.rgb = TEXT_DARK

    # =========================================================================
    # SLIDE 9: 6. REFERÊNCIAS
    # =========================================================================
    s9 = prs.slides.add_slide(blank_layout)
    apply_background(s9)
    add_top_header(s9)
    add_slide_title(s9, "6. Referências")

    create_card(s9, Inches(0.8), Inches(2.1), Inches(11.733), Inches(4.8), "Materiais Consultados")
    tb9 = s9.shapes.add_textbox(Inches(1.1), Inches(2.8), Inches(11.1), Inches(3.9))
    tf9 = tb9.text_frame
    tf9.word_wrap = True

    refs = [
        ("Livros da disciplina:", " CORMEN, T. H. et al. Algoritmos: Teoria e Prática (capítulos de grafos, busca e caminhos mínimos).\nSZWARCFITER, J. L. Grafos e Algoritmos Computacionais."),
        ("Biblioteca de desenho:", " JGraphX (biblioteca livre em Java para renderização de grafos em Swing)."),
        ("Declaração sobre IA (Edital pág. 1):", " Usamos ferramentas de IA como apoio para tirar dúvidas de código, ajudar a limpar repetições e estruturar o relatório. Todo o funcionamento, as classes de dados e os testes foram revisados e testados por mim.")
    ]
    for i, (b, t) in enumerate(refs):
        p = tf9.paragraphs[0] if i == 0 else tf9.add_paragraph()
        p.space_after = Pt(16)
        r1 = p.add_run()
        r1.text = "• " + b
        r1.font.bold = True
        r1.font.size = Pt(12)
        r1.font.color.rgb = TEXT_DARK
        r2 = p.add_run()
        r2.text = t
        r2.font.size = Pt(12)
        r2.font.color.rgb = TEXT_MUTED

    out_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "apresentacao", "apresentacao.pptx")
    prs.save(out_path)
    print(f"✅ Slides simples e objetivos gerados em: {out_path}")

if __name__ == '__main__':
    build_presentation()
