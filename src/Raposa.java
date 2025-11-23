import java.util.Random;

/**
 * Um modelo simples de uma raposa.
 * Raposas envelhecem, caçam coelhos e se reproduzem.
 * 
 * Essa classe demonstra como o método agir(), herdado de SerVivo,
 * chama os comportamentos internos da raposa (como caçar e se mover),
 * agora apoiados pela lógica genérica de Animal/Carnivoro.
 * 
 *  
 */
public class Raposa extends Carnivoro {

    // A idade em que uma raposa pode começar a se reproduzir.
    private static final int IDADE_REPRODUCAO_RAPOSA = 10;
    // A idade máxima que uma raposa pode alcançar.
    private static final int IDADE_MAXIMA_RAPOSA = 160; // para aumentar longevidade
    // A probabilidade de uma raposa se reproduzir.
    private static final double PROBABILIDADE_REPRODUCAO_RAPOSA = 0.18; // antes 0.25 — reduz explosão de raposas

    // O número máximo de filhotes por ninhada.
    private static final int TAMANHO_MAXIMO_NINHADA_RAPOSA = 4;
    // O valor de comida de um único coelho (quantos passos ela sobrevive após comer).
    private static final int VALOR_COMIDA_COELHO = 10; // mais resistência à fome

    // Energia base
    private static final int ENERGIA_INICIAL = VALOR_COMIDA_COELHO;
    private static final int PERDA_POR_PASSO = 2;

    private static final Random aleatorio = new Random();

    /**
     * Cria uma raposa.
     * 
     * @param idadeAleatoria Se true, a raposa terá idade e energia aleatórias.
     */
    public Raposa(boolean idadeAleatoria) {
        super();
        this.idadeMaxima = IDADE_MAXIMA_RAPOSA;
        this.idadeReproducao = IDADE_REPRODUCAO_RAPOSA;
        this.probabilidadeReproducao = PROBABILIDADE_REPRODUCAO_RAPOSA;
        this.tamanhoMaximoNinhada = TAMANHO_MAXIMO_NINHADA_RAPOSA;

        this.energiaInicial = ENERGIA_INICIAL;
        this.perdaEnergiaPorPasso = PERDA_POR_PASSO;

        if (idadeAleatoria) {
            idade = aleatorio.nextInt(IDADE_MAXIMA_RAPOSA);
            energia = aleatorio.nextInt(VALOR_COMIDA_COELHO);
        } else {
            idade = 0;
            energia = ENERGIA_INICIAL;
        }
    }

    /**
     * A raposa só come coelhos.
     */
    @Override
    protected boolean podeComer(Ator ator) {
        return ator instanceof Coelho;
    }

    /**
     * Energia recebida ao comer um coelho.
     */
    @Override
    protected int energiaPorPresa(Ator ator) {
        return VALOR_COMIDA_COELHO;
    }

    /**
     * Cria um novo filho (raposa jovem).
     * 
     * @return Uma nova instância de Raposa.
     */
    @Override
    protected Animal criarFilho() {
        return new Raposa(false);
    }
}
