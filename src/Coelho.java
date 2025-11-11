import java.util.Iterator;
import java.util.Random;

/**
 * Um modelo simples de um coelho.
 * Coelhos envelhecem, movem-se, reproduzem-se e morrem.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-11
 */
public class Coelho extends SerVivo
{
    // Características compartilhadas por todos os coelhos (campos estáticos).

    // A idade em que um coelho pode começar a se reproduzir.
    private static final int IDADE_REPRODUCAO = 3; // era 5 — reduzida para aumentar a taxa de sobrevivência
    // A idade máxima que um coelho pode atingir.
    private static final int IDADE_MAXIMA = 50;
    // A probabilidade de um coelho se reproduzir.
    private static final double PROBABILIDADE_REPRODUCAO = 0.18; // antes 0.12 — melhora taxa populacional

    // O número máximo de filhotes por ninhada.
    private static final int TAMANHO_MAXIMO_NINHADA = 4;         // antes 3 — mais descendentes

    // Um gerador de números aleatórios compartilhado para controlar a reprodução.
    private static final Random aleatorio = new Random();

    /**
     * Cria um novo coelho. Um coelho pode ser criado com idade zero
     * (recém-nascido) ou com idade aleatória.
     * 
     * @param idadeAleatoria Se true, o coelho terá uma idade aleatória.
     */
    public Coelho(boolean idadeAleatoria)
    {
        super();
        this.idadeMaxima = IDADE_MAXIMA;
        this.idadeReproducao = IDADE_REPRODUCAO;
        this.probabilidadeReproducao = PROBABILIDADE_REPRODUCAO;
        this.tamanhoMaximoNinhada = TAMANHO_MAXIMO_NINHADA;

        if (idadeAleatoria) {
            idade = aleatorio.nextInt(IDADE_MAXIMA);
        } else {
            idade = 0;
        }
    }

    /**
     * Isto é o que o coelho faz na maior parte do tempo — ele se move
     * por aí em busca de espaço livre. Às vezes ele se reproduz ou morre
     * de velhice.
     * 
     * @param campoAtual O campo atual.
     * @param campoNovo  O campo atualizado para o novo passo.
     */
    @Override
    protected void mover(Campo campoAtual, Campo campoNovo) {
        // Coelho tenta primeiro encontrar uma planta para comer.
        Iterator<Localizacao> adjacentes = campoAtual.localizacoesAdjacentes(localizacao);
        while (adjacentes.hasNext()) {
            Localizacao onde = adjacentes.next();
            Object ator = campoAtual.getObjetoEm(onde);
            if (ator instanceof Planta) {
                Planta planta = (Planta) ator;
                if (planta.estaVivo()) {
                    planta.morrer(); // planta foi comida
                    setLocalizacao(onde);
                    campoNovo.colocar(this, onde);
                    return; // comeu e se moveu
                }
            }
        }

        // Se não encontrou planta, move-se aleatoriamente.
        Localizacao novaLocalizacao = campoNovo.localizacaoAdjacenteLivre(localizacao);
        if (novaLocalizacao != null) {
            setLocalizacao(novaLocalizacao);
            campoNovo.colocar(this, novaLocalizacao);
        } else {
            // Superpopulação: não conseguiu se mover.
            morrer();
        }
    }

    /**
     * Cria um novo coelho (filho).
     * 
     * @return Uma nova instância de Coelho.
     */
    @Override
    protected Ator criarFilho()
    {
        return new Coelho(false);
    }
}
