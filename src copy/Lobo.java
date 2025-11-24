import java.util.List;
import java.util.Random;

/**
 * Modelo simples de lobo.
 *
 * Lobos:
 *  - envelhecem e morrem por velhice
 *  - perdem fome a cada passo
 *  - comem coelhos ou raposas adjacentes
 *  - reproduzem conforme a regra base de Animal
 *  - movem-se uma vez por passo
 */
public class Lobo extends Animal
{
    /** Quantidade de fome restaurada ao comer uma presa. */
    private static final int VALOR_ALIMENTAR_PRESA = 6;

    /** Random específico da espécie. */
    private static final Random rand = new Random();

    /** Fome do lobo. Se chegar a 0, ele morre. */
    private int nivelComida;

    /**
     * Constrói um lobo.
     * Pode nascer com idade/fome aleatórias.
     */
    public Lobo(boolean idadeAleatoria)
    {
        super(180, 15, 0.06, 2, idadeAleatoria);

        if (idadeAleatoria) {
            nivelComida = rand.nextInt(VALOR_ALIMENTAR_PRESA);
        } else {
            nivelComida = VALOR_ALIMENTAR_PRESA;
        }
    }

    /**
     * Ações do lobo em um passo:
     * 1) envelhecer e sentir fome
     * 2) procurar presa adjacente
     * 3) se comer, move para a posição da presa
     * 4) se não comer, move para uma casa livre
     * 5) reproduzir
     */
    @Override
    public void agir(Campo campoAtual, Campo campoAtualizado, List<Ator> novosAtores)
    {
        envelhecer();
        diminuirFome();
        if(!estaAtivo()) return;

        // tenta achar comida no campo antigo
        Localizacao comida = encontrarComidaAdjacente(campoAtual);

        Localizacao novaLocalizacao;

        if (comida != null) {
            // achou presa: vai para lá e restaura fome
            novaLocalizacao = comida;
            nivelComida = VALOR_ALIMENTAR_PRESA;
        }
        else {
            // não achou presa: tenta andar para lado livre no campo novo
            novaLocalizacao =
                campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());
        }

        // move ou morre se não houver espaço
        if(novaLocalizacao != null) {
            definirLocalizacao(novaLocalizacao);
            campoAtualizado.colocar(this, novaLocalizacao);
        }
        else {
            morrer();
            return;
        }

        if(!estaAtivo()) return;

        // reprodução padrão reaproveitada da Animal
        reproduzir(campoAtualizado, novosAtores);
    }

    /** Diminui fome; se chegar a zero, o lobo morre. */
    private void diminuirFome()
    {
        nivelComida--;
        if(nivelComida <= 0) {
            morrer();
        }
    }

    /** Dieta do lobo: coelho ou raposa. */
    @Override
    public boolean podeComer(Ator alvo)
    {
        return (alvo instanceof Coelho) || (alvo instanceof Raposa);
    }

    /**
     * Ao comer:
     *  - se for coelho, aplica o método específico do coelho
     *  - se for raposa, usa a morte padrão de Animal
     */
    @Override
    public void comer(Ator alvo)
    {
        if (alvo instanceof Coelho) {
            ((Coelho) alvo).setComido();
        }
        else if (alvo instanceof Raposa) {
            ((Raposa) alvo).morrer();
        }
    }

    /** Cria um filhote da espécie Lobo. */
    @Override
    public Animal criarFilhote()
    {
        return new Lobo(false);
    }
}
