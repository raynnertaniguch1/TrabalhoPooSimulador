import java.util.List;
import java.util.Random;

/**
 * Um modelo simples de um coelho.
 *
 * Coelhos:
 *  - envelhecem e morrem por velhice
 *  - perdem fome a cada passo
 *  - comem plantas adjacentes
 *  - reproduzem conforme a regra base de Animal
 *  - movem-se uma vez por passo
 *
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-11
 * (Versão traduzida e adaptada pelo grupo: Lucas, Raynner, Higor e Brunno)
 */
public class Coelho extends Animal
{
    // ===== Configuração fixa da espécie =====
    private static final int IDADE_REPRODUCAO = 5;
    private static final int IDADE_MAXIMA = 50;
    private static final double PROB_REPRODUCAO = 0.15;
    private static final int TAMANHO_MAX_NINHADA = 5;

    /** Fome recuperada ao comer uma planta (quantos passos sobrevive sem comer). */
    private static final int VALOR_ALIMENTAR_PLANTA = 4;

    /** Random específico da espécie. */
    private static final Random rand = new Random();

    /** Fome atual do coelho. Se chegar a 0, ele morre. */
    private int nivelComida;

    /**
     * Constrói um coelho.
     *
     * @param idadeAleatoria se true, nasce com idade/fome aleatórias
     */
    public Coelho(boolean idadeAleatoria)
    {
        super(IDADE_MAXIMA, IDADE_REPRODUCAO, PROB_REPRODUCAO,
              TAMANHO_MAX_NINHADA, idadeAleatoria);

        // Nasce com fome aleatória ou cheia.
        if (idadeAleatoria) {
            nivelComida = rand.nextInt(VALOR_ALIMENTAR_PLANTA);
        } else {
            nivelComida = VALOR_ALIMENTAR_PLANTA;
        }
    }

    /**
     * Ações do coelho em um passo:
     * 1) envelhecer e sentir fome
     * 2) procurar planta adjacente
     * 3) se comer, move para a posição da planta e restaura fome
     * 4) se não comer, move para uma casa livre adjacente
     * 5) reproduzir
     */
    @Override
    public void agir(Campo campoAtual, Campo campoAtualizado, List<Ator> novosAtores)
    {
        envelhecer();
        diminuirFome();
        if(!estaAtivo()) return;

        // tenta achar comida (planta) no campo antigo
        Localizacao comida = encontrarComidaAdjacente(campoAtual);

        Localizacao novaLocalizacao;

        if (comida != null) {
            // achou planta: vai para lá e restaura fome
            novaLocalizacao = comida;
            nivelComida = VALOR_ALIMENTAR_PLANTA;
        }
        else {
            // não achou comida: tenta andar para lado livre no campo novo
            novaLocalizacao =
                campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());
        }

        // move ou morre se não houver espaço
        if(novaLocalizacao != null) {
            definirLocalizacao(novaLocalizacao);
            campoAtualizado.colocar(this, novaLocalizacao);
        }
        else {
            morrer(); // superlotação
            return;
        }

        if(!estaAtivo()) return;

        // reprodução padrão reaproveitada da Animal
        reproduzir(campoAtualizado, novosAtores);
    }

    /** Reduz a fome do coelho; se zerar, ele morre. */
    private void diminuirFome()
    {
        nivelComida--;
        if(nivelComida <= 0) {
            morrer();
        }
    }

    /** Dieta do coelho: só come planta. */
    @Override
    public boolean podeComer(Ator alvo)
    {
        return alvo instanceof Planta;
    }

    /** Ao comer, o coelho apenas torna a planta inativa. */
    @Override
    public void comer(Ator alvo)
    {
        ((Planta) alvo).serComida();
    }

    /** Cria um filhote da espécie Coelho. */
    @Override
    public Animal criarFilhote()
    {
        return new Coelho(false);
    }

    /** Chamado quando um predador come o coelho. */
    public void setComido()
    {
        morrer();
    }
}
