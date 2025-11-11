import java.util.Iterator;
import java.util.Random;

/**
 * Representa um lobo no simulador.
 * Lobos envelhecem, caçam raposas e coelhos e morrem.
 * 
 * 
 */
public class Lobo extends SerVivo {

    // Características específicas do lobo
    private static final int IDADE_REPRODUCAO = 12;
    private static final int IDADE_MAXIMA = 100;
    private static final double PROBABILIDADE_REPRODUCAO = 0.08; // antes 0.10 — mantém o topo raro
    private static final int VALOR_COMIDA_COELHO = 6;            // mantém
    private static final int VALOR_COMIDA_RAPOSA = 10;           // mantém

    private static final int TAMANHO_MAXIMO_NINHADA = 2;
    private static final Random aleatorio = new Random();

    // Nível de comida do lobo
    private int nivelComida;

    /**
     * Cria um lobo.
     * Pode nascer com idade e fome aleatórias ou começar jovem e alimentado.
     * 
     * @param idadeAleatoria Se true, o lobo nasce com idade e fome aleatórias.
     */
    public Lobo(boolean idadeAleatoria) {
        super();
        this.idadeMaxima = IDADE_MAXIMA;
        this.idadeReproducao = IDADE_REPRODUCAO;
        this.probabilidadeReproducao = PROBABILIDADE_REPRODUCAO;
        this.tamanhoMaximoNinhada = TAMANHO_MAXIMO_NINHADA;

        if (idadeAleatoria) {
            idade = aleatorio.nextInt(IDADE_MAXIMA);
            nivelComida = aleatorio.nextInt(VALOR_COMIDA_RAPOSA);
        } else {
            idade = 0;
            nivelComida = VALOR_COMIDA_RAPOSA;
        }
    }

    /**
     * Move o lobo.
     * O lobo tenta primeiro encontrar uma raposa ou um coelho próximo para comer.
     * Caso contrário, move-se para uma localização livre.
     * Se não houver espaço, morre por superpopulação.
     */
    @Override
    protected void mover(Campo campoAtual, Campo campoNovo) {
        aumentarFome();

        if (!estaVivo()) {
            return;
        }

        // Procura por comida nas redondezas (raposas ou coelhos)
        Localizacao novaLocalizacao = procurarComida(campoAtual, localizacao);

        // Se não encontrou comida, tenta se mover para um local livre
        if (novaLocalizacao == null) {
            novaLocalizacao = campoNovo.localizacaoAdjacenteLivre(localizacao);
        }

        // Se conseguiu se mover, atualiza posição; senão, morre por superpopulação
        if (novaLocalizacao != null) {
            setLocalizacao(novaLocalizacao);
            campoNovo.colocar(this, novaLocalizacao);
        } else {
            morrer();
        }
    }

    /**
     * Cria um novo lobo (filho).
     * 
     * @return Uma nova instância de Lobo.
     */
    @Override
    protected Ator criarFilho() {
        return new Lobo(false);
    }

    /**
     * Aumenta a fome do lobo. Se a comida acabar, ele morre.
     */
    private void aumentarFome() {
        nivelComida--;
        if (nivelComida <= 0) {
            morrer();
        }
    }

    /**
     * Procura por presas (coelhos e raposas) em locais adjacentes.
     * Se encontrar, come e restaura o nível de comida.
     * 
     * @param campo O campo atual.
     * @param localizacao A posição atual do lobo.
     * @return A nova localização após encontrar comida, ou null se não encontrou.
     */
    private Localizacao procurarComida(Campo campo, Localizacao localizacao) {
        Iterator<Localizacao> adjacentes = campo.localizacoesAdjacentes(localizacao);
        while (adjacentes.hasNext()) {
            Localizacao onde = adjacentes.next();
            Object ator = campo.getObjetoEm(onde);

            if (ator instanceof Raposa) {
                Raposa raposa = (Raposa) ator;
                if (raposa.estaVivo()) {
                    raposa.morrer();
                    nivelComida = VALOR_COMIDA_RAPOSA;
                    return onde;
                }
            } else if (ator instanceof Coelho) {
                Coelho coelho = (Coelho) ator;
                if (coelho.estaVivo()) {
                    coelho.morrer();
                    nivelComida = VALOR_COMIDA_COELHO;
                    return onde;
                }
            }
        }
        return null;
    }
}
