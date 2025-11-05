import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Um modelo simples de uma raposa.
 * Raposas envelhecem, movem-se, caçam coelhos e morrem.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-11
 */
public class Raposa
{
    // Características compartilhadas por todas as raposas (campos estáticos).

    // A idade em que uma raposa pode começar a se reproduzir.
    private static final int IDADE_REPRODUCAO = 10;
    // A idade máxima que uma raposa pode alcançar.
    private static final int IDADE_MAXIMA = 150;
    // A probabilidade de uma raposa se reproduzir.
    private static final double PROBABILIDADE_REPRODUCAO = 0.09;
    // O número máximo de filhotes por ninhada.
    private static final int TAMANHO_MAXIMO_NINHADA = 3;
    // O valor de comida de um único coelho. Na prática, este é o
    // número de passos que uma raposa pode dar antes de precisar comer novamente.
    private static final int VALOR_COMIDA_COELHO = 4;
    // Um gerador de números aleatórios compartilhado para controlar a reprodução.
    private static final Random aleatorio = new Random();
    
    // Características individuais (campos de instância).

    // A idade da raposa.
    private int idade;
    // Se a raposa está viva ou não.
    private boolean viva;
    // A posição da raposa no campo.
    private Localizacao localizacao;
    // O nível de comida da raposa, que aumenta ao comer coelhos.
    private int nivelComida;

    /**
     * Cria uma raposa. Uma raposa pode ser criada como recém-nascida (idade zero)
     * e não faminta, ou com idade aleatória.
     * 
     * @param idadeAleatoria Se true, a raposa terá idade e fome aleatórias.
     */
    public Raposa(boolean idadeAleatoria)
    {
        idade = 0;
        viva = true;
        if(idadeAleatoria) {
            idade = aleatorio.nextInt(IDADE_MAXIMA);
            nivelComida = aleatorio.nextInt(VALOR_COMIDA_COELHO);
        }
        else {
            // Deixa a idade em 0.
            nivelComida = VALOR_COMIDA_COELHO;
        }
    }
    
    /**
     * Isto é o que a raposa faz na maior parte do tempo: caça coelhos.
     * Nesse processo, ela pode se reproduzir, morrer de fome
     * ou morrer de velhice.
     * 
     * @param campoAtual O campo atual.
     * @param campoAtualizado O campo atualizado para o novo passo.
     * @param novasRaposas Lista onde novas raposas nascidas serão adicionadas.
     */
    public void caca(Campo campoAtual, Campo campoAtualizado, List novasRaposas)
    {
        envelhecer();
        aumentarFome();
        if(estaViva()) {
            // Novas raposas nascem em localizações adjacentes.
            int nascimentos = reproduzir();
            for(int b = 0; b < nascimentos; b++) {
                Raposa novaRaposa = new Raposa(false);
                novasRaposas.add(novaRaposa);
                Localizacao loc = campoAtualizado.localizacaoAdjacenteAleatoria(localizacao);
                novaRaposa.setLocalizacao(loc);
                campoAtualizado.colocar(novaRaposa, loc);
            }
            // Move-se em direção a uma fonte de comida, se encontrada.
            Localizacao novaLocalizacao = procurarComida(campoAtual, localizacao);
            if(novaLocalizacao == null) {  // nenhuma comida encontrada — move-se aleatoriamente
                novaLocalizacao = campoAtualizado.localizacaoAdjacenteLivre(localizacao);
            }
            if(novaLocalizacao != null) {
                setLocalizacao(novaLocalizacao);
                campoAtualizado.colocar(this, novaLocalizacao);
            }
            else {
                // Não pode se mover nem ficar — superpopulação — todas as localizações ocupadas.
                viva = false;
            }
        }
    }
    
    /**
     * Aumenta a idade. Isso pode resultar na morte da raposa.
     */
    private void envelhecer()
    {
        idade++;
        if(idade > IDADE_MAXIMA) {
            viva = false;
        }
    }
    
    /**
     * Faz a raposa ficar mais faminta. Isso pode resultar em sua morte.
     */
    private void aumentarFome()
    {
        nivelComida--;
        if(nivelComida <= 0) {
            viva = false;
        }
    }
    
    /**
     * Diz à raposa para procurar coelhos adjacentes à sua localização atual.
     * @param campo O campo onde procurar.
     * @param localizacao A posição atual da raposa.
     * @return Onde a comida foi encontrada, ou null se não foi.
     */
    private Localizacao procurarComida(Campo campo, Localizacao localizacao)
    {
        Iterator adjacentes = campo.localizacoesAdjacentes(localizacao);
        while(adjacentes.hasNext()) {
            Localizacao onde = (Localizacao) adjacentes.next();
            Object animal = campo.getObjetoEm(onde);
            if(animal instanceof Coelho) {
                Coelho coelho = (Coelho) animal;
                if(coelho.estaVivo()) { 
                    coelho.foiComido();
                    nivelComida = VALOR_COMIDA_COELHO;
                    return onde;
                }
            }
        }
        return null;
    }
        
    /**
     * Gera um número representando o número de nascimentos,
     * se a raposa puder se reproduzir.
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
     * Uma raposa pode se reproduzir se tiver atingido a idade de reprodução.
     */
    private boolean podeReproduzir()
    {
        return idade >= IDADE_REPRODUCAO;
    }
    
    /**
     * Verifica se a raposa está viva ou não.
     * @return true se a raposa ainda estiver viva.
     */
    public boolean estaViva()
    {
        return viva;
    }

    /**
     * Define a localização da raposa.
     * @param linha A coordenada vertical.
     * @param coluna A coordenada horizontal.
     */
    public void setLocalizacao(int linha, int coluna)
    {
        this.localizacao = new Localizacao(linha, coluna);
    }

    /**
     * Define a localização da raposa.
     * @param localizacao A nova localização da raposa.
     */
    public void setLocalizacao(Localizacao localizacao)
    {
        this.localizacao = localizacao;
    }
}
