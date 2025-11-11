import java.util.List;
import java.util.Random;

/**
 * Classe abstrata que representa um ser vivo no simulador.
 * Define o comportamento básico e o ciclo de vida comum a todos os seres biológicos.
 * 
 * Subclasses (como Raposa, Coelho, Planta, Lobo) devem implementar
 * os métodos abstratos mover() e criarFilho().
 * 
 * 
 */
public abstract class SerVivo extends Ator {

    //  Atributos comuns a todos os seres vivos
    protected int idade;
    protected int idadeMaxima;
    protected int idadeReproducao;
    protected double probabilidadeReproducao;
    protected int tamanhoMaximoNinhada;
    protected static final Random aleatorio = new Random();

    /**
     * Construtor padrão — todo ser vivo nasce com idade 0 e vivo.
     */
    public SerVivo() {
        super();
        this.idade = 0;
    }

    // ============================================================
    //  MÉTODO TEMPLATE: define o "esqueleto" do comportamento
    // ============================================================

    @Override
    public void agir(Campo campoAtual, Campo campoNovo, List<Ator> novos) {
        envelhecer();

        if (estaVivo()) {
            mover(campoAtual, campoNovo);          // cada subclasse define como se move
            reproduzir(campoNovo, novos);          // comportamento genérico de reprodução
        }
    }

    // ============================================================
    //  MÉTODOS COMUNS A TODOS OS SERES VIVOS
    // ============================================================

    /**
     * Aumenta a idade e verifica se o ser ainda está vivo.
     */
    protected void envelhecer() {
        idade++;
        if (idade > idadeMaxima) {
            morrer();
        }
    }

    /**
     * Verifica se o ser pode se reproduzir e, se sim, cria novos indivíduos.
     */
    protected void reproduzir(Campo campo, List<Ator> novos) {
        if (podeReproduzir() && aleatorio.nextDouble() <= probabilidadeReproducao) {
            int nascimentos = aleatorio.nextInt(tamanhoMaximoNinhada) + 1;

            for (int i = 0; i < nascimentos; i++) {
                Ator filho = criarFilho(); // método abstrato — cada subclasse cria o tipo certo
                Localizacao loc = campo.localizacaoAdjacenteAleatoria(localizacao);

                if (loc != null) {
                    filho.setLocalizacao(loc);
                    campo.colocar(filho, loc);
                    novos.add(filho);
                }
            }
        }
    }

    /**
     * Verifica se o ser já atingiu a idade de reprodução.
     */
    protected boolean podeReproduzir() {
        return idade >= idadeReproducao;
    }

    // ============================================================
    //  MÉTODOS ABSTRATOS — subclasses implementam o detalhe
    // ============================================================

    /**
     * Define como o ser se move no campo.
     * (Raposas caçam, coelhos correm, plantas não se movem, etc.)
     */
    protected abstract void mover(Campo campoAtual, Campo campoNovo);

    /**
     * Cria um novo indivíduo da mesma espécie.
     * (Ex: Raposa cria uma nova Raposa, Coelho cria Coelho, etc.)
     */
    protected abstract Ator criarFilho();
}
