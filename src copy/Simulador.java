import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Um simulador simples de predador-presa, baseado em um campo contendo
 * coelhos e raposas.
 *
 * @author David J. Barnes e Michael Kolling (Traduzido por IA)
 * @version 2002-04-09
 */
public class Simulador
{
    // As variáveis estáticas finais privadas representam
    // informações de configuração para a simulação.
    private static final int LARGURA_PADRAO = 50;
    private static final int PROFUNDIDADE_PADRAO = 50;

    // probabilidade criação dos atores do simulador
    private static final double PROB_CRIACAO_RAPOSA = 0.02;
    private static final double PROB_CRIACAO_COELHO = 0.08;
    private static final double PROB_CRIACAO_LOBO   = 0.01;
    private static final double PROB_CRIACAO_PLANTA = 0.10;

    // Random único do simulador (evita criar vários objetos Random)
    private static final Random rand = new Random();

    // A lista de atores no campo
    private List<Ator> animais;
    // A lista de atores recém-criados
    private List<Ator> novosAnimais;
    // O estado atual do campo.
    private Campo campo;
    // Um segundo campo, usado para construir a próxima etapa da simulação.
    private Campo campoAtualizado;
    // O passo atual da simulação.
    private int passo;
    // Uma visão gráfica da simulação.
    private VisaoSimulador visao;

    // ===== Controle de simulação contínua =====
    private Timer timer;
    // -1 = até morrer, >0 = simulação longa, 0 = parado
    private int passosRestantes;

    /**
     * Constrói um campo de simulação com tamanho padrão.
     */
    public Simulador()
    {
        this(PROFUNDIDADE_PADRAO, LARGURA_PADRAO);
    }

    /**
     * Cria um campo de simulação com o tamanho dado.
     * @param profundidade Profundidade do campo. Deve ser maior que zero.
     * @param largura Largura do campo. Deve ser maior que zero.
     */
    public Simulador(int profundidade, int largura)
    {
        if(largura <= 0 || profundidade <= 0) {
            System.out.println("As dimensões devem ser maiores que zero.");
            System.out.println("Usando valores padrão.");
            profundidade = PROFUNDIDADE_PADRAO;
            largura = LARGURA_PADRAO;
        }

        animais = new ArrayList<>();
        novosAnimais = new ArrayList<>();

        campo = new Campo(profundidade, largura);
        campoAtualizado = new Campo(profundidade, largura);

        visao = new VisaoSimulador(profundidade, largura);
        visao.setCor(Raposa.class, Color.blue);
        visao.setCor(Coelho.class, Color.orange);
        visao.setCor(Lobo.class, Color.red);
        visao.setCor(Planta.class, Color.green);

        // registra botões da GUI usando listeners anônimos
        visao.registrarControles(this);

        // Configura um ponto de partida válido.
        resetar();
    }

    /**
     * Executa a simulação a partir do seu estado atual por um período razoavelmente longo,
     * por exemplo, 500 passos.
     */
    public void executarSimulacaoLonga()
    {
        simular(500);
    }

    /**
     * Executa a simulação a partir do seu estado atual pelo número dado de passos.
     * Para antes do número dado de passos se deixar de ser viável.
     */
    public void simular(int numPassos)
    {
        for(int i = 1; i <= numPassos && visao.ehViavel(campo); i++) {
            simularUmPasso();
        }
    }

    /**
     * Executa a simulação a partir do seu estado atual por um único passo.
     */
    public void simularUmPasso()
    {
        passo++;
        novosAnimais.clear();

        // permite que todos os atores ajam usando polimorfismo
        for(Iterator<Ator> iter = animais.iterator(); iter.hasNext(); ) {
            Ator ator = iter.next();

            if(ator.estaAtivo()) {
                ator.agir(campo, campoAtualizado, novosAnimais);
            }

            if(!ator.estaAtivo()) {
                iter.remove();  // remove qualquer ator morto/inativo
            }
        }

        animais.addAll(novosAnimais);

        Campo temp = campo;
        campo = campoAtualizado;
        campoAtualizado = temp;
        campoAtualizado.limpar();

        visao.mostrarStatus(passo, campo);
    }

    /**
     * Reseta a simulação para uma posição inicial.
     */
    public void resetar()
    {
        passo = 0;
        animais.clear();
        campo.limpar();
        campoAtualizado.limpar();
        popular(campo);

        visao.mostrarStatus(passo, campo);
    }

    /**
     * Popula o campo com raposas, coelhos, lobos e plantas.
     */
    private void popular(Campo campo)
    {
        campo.limpar();

        for(int linha = 0; linha < campo.getProfundidade(); linha++) {
            for(int coluna = 0; coluna < campo.getLargura(); coluna++) {

                double sorteio = rand.nextDouble();

                if(sorteio <= PROB_CRIACAO_LOBO) {
                    Lobo lobo = new Lobo(true);
                    lobo.definirLocalizacao(new Localizacao(linha, coluna));
                    animais.add(lobo);
                    campo.colocar(lobo, linha, coluna);
                }
                else if(sorteio <= PROB_CRIACAO_LOBO + PROB_CRIACAO_RAPOSA) {
                    Raposa raposa = new Raposa(true);
                    raposa.definirLocalizacao(new Localizacao(linha, coluna));
                    animais.add(raposa);
                    campo.colocar(raposa, linha, coluna);
                }
                else if(sorteio <= PROB_CRIACAO_LOBO + PROB_CRIACAO_RAPOSA + PROB_CRIACAO_COELHO) {
                    Coelho coelho = new Coelho(true);
                    coelho.definirLocalizacao(new Localizacao(linha, coluna));
                    animais.add(coelho);
                    campo.colocar(coelho, linha, coluna);
                }
                else if(sorteio <= PROB_CRIACAO_LOBO + PROB_CRIACAO_RAPOSA + PROB_CRIACAO_COELHO + PROB_CRIACAO_PLANTA) {
                    Planta planta = new Planta(true);
                    planta.definirLocalizacao(new Localizacao(linha, coluna));
                    animais.add(planta);
                    campo.colocar(planta, linha, coluna);
                }
            }
        }

        Collections.shuffle(animais);
    }

    // =======================================================
    //  CONTROLES CHAMADOS PELA GUI
    // =======================================================

    /**
     * Inicia simulação longa (500 passos) sem travar a GUI.
     */
    public void iniciarSimulacaoLonga()
    {
        passosRestantes = 500;
        garantirTimer();
        timer.start();
    }

    /**
     * Inicia simulação contínua até não ser mais viável.
     */
    public void iniciarSimulacaoAteMorrer()
    {
        passosRestantes = -1;
        garantirTimer();
        timer.start();
    }

    /**
     * Reinicia o simulador e para simulação contínua.
     */
    public void reiniciar()
    {
        if(timer != null) {
            timer.stop();
        }
        resetar();
    }

    /**
     * Cria o Timer só uma vez.
     */
    private void garantirTimer()
    {
        if(timer == null) {
            timer = new Timer(80, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if(!visao.ehViavel(campo)) {
                        passosRestantes = 0;
                        timer.stop();
                        return;
                    }

                    simularUmPasso();

                    if(passosRestantes > 0) {
                        passosRestantes--;
                        if(passosRestantes == 0) {
                            timer.stop();
                        }
                    }
                }
            });
        }
    }
    public void pausar() {
    if (timer != null) timer.stop();
    }
}
