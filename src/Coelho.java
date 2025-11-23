import java.util.Random;

/**
 * Um modelo simples de um coelho.
 * Coelhos envelhecem, movem-se, reproduzem-se e morrem.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-11
 */
public class Coelho extends Herbivoro
{
    // Características compartilhadas por todos os coelhos (campos estáticos).

    // A idade em que um coelho pode começar a se reproduzir.
    private static final int IDADE_REPRODUCAO = 3;  
    // A idade máxima que um coelho pode atingir.
    private static final int IDADE_MAXIMA = 50;
    // A probabilidade de um coelho se reproduzir.
    private static final double PROBABILIDADE_REPRODUCAO = 0.18; 

    // O número máximo de filhotes por ninhada.
    private static final int TAMANHO_MAXIMO_NINHADA = 4;         

    // Energia padrão do coelho.
    private static final int ENERGIA_INICIAL = 8;
    private static final int PERDA_POR_PASSO = 1;

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

        this.energiaInicial = ENERGIA_INICIAL;
        this.perdaEnergiaPorPasso = PERDA_POR_PASSO;
        this.energia = ENERGIA_INICIAL;

        if (idadeAleatoria) {
            idade = aleatorio.nextInt(IDADE_MAXIMA);
        } else {
            idade = 0;
        }
    }

    /**
     * Cria um novo coelho (filho).
     * 
     * @return Uma nova instância de Coelho.
     */
    @Override
    protected Animal criarFilho()
    {
        return new Coelho(false);
    }
}
