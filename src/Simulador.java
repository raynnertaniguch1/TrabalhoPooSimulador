import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Um simulador simples de predador-presa, baseado em um campo contendo
 * coelhos, raposas, lobos e plantas.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-09 / adaptado por Raynner
 */
public class Simulador {
    // As variáveis privadas estáticas finais representam informações de configuração
    private static final int LARGURA_PADRAO = 50;
    private static final int PROFUNDIDADE_PADRAO = 50;

    // Probabilidades de criação inicial
   private static final double PROB_CRIACAO_PLANTA = 0.08;
    private static final double PROB_CRIACAO_COELHO = 0.10;
    private static final double PROB_CRIACAO_RAPOSA = 0.04;
    private static final double PROB_CRIACAO_LOBO   = 0.02;

    // Lista de atores no campo
    private List<Ator> atores;
    // Lista de atores recém-nascidos
    private List<Ator> novosAtores;
    // Estado atual do campo
    private Campo campo;
    // Um segundo campo usado para construir o próximo estágio da simulação
    private Campo campoAtualizado;
    // O passo atual da simulação
    private int passo;
    // Uma visão gráfica da simulação
    private VisualizacaoSimulador visualizacao;

    /**
     * Constrói um campo de simulação com tamanho padrão.
     */
    public Simulador() {
        this(PROFUNDIDADE_PADRAO, LARGURA_PADRAO);
    }

    /**
     * Cria um campo de simulação com o tamanho fornecido.
     * 
     * @param profundidade Profundidade do campo. Deve ser maior que zero.
     * @param largura       Largura do campo. Deve ser maior que zero.
     */
    public Simulador(int profundidade, int largura) {
        if (largura <= 0 || profundidade <= 0) {
            System.out.println("As dimensões devem ser maiores que zero.");
            System.out.println("Usando valores padrão.");
            profundidade = PROFUNDIDADE_PADRAO;
            largura = LARGURA_PADRAO;
        }

        atores = new ArrayList<>();
        novosAtores = new ArrayList<>();
        campo = new Campo(profundidade, largura);
        campoAtualizado = new Campo(profundidade, largura);

        // Cria uma visualização do estado de cada localização no campo
        visualizacao = new VisualizacaoSimulador(profundidade, largura);
        visualizacao.definirCor(Raposa.class, Color.blue);
        visualizacao.definirCor(Coelho.class, Color.orange);
        visualizacao.definirCor(Lobo.class, Color.red);
        visualizacao.definirCor(Planta.class, Color.green);

        // Define um ponto de partida válido
        reiniciar();
    }

    /**
     * Executa a simulação a partir do estado atual por um período razoavelmente longo.
     */
    public void executarSimulacaoLonga() {
        simular(500);
    }

    /**
     * Executa a simulação pelo número fornecido de passos.
     */
    public void simular(int numPassos) {
        for (int passo = 1; passo <= numPassos && visualizacao.eViavel(campo); passo++) {
            simularUmPasso();
        }
    }

    /**
     * Executa a simulação por um único passo.
     * Itera sobre todo o campo, atualizando o estado de cada ator.
     */
    public void simularUmPasso() {
        passo++;
        novosAtores.clear();

        // Faz todos os atores agirem e embaralha a cada ciclo
        Collections.shuffle(atores);
        for (Iterator<Ator> it = atores.iterator(); it.hasNext();) {
            Ator ator = it.next();
            if (ator.estaVivo()) {
                ator.agir(campo, campoAtualizado, novosAtores);
            } else {
                it.remove();
            }
        }

        // Adiciona novos atores ao campo
        atores.addAll(novosAtores);

        // Troca o campo e o campo atualizado
        Campo temp = campo;
        campo = campoAtualizado;
        campoAtualizado = temp;
        campoAtualizado.limpar();

        // Exibe o novo estado na tela
        visualizacao.mostrarStatus(passo, campo);
    }

    /**
     * Reinicia a simulação para um estado inicial.
     */
    public void reiniciar() {
        passo = 0;
        atores.clear();
        campo.limpar();
        campoAtualizado.limpar();
        povoar(campo);
        visualizacao.mostrarStatus(passo, campo);
    }

    /**
     * Povoa o campo com coelhos, raposas, lobos e plantas.
     */
    private void povoar(Campo campo) {
        Random aleatorio = new Random();
        campo.limpar();

        for (int linha = 0; linha < campo.getProfundidade(); linha++) {
            for (int coluna = 0; coluna < campo.getLargura(); coluna++) {
                if (aleatorio.nextDouble() <= PROB_CRIACAO_RAPOSA) {
                    Raposa raposa = new Raposa(true);
                    atores.add(raposa);
                    raposa.setLocalizacao(linha, coluna);
                    campo.colocar(raposa, linha, coluna);
                } else if (aleatorio.nextDouble() <= PROB_CRIACAO_COELHO) {
                    Coelho coelho = new Coelho(true);
                    atores.add(coelho);
                    coelho.setLocalizacao(linha, coluna);
                    campo.colocar(coelho, linha, coluna);
                } else if (aleatorio.nextDouble() <= PROB_CRIACAO_LOBO) {
                    Lobo lobo = new Lobo(true);
                    atores.add(lobo);
                    lobo.setLocalizacao(linha, coluna);
                    campo.colocar(lobo, linha, coluna);
                } else if (aleatorio.nextDouble() <= PROB_CRIACAO_PLANTA) {
                    Planta planta = new Planta(true);
                    atores.add(planta);
                    planta.setLocalizacao(linha, coluna);
                    campo.colocar(planta, linha, coluna);
                }
            }
        }

        // Embaralha os atores para evitar ordem fixa de execução
        Collections.shuffle(atores);
    }
    /**
 * Retorna o campo atual da simulação.
 */
    public Campo getCampo() {
        return campo;
    }

    /**
     * Indica se a simulação ainda é viável (há mais de uma espécie viva).
     */
    public boolean eViavel() {
        return visualizacao.eViavel(campo);
    }
}
