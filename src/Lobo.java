import java.util.Random;

/**
 * Representa um lobo no simulador.
 * Lobos envelhecem, caçam raposas e coelhos e morrem.
 * 
 * Versão adaptada para o modelo com Animal/Carnivoro, evitando
 * duplicação da lógica de movimento, caça e reprodução.
 */
public class Lobo extends Carnivoro {

    // Características específicas do lobo
    private static final int IDADE_REPRODUCAO = 12;
    private static final int IDADE_MAXIMA = 100;
    private static final double PROBABILIDADE_REPRODUCAO = 0.08; // mantém o topo raro

    private static final int TAMANHO_MAXIMO_NINHADA = 2;

    private static final int VALOR_COMIDA_COELHO = 6;            // mantém
    private static final int VALOR_COMIDA_RAPOSA = 10;           // mantém

    private static final int ENERGIA_INICIAL = VALOR_COMIDA_RAPOSA;
    private static final int PERDA_POR_PASSO = 2;

    private static final Random aleatorio = new Random();

    /**
     * Cria um lobo.
     * Pode nascer com idade e "energia" aleatórias ou começar jovem.
     * 
     * @param idadeAleatoria Se true, o lobo nasce com idade e energia aleatórias.
     */
    public Lobo(boolean idadeAleatoria) {
        super();
        this.idadeMaxima = IDADE_MAXIMA;
        this.idadeReproducao = IDADE_REPRODUCAO;
        this.probabilidadeReproducao = PROBABILIDADE_REPRODUCAO;
        this.tamanhoMaximoNinhada = TAMANHO_MAXIMO_NINHADA;

        this.energiaInicial = ENERGIA_INICIAL;
        this.perdaEnergiaPorPasso = PERDA_POR_PASSO;

        if (idadeAleatoria) {
            idade = aleatorio.nextInt(IDADE_MAXIMA);
            energia = aleatorio.nextInt(VALOR_COMIDA_RAPOSA);
        } else {
            idade = 0;
            energia = ENERGIA_INICIAL;
        }
    }

    /**
     * Lobo come raposas e coelhos.
     */
    @Override
    protected boolean podeComer(Ator ator) {
        return ator instanceof Raposa || ator instanceof Coelho;
    }

    /**
     * Energia obtida depende da presa.
     */
    @Override
    protected int energiaPorPresa(Ator ator) {
        if (ator instanceof Raposa) {
            return VALOR_COMIDA_RAPOSA;
        }
        return VALOR_COMIDA_COELHO;
    }

    /**
     * Cria um novo lobo (filho).
     * 
     * @return Uma nova instância de Lobo.
     */
    @Override
    protected Animal criarFilho() {
        return new Lobo(false);
    }
}
