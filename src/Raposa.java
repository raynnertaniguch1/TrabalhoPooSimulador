import java.util.Iterator;
import java.util.Random;

/**
 * Um modelo simples de uma raposa.
 * Raposas envelhecem, caçam coelhos e se reproduzem.
 * 
 * Essa classe demonstra como o método agir(), herdado de SerVivo,
 * chama os comportamentos internos da raposa (como caçar e se mover).
 * 
 * @author Raynner
 */
public class Raposa extends SerVivo {

    // A idade em que uma raposa pode começar a se reproduzir.
    private static final int IDADE_REPRODUCAO_RAPOSA = 10;
    // A idade máxima que uma raposa pode alcançar.
    private static final int IDADE_MAXIMA_RAPOSA = 160; //  para aumentar longevidade
    // A probabilidade de uma raposa se reproduzir.
    private static final double PROBABILIDADE_REPRODUCAO_RAPOSA = 0.18; // antes 0.25 — reduz explosão de raposas

    // O número máximo de filhotes por ninhada.
    private static final int TAMANHO_MAXIMO_NINHADA_RAPOSA = 4;
    // O valor de comida de um único coelho (quantos passos ela sobrevive após comer).
    private static final int VALOR_COMIDA_COELHO = 10; //  mais resistência à fome
    private static final Random aleatorio = new Random();

    // O nível de comida da raposa, que aumenta ao comer coelhos.
    private int nivelComida;

    /**
     * Cria uma raposa.
     * 
     * @param idadeAleatoria Se true, a raposa terá idade e fome aleatórias.
     */
    public Raposa(boolean idadeAleatoria) {
        super();
        this.idadeMaxima = IDADE_MAXIMA_RAPOSA;
        this.idadeReproducao = IDADE_REPRODUCAO_RAPOSA;
        this.probabilidadeReproducao = PROBABILIDADE_REPRODUCAO_RAPOSA;
        this.tamanhoMaximoNinhada = TAMANHO_MAXIMO_NINHADA_RAPOSA;

        if (idadeAleatoria) {
            idade = aleatorio.nextInt(IDADE_MAXIMA_RAPOSA);
            nivelComida = aleatorio.nextInt(VALOR_COMIDA_COELHO);
        } else {
            idade = 0;
            nivelComida = VALOR_COMIDA_COELHO;
        }
    }

    /**
     * O método agir() é herdado de SerVivo.
     * Ele executa o ciclo básico: envelhecer, mover e reproduzir.
     * 
     * Neste caso, o método mover() da Raposa é responsável por caçar.
     */

    /**
     * Move a raposa no campo.
     * A raposa tenta primeiro encontrar um coelho próximo para comer.
     * Se encontrar, move-se até ele e restaura sua energia.
     * Caso contrário, move-se aleatoriamente.
     * Se não conseguir se mover (sem espaço livre), morre por superpopulação.
     * 
     * @param campoAtual O campo atual.
     * @param campoNovo  O campo atualizado.
     */
    @Override
    protected void mover(Campo campoAtual, Campo campoNovo) {
        aumentarFome();

        if (!estaVivo()) {
            return;
        }

        // Tenta encontrar comida (coelhos próximos)
        Localizacao novaLocalizacao = procurarComida(campoAtual, localizacao);
        if (novaLocalizacao == null) {
            // Nenhum coelho encontrado - move-se para um local livre
            novaLocalizacao = campoNovo.localizacaoAdjacenteLivre(localizacao);
        }

        if (novaLocalizacao != null) {
            setLocalizacao(novaLocalizacao);
            campoNovo.colocar(this, novaLocalizacao);
        } else {
            // Superpopulação - não conseguiu se mover
            morrer();
        }
    }

    /**
     * Cria um novo filho (raposa jovem).
     * 
     * @return Uma nova instância de Raposa.
     */
    @Override
    protected Ator criarFilho() {
        return new Raposa(false);
    }

    /**
     * Aumenta a fome da raposa. Se o nível de comida chegar a zero, ela morre.
     */
    private void aumentarFome() {
        nivelComida--;
        if (nivelComida <= 0) {
            morrer();
        }
    }

    /**
     * Tenta encontrar e comer um coelho em uma das localizações adjacentes.
     * 
     * @param campo       O campo onde procurar.
     * @param localizacao A posição atual da raposa.
     * @return A localização onde encontrou comida, ou null se não encontrou.
     */
    private Localizacao procurarComida(Campo campo, Localizacao localizacao) {
        Iterator<Localizacao> adjacentes = campo.localizacoesAdjacentes(localizacao);
        while (adjacentes.hasNext()) {
            Localizacao onde = adjacentes.next();
            Object ator = campo.getObjetoEm(onde);
            if (ator instanceof Coelho) {
                Coelho coelho = (Coelho) ator;
                if (coelho.estaVivo()) {
                    coelho.morrer(); // o coelho foi comido
                    nivelComida = VALOR_COMIDA_COELHO;
                    return onde;
                }
            }
        }
        return null;
    }
}
