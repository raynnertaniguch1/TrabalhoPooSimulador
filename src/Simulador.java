import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Um simulador simples de predador-presa, baseado em um campo contendo
 * coelhos e raposas.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-09
 */
public class Simulador
{
    // As variáveis privadas estáticas finais representam
    // informações de configuração para a simulação.
    // A largura padrão da grade.
    private static final int LARGURA_PADRAO = 50;
    // A profundidade padrão da grade.
    private static final int PROFUNDIDADE_PADRAO = 50;
    // A probabilidade de uma raposa ser criada em uma posição da grade.
    private static final double PROB_CRIACAO_RAPOSA = 0.02;
    // A probabilidade de um coelho ser criado em uma posição da grade.
    private static final double PROB_CRIACAO_COELHO = 0.08;    

    // Lista de animais no campo.
    private List animais;
    // Lista de animais recém-nascidos.
    private List novosAnimais;
    // Estado atual do campo.
    private Campo campo;
    // Um segundo campo usado para construir o próximo estágio da simulação.
    private Campo campoAtualizado;
    // O passo atual da simulação.
    private int passo;
    // Uma visão gráfica da simulação.
    private VisualizacaoSimulador visualizacao;
    
    /**
     * Constrói um campo de simulação com tamanho padrão.
     */
    public Simulador()
    {
        this(PROFUNDIDADE_PADRAO, LARGURA_PADRAO);
    }
    
    /**
     * Cria um campo de simulação com o tamanho fornecido.
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
        animais = new ArrayList();
        novosAnimais = new ArrayList();
        campo = new Campo(profundidade, largura);
        campoAtualizado = new Campo(profundidade, largura);

        // Cria uma visualização do estado de cada localização no campo.
        visualizacao = new VisualizacaoSimulador(profundidade, largura);
        visualizacao.definirCor(Raposa.class, Color.blue);
        visualizacao.definirCor(Coelho.class, Color.orange);
        
        // Define um ponto de partida válido.
        reiniciar();
    }
    
    /**
     * Executa a simulação a partir do estado atual por um período razoavelmente longo,
     * por exemplo, 500 passos.
     */
    public void executarSimulacaoLonga()
    {
        simular(500);
    }
    
    /**
     * Executa a simulação a partir do estado atual pelo número fornecido de passos.
     * Para antes se ela deixar de ser viável.
     */
    public void simular(int numPassos)
    {
        for(int passo = 1; passo <= numPassos && visualizacao.eViavel(campo); passo++) {
            simularUmPasso();
        }
    }
    
    /**
     * Executa a simulação por um único passo.
     * Itera sobre todo o campo atualizando o estado de cada
     * raposa e coelho.
     */
    public void simularUmPasso()
    {
        passo++;
        novosAnimais.clear();
        
        // Faz todos os animais agirem.
        for(Iterator it = animais.iterator(); it.hasNext(); ) {
            Object animal = it.next();
            if(animal instanceof Coelho) {
                Coelho coelho = (Coelho)animal;
                if(coelho.estaVivo()) {
                    coelho.correr(campoAtualizado, novosAnimais);
                }
                else {
                    it.remove();   // remove coelhos mortos da coleção.
                }
            }
            else if(animal instanceof Raposa) {
                Raposa raposa = (Raposa)animal;
                if(raposa.estaViva()) {
                    raposa.caca(campo, campoAtualizado, novosAnimais);
                }
                else {
                    it.remove();   // remove raposas mortas da coleção.
                }
            }
        }
        // Adiciona os novos animais à lista.
        animais.addAll(novosAnimais);
        
        // Troca o campo e o campo atualizado no final do passo.
        Campo temp = campo;
        campo = campoAtualizado;
        campoAtualizado = temp;
        campoAtualizado.limpar();

        // Exibe o novo campo na tela.
        visualizacao.mostrarStatus(passo, campo);
    }
        
    /**
     * Reinicia a simulação para um estado inicial.
     */
    public void reiniciar()
    {
        passo = 0;
        animais.clear();
        campo.limpar();
        campoAtualizado.limpar();
        povoar(campo);
        
        // Mostra o estado inicial na visualização.
        visualizacao.mostrarStatus(passo, campo);
    }
    
    /**
     * Povoa o campo com raposas e coelhos.
     */
    private void povoar(Campo campo)
    {
        Random aleatorio = new Random();
        campo.limpar();
        for(int linha = 0; linha < campo.getProfundidade(); linha++) {
            for(int coluna = 0; coluna < campo.getLargura(); coluna++) {
                if(aleatorio.nextDouble() <= PROB_CRIACAO_RAPOSA) {
                    Raposa raposa = new Raposa(true);
                    animais.add(raposa);
                    raposa.setLocalizacao(linha, coluna);
                    campo.colocar(raposa, linha, coluna);
                }
                else if(aleatorio.nextDouble() <= PROB_CRIACAO_COELHO) {
                    Coelho coelho = new Coelho(true);
                    animais.add(coelho);
                    coelho.setLocalizacao(linha, coluna);
                    campo.colocar(coelho, linha, coluna);
                }
                // Caso contrário, deixa a localização vazia.
            }
        }
        Collections.shuffle(animais);
    }
}
