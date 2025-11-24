import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.*;

/**
 * Uma visão gráfica da simulação com botões e eventos (GUI Swing),
 * utilizando componentes, layouts e classes internas anônimas,
 * exatamente como apresentado nos slides do Prof. Merschmann.
 */
public class VisaoSimulador extends JFrame
{
    private static final Color COR_VAZIO = Color.white;
    private static final Color COR_DESCONHECIDA = Color.gray;

    private final String PREFIXO_PASSO = "Passo: ";
    private final String PREFIXO_POPULACAO = "População: ";

    private JLabel rotuloPasso;
    private JLabel rotuloPopulacao;
    private VisaoCampo painelCampo;

    private HashMap<Class<?>, Color> cores;
    private EstatisticasCampo estatisticas;

    // Botões da interface
    private JButton btnLonga;
    private JButton btnIniciar;
    private JButton btnPausar;
    private JButton btnReiniciar;

    /**
     * Constrói a visão gráfica do simulador.
     */
    public VisaoSimulador(int altura, int largura)
    {
        estatisticas = new EstatisticasCampo();
        cores = new HashMap<>();

        setTitle("Simulação de Raposas, Coelhos, Lobos e Plantas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(200, 50);

        rotuloPasso = new JLabel(PREFIXO_PASSO + 0, JLabel.CENTER);
        rotuloPopulacao = new JLabel(PREFIXO_POPULACAO, JLabel.CENTER);

        painelCampo = new VisaoCampo(altura, largura);

        // -----------------------
        // BARRA DE BOTÕES (SOUTH)
        // -----------------------
        JPanel barraBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnLonga     = new JButton("▶ Simulação Longa");
        btnIniciar   = new JButton("► Iniciar");
        btnPausar    = new JButton("❚❚ Pausar");
        btnReiniciar = new JButton("↻ Reiniciar");

        barraBotoes.add(btnLonga);
        barraBotoes.add(btnIniciar);
        barraBotoes.add(btnPausar);
        barraBotoes.add(btnReiniciar);

        // -----------------------
        // Layout principal (BorderLayout)
        // -----------------------
        setLayout(new BorderLayout());
        add(rotuloPasso, BorderLayout.NORTH);
        add(painelCampo, BorderLayout.CENTER);

        JPanel painelSul = new JPanel(new BorderLayout());
        painelSul.add(rotuloPopulacao, BorderLayout.NORTH);
        painelSul.add(barraBotoes, BorderLayout.SOUTH);

        add(painelSul, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    /**
     * Registro dos eventos usando CLASSES INTERNAS ANÔNIMAS,
     * como ensinado nos slides.
     */
    public void registrarControles(Simulador simulador)
    {
        btnLonga.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulador.iniciarSimulacaoLonga();
            }
        });

        btnIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulador.iniciarSimulacaoAteMorrer();
            }
        });

        btnPausar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulador.pausar();
            }
        });

        btnReiniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulador.reiniciar();
            }
        });
    }

    /**
     * Define cor de uma espécie.
     */
    public void setCor(Class classe, Color cor)
    {
        cores.put(classe, cor);
    }

    private Color getCor(Class classe)
    {
        return cores.getOrDefault(classe, COR_DESCONHECIDA);
    }

    /**
     * Atualiza a interface com o novo estado do campo.
     */
    public void mostrarStatus(int passo, Campo campo)
    {
        rotuloPasso.setText(PREFIXO_PASSO + passo);

        estatisticas.resetar();
        painelCampo.prepararPintura();

        for (int lin = 0; lin < campo.getProfundidade(); lin++) {
            for (int col = 0; col < campo.getLargura(); col++) {

                Object obj = campo.getObjetoEm(lin, col);

                if (obj instanceof Ator ator) {
                    if (!ator.estaAtivo()) {
                        painelCampo.desenharMarca(col, lin, COR_VAZIO);
                        continue;
                    }
                    estatisticas.incrementarContagem(obj.getClass());
                    painelCampo.desenharMarca(col, lin, getCor(obj.getClass()));
                }
                else if (obj != null) {
                    painelCampo.desenharMarca(col, lin, getCor(obj.getClass()));
                }
                else {
                    painelCampo.desenharMarca(col, lin, COR_VAZIO);
                }
            }
        }

        estatisticas.contagemFinalizada();
        rotuloPopulacao.setText(PREFIXO_POPULACAO + estatisticas.obterDetalhesPopulacao(campo));

        painelCampo.repaint();
    }

    public boolean ehViavel(Campo campo)
    {
        return estatisticas.ehViavel(campo);
    }

    /**
     * Painel interno responsável por desenhar o grid da simulação.
     */
    private class VisaoCampo extends JPanel
    {
        private final int ESCALA = 6;
        private int altura, largura;

        private int escalaX, escalaY;
        private Dimension tamanho;
        private Image img;
        private Graphics g;

        public VisaoCampo(int altura, int largura)
        {
            this.altura = altura;
            this.largura = largura;
            tamanho = new Dimension(0, 0);
        }

        public Dimension getPreferredSize()
        {
            return new Dimension(largura * ESCALA, altura * ESCALA);
        }

        public void prepararPintura()
        {
            Dimension atual = getSize();
            if (img == null || !tamanho.equals(atual)) {
                tamanho = atual;
                img = createImage(tamanho.width, tamanho.height);
                g = img.getGraphics();

                escalaX = tamanho.width / largura;
                escalaY = tamanho.height / altura;
                if (escalaX < 1) escalaX = ESCALA;
                if (escalaY < 1) escalaY = ESCALA;
            }
        }

        public void desenharMarca(int x, int y, Color cor)
        {
            g.setColor(cor);
            g.fillRect(x * escalaX, y * escalaY, escalaX - 1, escalaY - 1);
        }

        public void paintComponent(Graphics g2)
        {
            super.paintComponent(g2);
            if (img != null) g2.drawImage(img, 0, 0, null);
        }
    }
}
