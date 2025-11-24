// File: Planta.java
import java.util.List;
import java.util.Random;

/**
 * Modelo simples de uma planta.
 * Plantas não se movem. Elas podem ser comidas por coelhos
 * e podem rebrotar após algum tempo.
 *
 
 */
public class Planta implements Ator
{
    // Configuração da espécie
    private static final int IDADE_MAXIMA = 20;          
    private static final double PROBABILIDADE_REBROTA = 0.05;

    // Estado do indivíduo
    private int idade;
    private boolean ativa;
    private Localizacao localizacao;

    private static final Random rand = new Random();

     public Planta(boolean estadoAleatorio)
    {
        ativa = true;
        idade = 0;

        if(estadoAleatorio) {
            idade = rand.nextInt(IDADE_MAXIMA);
        }
    }

    @Override
    public void agir(Campo campoAtual, Campo campoAtualizado, List<Ator> novosAtores)
    {
        if(ativa) {
            idade++;
            if(idade > IDADE_MAXIMA) {
                ativa = false;
                idade = 0;
            }
        }
        else {
            if(rand.nextDouble() <= PROBABILIDADE_REBROTA) {
                ativa = true;
                idade = 0;
            }
        }

        if(ativa && localizacao != null) {
            campoAtualizado.colocar(this, localizacao);
        }
    }

    @Override
    public boolean estaAtivo()
    {
        return ativa;
    }

    public void serComida()
    {
        ativa = false;
        idade = 0;
    }

    @Override
    public Localizacao getLocalizacao()
    {
        return localizacao;
    }

    @Override
    public void definirLocalizacao(Localizacao nova)
    {
        localizacao = nova;
    }
}