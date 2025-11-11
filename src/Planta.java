import java.util.Random;

/**
 * Representa uma planta no simulador.
 * Plantas crescem e se reproduzem, mas não se movem.
 * 
 *  
 */
public class Planta extends SerVivo {

    // A idade máxima que uma planta pode atingir.
    private static final int IDADE_MAXIMA_PLANTA = 25;           // era 30 — reduzida para que as plantas morram mais cedo
    // A idade em que uma planta pode começar a se reproduzir.
    private static final int IDADE_REPRODUCAO_PLANTA = 8;        // era 10 — começa a se reproduzir um pouco antes
    // A probabilidade de uma planta se reproduzir.
    private static final double PROBABILIDADE_REPRODUCAO_PLANTA = 0.04; // era 0.05 — reduzida para conter o crescimento
    // O número máximo de novas plantas por reprodução.
    private static final int TAMANHO_MAXIMO_NINHADA_PLANTA = 3;  // era 4 — diminui a multiplicação por rodada

    private static final Random aleatorio = new Random();

    public Planta(boolean idadeAleatoria) {
        super();
        this.idadeMaxima = IDADE_MAXIMA_PLANTA;
        this.idadeReproducao = IDADE_REPRODUCAO_PLANTA;
        this.probabilidadeReproducao = PROBABILIDADE_REPRODUCAO_PLANTA;
        this.tamanhoMaximoNinhada = TAMANHO_MAXIMO_NINHADA_PLANTA;

        if (idadeAleatoria) {
            this.idade = aleatorio.nextInt(IDADE_MAXIMA_PLANTA);
        } else {
            this.idade = 0;
        }
    }

    @Override
    protected void mover(Campo campoAtual, Campo campoNovo) {
        // Plantas não se movem.
    }

    @Override
    protected Ator criarFilho() {
        return new Planta(false);
    }
}
