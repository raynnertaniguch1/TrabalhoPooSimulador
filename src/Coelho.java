import java.util.List;
import java.util.Random;

/**
 * Um modelo simples de um coelho.
 * Coelhos envelhecem, movem-se, reproduzem-se e morrem.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-11
 */
public class Coelho extends Animal
{
    // Características compartilhadas por todos os coelhos (campos estáticos).

    // A idade em que um coelho pode começar a se reproduzir.
    private static final int IDADE_REPRODUCAO = 5;
    // A idade máxima que um coelho pode atingir.
    private static final int IDADE_MAXIMA = 50;
    // A probabilidade de um coelho se reproduzir.
    private static final double PROBABILIDADE_REPRODUCAO = 0.15;
    // O número máximo de filhotes por ninhada.
    private static final int TAMANHO_MAXIMO_NINHADA = 5;
    // Um gerador de números aleatórios compartilhado para controlar a reprodução.
    private static final Random aleatorio = new Random();
    
   
    
     

    /**
     * Cria um novo coelho. Um coelho pode ser criado com idade zero
     * (recém-nascido) ou com idade aleatória.
     * 
     * @param idadeAleatoria Se true, o coelho terá uma idade aleatória.
     */
    public Coelho( boolean idadeAleatoria)
    {
         
         
        if(idadeAleatoria) {
            idade = aleatorio.nextInt(IDADE_MAXIMA);
        }
    }
    
    /**
     * Isto é o que o coelho faz na maior parte do tempo — ele corre
     * por aí. Às vezes ele se reproduz ou morre de velhice.
     * 
     * @param campoAtualizado O campo atualizado para o novo passo.
     * @param novosCoelhos Lista onde novos coelhos nascidos serão adicionados.
     */
    public void correr(Campo campoAtualizado, List novosCoelhos)
    {
        envelhecer();
        if(vivo) {
            int nascimentos = reproduzir();
            for(int b = 0; b < nascimentos; b++) {
                Coelho novoCoelho = new Coelho(false);
                novosCoelhos.add(novoCoelho);
                Localizacao loc = campoAtualizado.localizacaoAdjacenteAleatoria(localizacao);
                novoCoelho.setLocalizacao(loc);
                campoAtualizado.colocar(novoCoelho, loc);
            }
            Localizacao novaLocalizacao = campoAtualizado.localizacaoAdjacenteLivre(localizacao);
            // Só se move para o campo atualizado se houver uma localização livre.
            if(novaLocalizacao != null) {
                setLocalizacao(novaLocalizacao);
                campoAtualizado.colocar(this, novaLocalizacao);
            }
            else {
                // Não pode se mover nem ficar — superpopulação — todas as localizações ocupadas.
                vivo = false;
            }
        }
    }
     @Override
    public void agir(Campo campoAtual, Campo campoNovo, List<Animal> novos) {
    // aqui você pode chamar o comportamento principal da raposa
    correr(campoAtual, novos);
    }
    /**
     * Aumenta a idade.
     * Isso pode resultar na morte do coelho.
     */
    private void envelhecer()
    {
        idade++;
        if(idade > IDADE_MAXIMA) {
            vivo = false;
        }
    }
    
    /**
     * Gera um número representando o número de nascimentos,
     * se o coelho puder se reproduzir.
     * @return O número de nascimentos (pode ser zero).
     */
    private int reproduzir()
    {
        int nascimentos = 0;
        if(podeReproduzir() && aleatorio.nextDouble() <= PROBABILIDADE_REPRODUCAO) {
            nascimentos = aleatorio.nextInt(TAMANHO_MAXIMO_NINHADA) + 1;
        }
        return nascimentos;
    }

    /**
     * Um coelho pode se reproduzir se tiver atingido a idade de reprodução.
     */
    private boolean podeReproduzir()
    {
        return idade >= IDADE_REPRODUCAO;
    }
    
    /**
     * Verifica se o coelho está vivo ou não.
     * @return true se o coelho ainda estiver vivo.
     */
    public boolean estaVivo()
    {
        return vivo;
    }

    /**
     * Indica que o coelho foi comido.
     */
    public void foiComido()
    {
        vivo = false;
    }
    
    /**
     * Define a localização do coelho.
     * @param linha A coordenada vertical da localização.
     * @param coluna A coordenada horizontal da localização.
     */
    @Override
    public void setLocalizacao(int linha, int coluna)
    {
        this.localizacao = new Localizacao(linha, coluna);
    }

    /**
     * Define a localização do coelho.
     * @param localizacao A nova localização do coelho.
     */
    @Override
    public void setLocalizacao(Localizacao localizacao)
    {
        this.localizacao = localizacao;
    }
}
