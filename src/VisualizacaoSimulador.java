import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.*;

/**
 * Uma visão gráfica da grade de simulação.
 * A visualização exibe um retângulo colorido para cada localização,
 * representando o seu conteúdo. Usa uma cor de fundo padrão.
 * As cores para cada tipo de espécie podem ser definidas
 * usando o método definirCor.
 * 
 * Adicionado: botões para controlar a simulação (Iniciar, Pausar, Reiniciar)
 * seguindo o estilo e estrutura originais do livro.
 * 
 * @author 
 * David J. Barnes e Michael Kolling / modificado por Raynner
 * @version 2002-04-23
 */
public class VisualizacaoSimulador extends JFrame {
    // Cores usadas para locais vazios.
    private static final Color COR_VAZIA = Color.white;

    // Cor usada para objetos que não têm uma cor definida.
    private static final Color COR_DESCONHECIDA = Color.gray;

    private final String PREFIXO_PASSO = "Passo: ";
    private final String PREFIXO_POPULACAO = "População: ";
    private JLabel rotuloPasso, rotuloPopulacao;
    private VisaoCampo visaoCampo;

    // Um mapa para armazenar as cores dos participantes da simulação.
    private HashMap<Class, Color> cores;
    // Um objeto de estatísticas que calcula e armazena informações da simulação.
    private EstatisticasCampo estatisticas;

    // Componentes de controle da simulação.
    private JButton botaoIniciar;
    private JButton botaoPausar;
    private JButton botaoReiniciar;
    private JButton botaoSimulacaoLonga;


    // Referência ao simulador e controle de execução em thread separada.
    private Simulador simulador;
    private boolean executando = false;
    private Thread threadSimulacao;

    /**
     * Cria uma visualização com a altura e largura fornecidas.
     */
    public VisualizacaoSimulador(int altura, int largura) {
        estatisticas = new EstatisticasCampo();
        cores = new HashMap<Class, Color>();

        setTitle("Simulação de Raposas, Coelhos, Lobos e Plantas");
        rotuloPasso = new JLabel(PREFIXO_PASSO, JLabel.CENTER);
        rotuloPopulacao = new JLabel(PREFIXO_POPULACAO, JLabel.CENTER);

        setLocation(100, 50);

        visaoCampo = new VisaoCampo(altura, largura);

        Container conteudo = getContentPane();
        conteudo.setLayout(new BorderLayout());
        conteudo.add(rotuloPasso, BorderLayout.NORTH);
        conteudo.add(visaoCampo, BorderLayout.CENTER);
        conteudo.add(rotuloPopulacao, BorderLayout.SOUTH);

        // Painel de botões adicionado na parte inferior.
        JPanel painelSul = new JPanel();
        painelSul.setLayout(new BorderLayout());

        rotuloPopulacao = new JLabel(PREFIXO_POPULACAO, JLabel.CENTER);
        painelSul.add(rotuloPopulacao, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout());
        botaoIniciar = new JButton("▶ Iniciar");
        botaoPausar = new JButton("⏸ Pausar");
        botaoReiniciar = new JButton("↺ Reiniciar");
        botaoSimulacaoLonga = new JButton("⏩ Simulação Longa");

        painelBotoes.add(botaoSimulacaoLonga);
        painelBotoes.add(botaoIniciar);
        painelBotoes.add(botaoPausar);
        painelBotoes.add(botaoReiniciar);

        painelSul.add(painelBotoes, BorderLayout.SOUTH);

        conteudo.add(painelSul, BorderLayout.SOUTH);

        // Configura os ouvintes dos botões usando classes internas anônimas.
        configurarEventos();

        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Define o simulador associado a esta visualização.
     * Isso permite que os botões controlem a simulação.
     */
    public void definirSimulador(Simulador simulador) {
        this.simulador = simulador;
    }

    /**
     * Configura os eventos de clique dos botões
     * utilizando classes internas anônimas (sem lambdas).
     */
    private void configurarEventos() {
        // Botão Iniciar: inicia a simulação em uma nova thread.
        botaoIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSimulacao();
            }
        });

        // Botão Pausar: interrompe o laço de simulação.
        botaoPausar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pausarSimulacao();
            }
        });

        // Botão Reiniciar: pausa e volta ao estado inicial.
        botaoReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarSimulacao();
            }
        });
        // Botão Simulação Longa
    botaoSimulacaoLonga.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (simulador != null && !executando) {
            executando = true;
            new Thread(new Runnable() {
                public void run() {
                    simulador.executarSimulacaoLonga();
                    executando = false; // liberação depois de terminar
                }
            }).start();
        }
    }
    });

    }

    /**
     * Inicia a simulação em uma nova thread, executando passo a passo
     * enquanto a simulação continuar viável.
     */
    private void iniciarSimulacao() {
        // Se não há simulador ou já está rodando, não faz nada.
        if (simulador == null || executando) {
            return;
        }

        executando = true;

        threadSimulacao = new Thread(new Runnable() {
            @Override
            public void run() {
                // Enquanto estiver executando e a simulação for viável, avança um passo.
                while (executando && simulador.eViavel()) {
                    simulador.simularUmPasso();
                    try {
                        Thread.sleep(100); // controle de velocidade da simulação
                    } catch (InterruptedException ex) {
                        // Thread interrompida: sai do laço.
                        break;
                    }
                }
            }
        });

        threadSimulacao.start();
    }

    /**
     * Pausa a execução da simulação.
     */
    private void pausarSimulacao() {
        executando = false;
    }

    /**
     * Reinicia a simulação para o estado inicial.
     */
    private void reiniciarSimulacao() {
        // Garante que o laço de execução seja encerrado.
        pausarSimulacao();
        if (simulador != null) {
            simulador.reiniciar();
        }
    }

    /**
     * Define uma cor a ser usada para uma classe específica de animal.
     * @param classeAnimal A classe do animal.
     * @param cor A cor a ser usada para representar esse animal.
     */
    public void definirCor(Class classeAnimal, Color cor) {
        cores.put(classeAnimal, cor);
    }

    /**
     * Retorna a cor usada para uma determinada classe de animal.
     * @param classeAnimal A classe do animal.
     * @return A cor definida, ou uma cor padrão se nenhuma for definida.
     */
    private Color getCor(Class classeAnimal) {
        Color cor = (Color) cores.get(classeAnimal);
        if (cor == null) {
            // Nenhuma cor definida para esta classe.
            return COR_DESCONHECIDA;
        } else {
            return cor;
        }
    }

    /**
     * Mostra o estado atual do campo.
     * @param passo O número do passo atual da iteração.
     * @param campo O campo cuja situação será representada.
     */
    public void mostrarStatus(int passo, Campo campo) {
        if (!isVisible()) {
            setVisible(true);
        }

        rotuloPasso.setText(PREFIXO_PASSO + passo);
         

        rotuloPopulacao.setText(PREFIXO_POPULACAO + estatisticas.getDetalhesPopulacao(campo));

        estatisticas.reiniciar();
        visaoCampo.prepararPintura();

        for (int linha = 0; linha < campo.getProfundidade(); linha++) {
            for (int coluna = 0; coluna < campo.getLargura(); coluna++) {
                // Usa diretamente a posição (linha, coluna), como no livro.
                Object animal = campo.getObjetoEm(linha, coluna);
                if (animal != null) {
                    estatisticas.incrementarContagem(animal.getClass());
                    visaoCampo.desenharMarca(coluna, linha, getCor(animal.getClass()));
                } else {
                    visaoCampo.desenharMarca(coluna, linha, COR_VAZIA);
                }
            }
        }
        estatisticas.contagemFinalizada();

        rotuloPopulacao.setText(PREFIXO_POPULACAO + estatisticas.getDetalhesPopulacao(campo));
        visaoCampo.repaint();
    }

    /**
     * Determina se a simulação deve continuar rodando.
     * @return true se houver mais de uma espécie viva.
     */
    public boolean eViavel(Campo campo) {
        return estatisticas.eViavel(campo);
    }

    /**
     * Fornece uma visão gráfica de um campo retangular. Esta é
     * uma classe interna (uma classe definida dentro de outra),
     * que define um componente personalizado para a interface gráfica.
     * Esse componente exibe o campo.
     * Isso é um recurso avançado de GUI — pode ser ignorado
     * se preferir focar apenas na lógica da simulação.
     */
    private class VisaoCampo extends JPanel {
        private final int FATOR_ESCALA_GRADE = 6;

        private int larguraGrade, alturaGrade;
        private int escalaX, escalaY;
        Dimension tamanho;
        private Graphics g;
        private Image imagemCampo;

        /**
         * Cria um novo componente VisaoCampo.
         */
        public VisaoCampo(int altura, int largura) {
            alturaGrade = altura;
            larguraGrade = largura;
            tamanho = new Dimension(0, 0);
        }

        /**
         * Informa ao gerenciador de layout o tamanho preferido deste componente.
         */
        public Dimension getPreferredSize() {
            return new Dimension(larguraGrade * FATOR_ESCALA_GRADE,
                                 alturaGrade * FATOR_ESCALA_GRADE);
        }

        /**
         * Prepara o componente para uma nova rodada de pintura.
         * Como o componente pode ser redimensionado, o fator de escala
         * é recalculado.
         */
        public void prepararPintura() {
            if (!tamanho.equals(getSize())) {  // se o tamanho mudou...
                tamanho = getSize();
                imagemCampo = createImage(tamanho.width, tamanho.height);
                g = imagemCampo.getGraphics();

                escalaX = tamanho.width / larguraGrade;
                if (escalaX < 1) {
                    escalaX = FATOR_ESCALA_GRADE;
                }
                escalaY = tamanho.height / alturaGrade;
                if (escalaY < 1) {
                    escalaY = FATOR_ESCALA_GRADE;
                }
            }
        }

        /**
         * Pinta uma célula na grade com a cor fornecida.
         * @param x A posição horizontal.
         * @param y A posição vertical.
         * @param cor A cor usada para pintar.
         */
        public void desenharMarca(int x, int y, Color cor) {
            g.setColor(cor);
            g.fillRect(x * escalaX, y * escalaY, escalaX - 1, escalaY - 1);
        }

        /**
         * O componente precisa ser redesenhado. Copia a imagem
         * interna para a tela.
         */
        public void paintComponent(Graphics g) {
            if (imagemCampo != null) {
                g.drawImage(imagemCampo, 0, 0, null);
            }
        }
    }
}
