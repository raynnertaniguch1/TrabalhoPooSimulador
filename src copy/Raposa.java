import java.util.List;
import java.util.Random;

/**
 * Um modelo simples de uma raposa.
 * Raposas envelhecem, movem-se, comem coelhos e morrem.
 *
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-11
 * (Versão traduzida/refatorada pelo grupo: Lucas, Raynner, Higor e Brunno)
 */
public class Raposa extends Animal
{
    /** Quantidade de fome restaurada ao comer um coelho. */
    private static final int VALOR_ALIMENTAR_COELHO = 4;

    /** Random específico da espécie. */
    private static final Random rand = new Random();

    /** Fome da raposa. Se chegar a 0, ela morre. */
    private int nivelComida;

    /**
     * Constrói uma raposa.
     * Pode nascer com idade/fome aleatórias.
     */
    public Raposa(boolean idadeAleatoria)
    {
        super(150, 10, 0.09, 3, idadeAleatoria);

        if(idadeAleatoria) {
            nivelComida = rand.nextInt(VALOR_ALIMENTAR_COELHO);
        }
        else {
            nivelComida = VALOR_ALIMENTAR_COELHO;
        }
    }

    /**
     * Ações da raposa em um passo:
     * 1) envelhecer e sentir fome
     * 2) procurar coelho adjacente
     * 3) se comer, move para a posição do coelho
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

        if(comida != null) {
            // achou coelho: vai para lá e restaura fome
            novaLocalizacao = comida;
            nivelComida = VALOR_ALIMENTAR_COELHO;
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
            morrer();
            return;
        }

        if(!estaAtivo()) return;

        // reprodução padrão reaproveitada da Animal
        reproduzir(campoAtualizado, novosAtores);
    }

    /** Diminui fome; se chegar a zero, a raposa morre. */
    private void diminuirFome()
    {
        nivelComida--;
        if(nivelComida <= 0) {
            morrer();
        }
    }

    /** Raposa só pode comer coelho. */
    @Override
    public boolean podeComer(Ator alvo)
    {
        return alvo instanceof Coelho;
    }

    /** Ao comer, a raposa mata o coelho. */
    @Override
    public void comer(Ator alvo)
    {
        ((Coelho) alvo).setComido();
    }

    /** Cria um filhote da espécie Raposa. */
    @Override
    public Animal criarFilhote()
    {
        return new Raposa(false);
    }
}